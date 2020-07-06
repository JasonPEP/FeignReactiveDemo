package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "feed", url = "http://localhost:8080")
public interface FeedInterface {

    @GetMapping("/feeds")
    List<String> feeds();
}
