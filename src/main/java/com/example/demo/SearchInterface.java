package com.example.demo;

import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface SearchInterface {

    @RequestLine("GET /s")
    Mono<String> search(@Param("keyword") String keyword);

}
