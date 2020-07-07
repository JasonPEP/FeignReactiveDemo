package com.example.demo;

import feign.Param;
import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface UserInterface {

    @RequestLine("GET /user?userId={userId}")
    Mono<Integer> getUser(@Param("userId") String userId);
}
