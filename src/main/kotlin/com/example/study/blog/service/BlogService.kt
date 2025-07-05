package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
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

