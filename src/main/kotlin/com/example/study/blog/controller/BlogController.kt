package com.example.study.blog.controller

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.entity.Wordcount
import com.example.study.blog.service.BlogService
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/blog")
@RestController
class BlogController(
    val blogService: BlogService
) {

    @GetMapping("")
    fun search(@ParameterObject @Valid blogDto: BlogDto): String? {
        val result = blogService.searchKakao(blogDto)
        return result
    }

    @GetMapping("/rank")
    fun searchWordRank(): List<Wordcount> = blogService.searchWordRank()

}