package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.core.exception.InvalidInputException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono


@Service
class BlogService(
    @Qualifier("kakaoWebClient")
    private val webClient: WebClient
) {
    fun searchKakao(blogDto: BlogDto): String? {
        val msgList = mutableListOf<ExceptionMsg>()

        if (blogDto.query.trim().isEmpty()) {
            msgList.add(ExceptionMsg.EMPTY_QUERY)
        }

        if (blogDto.sort.trim() !in arrayOf("accuracy", "recency")) {
            msgList.add(ExceptionMsg.NOT_IN_SORT)
        }

        when {
            blogDto.page < 1 -> msgList.add(ExceptionMsg.LESS_THAN_MIN)
            blogDto.page > 50 -> msgList.add(ExceptionMsg.MORE_THEN_MAX)
        }

        if (msgList.isNotEmpty()) {
            val message = msgList.joinToString {it.msg}
            throw InvalidInputException(message)
        }

        val response = webClient.get()
            .uri {
                it.path("/v2/search/blog")
                    .queryParam("query", blogDto.query)
                    .queryParam("sort", blogDto.sort)
                    .queryParam("page", blogDto.page)
                    .queryParam("size", blogDto.size)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()

        val result = response.block()

        return result

    }
}

private enum class ExceptionMsg(val msg: String) {
    EMPTY_QUERY("Empty Query"),
    NOT_IN_SORT("NotInSort By Sort"),
    LESS_THAN_MIN("page is less than min"),
    MORE_THEN_MAX("Page is more than max"),

}