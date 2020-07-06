package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", url = "http://localhost:8080")
public interface UserInterface {

    @GetMapping("/user")
    Integer getUser(@RequestParam("userId") String userId);
}
