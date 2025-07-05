package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.entity.Wordcount
import com.example.study.blog.repository.WordRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono


@Service
class BlogService(
    @Qualifier("kakaoWebClient")
    private val webClient: WebClient,

    val wordRepository: WordRepository
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

        val lowQuery: String = blogDto.query.lowercase()
        val word: Wordcount = wordRepository.findById(lowQuery).orElse(Wordcount(lowQuery))
        word.cnt++

        wordRepository.save(word)

        return result

    }

    fun searchWordRank(): List<Wordcount> = wordRepository.findTo10ByOrderByCntDesc()
}

