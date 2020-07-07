package com.example.demo;

import feign.RequestLine;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FeedInterface {

    @RequestLine("GET /feeds")
    Mono<List<String>> feeds();
}
