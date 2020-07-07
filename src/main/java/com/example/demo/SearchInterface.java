package com.example.demo;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

@Headers("Authorization: Bearer ABC")
public interface SearchInterface {

    @RequestLine("GET /s?keyword={keyword}")
    Mono<String> search(@Param("keyword") String keyword);
}
