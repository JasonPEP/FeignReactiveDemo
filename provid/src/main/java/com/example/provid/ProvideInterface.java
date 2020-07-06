package com.example.provid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@FeignClient("provide")
public interface ProvideInterface {

    @GetMapping("/")
    Flux<String> getProvide(@RequestParam("type") String type);

}
