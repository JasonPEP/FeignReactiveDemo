package com.example.demo;

import feign.reactive.ReactorFeign;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.util.function.Tuple2;
import rx.plugins.RxJavaCompletableExecutionHook;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableFeignClients
@SpringBootApplication
@RestController
public class DemoApplication {


    @Autowired
    private FeedInterface feedInterface;

    @Autowired
    private UserInterface userInterface;

    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/s")
    public Mono<String> search(String keyword) {
        log.info(keyword);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just("abs");
    }

    @GetMapping("/feeds")
    public Mono<List<String>> feeds() {
        List<String> strings = Arrays.asList("a", "b");
        try {
            log.info("getting feeds...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("e", e);
        }
        return Mono.just(strings);
    }

    @GetMapping("/user")
    public Mono<Integer> getUser(String userId) {
        try {
            log.info("getting user... {}", userId);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("", e);
        }
        return Mono.just(123);
    }

    @GetMapping("/test")
    public String test() {
        long begin = System.currentTimeMillis();https://github.com/JasonPEP/FeignReactiveDemo.git
        SearchInterface target = ReactorFeign.builder().target(SearchInterface.class, "http://baidu.com");
        Mono<String> abc = target.search("abc");
        abc.doOnSuccess(result -> log.info("{}", result)).subscribe();
//        Mono<Object> aaabc = Mono.create(sink -> sink.success(searchInterface.search("aaabc")));

        log.info("{}", System.currentTimeMillis() - begin);
        return "test";
    }
Å
//    @GetMapping("/test2")
//    public Mono<Map<String, Object>> test2() {
//        Map<String, Object> m = new HashMap<>();
//        Mono<String> m1 = Mono.just(searchInterface.search("apple")).doOnSuccess(result -> {
//            log.info("request search success!!! {}", result);
//            m.put("searchInterface", result);
//        });
//        Mono<Integer> m2 = Mono.just(userInterface.getUser("u1u1")).doOnSuccess(user -> {
//            log.info("request user success!!! {}", user);
//            m.put("userInterface", user);
//        });
//        Mono<List<String>> f1 = Mono.just(feedInterface.feeds()).doOnSuccess(feeds -> {
//            log.info("request feeds success!!! {}", feeds);
//            m.put("feed", feeds);
//        });
//        long begin = System.currentTimeMillis();
//        Flux<Object> merge = Flux.merge(m1, m2, f1);
//        merge.doOnComplete(() -> log.info("{}", m)).then().block();
//        log.info("{}", System.currentTimeMillis() - begin);
//        return Mono.just(m);
//    }

    @GetMapping("/test3")
    public Mono<String> test3() {
        WebClient webClient = WebClient.create();
        Mono<String> stringMono = webClient.get().uri("http://localhost:8080/s?keyword=aa").retrieve().bodyToMono(String.class);
        long begin = System.currentTimeMillis();
        stringMono.subscribe(s -> System.out.println(s));
        log.info("{}", System.currentTimeMillis() - begin);
        return Mono.just("stringMono");
    }
}

