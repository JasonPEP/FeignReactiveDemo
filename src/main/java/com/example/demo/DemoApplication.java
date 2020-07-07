package com.example.demo;

import feign.Util;
import feign.reactive.ReactorFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
public class DemoApplication {


    @Autowired
    private HttpServletRequest httpServletRequest;

    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/s")
    public Mono<String> search(String keyword) {
        log.info("Receives the request. params[{}]", keyword);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just(keyword);
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

    @GetMapping("/test1")
    public String test1() {
        long begin = System.currentTimeMillis();
        Map<String, Object> requestResults = new ConcurrentHashMap<>();

        SearchInterface searchInterface = ReactorFeign.builder().target(SearchInterface.class, "http://localhost:8080/");

        UserInterface userInterface = ReactorFeign.builder()
                .decoder((response, type) -> {
                    return Integer.valueOf(Util.toString(response.body().asReader(Util.UTF_8)));
                })
                .target(UserInterface.class, "http://localhost:8080/");

        FeedInterface feedInterface = ReactorFeign.builder()
                .decoder((response, type) -> {
                    return null;
                })
                .target(FeedInterface.class, "http://localhost:8080/");

        // Subscribe search API
        Mono<String> searchSubscriber = searchInterface.search("abc").doOnSuccess(result -> {
            log.info("Search API result [{}]", result);
            requestResults.put("search", result);
        });

        // Subscribe user API
        Mono<Integer> userSubscriber = userInterface.getUser("user 1").doOnSuccess(result -> {
            log.info("User API result [{}]", result);
            requestResults.put("user", result);
        });

        // Subscribe feed API
        Mono<List<String>> feedSubscriber = feedInterface.feeds().doOnSuccess(results -> {
            log.info("Feeds API [{}]", results);
            requestResults.put("feeds", results);
        });

        Flux.merge(searchSubscriber, userSubscriber).doOnComplete(() -> {
            log.info("All API requests on complete");
        }).then().block();

        long requestTime = System.currentTimeMillis() - begin;
        log.info("Request time[{}]", requestTime);
        log.info("[{}]", requestResults);
        return String.format("Request time [%s]", requestTime);
    }

    @GetMapping("/test2")
    public String test2() {
        long begin = System.currentTimeMillis();
        Map<String, Object> requestResults = new ConcurrentHashMap<>();

        SearchInterface searchInterface = ReactorFeign.builder().target(SearchInterface.class, "http://localhost:8080/");

        UserInterface userInterface = ReactorFeign.builder()
                .decoder((response, type) -> Integer.valueOf(Util.toString(response.body().asReader(Util.UTF_8))))
                .target(UserInterface.class, "http://localhost:8080/");


        searchInterface.search("abc").doOnSuccess(result -> {
            log.info("Search API result [{}]", result);
            requestResults.put("search", result);
        }).block();

        userInterface.getUser("user 1").doOnSuccess(result -> {
            log.info("User API result [{}]", result);
            requestResults.put("user", result);
        }).block();


        long requestTime = System.currentTimeMillis() - begin;
        log.info("Request time[{}]", requestTime);
        log.info("[{}]", requestResults);
        return String.format("Request time [%s]", requestTime);
    }

    @GetMapping("/test3")
    public String test3() {
        long begin = System.currentTimeMillis();
        Map<String, Object> requestResults = new ConcurrentHashMap<>();

        SearchInterface searchInterface = ReactorFeign.builder().target(SearchInterface.class, "http://localhost:8080/");

        UserInterface userInterface = ReactorFeign.builder()
                .decoder((response, type) -> Integer.valueOf(Util.toString(response.body().asReader(Util.UTF_8))))
                .target(UserInterface.class, "http://localhost:8080/");


        // Subscribe search API
        Mono<String> searchSubscriber = searchInterface.search("abc").doOnSuccess(result -> {
            log.info("Search API result [{}]", result);
            requestResults.put("search", result);
        });

        // Subscribe user API
        Mono<Integer> userSubscriber = userInterface.getUser("user 1").doOnSuccess(result -> {
            log.info("User API result [{}]", result);
            requestResults.put("user", result);
        });


        Flux.merge(searchSubscriber, userSubscriber).doOnComplete(() -> {
            log.info("All API requests on complete");
        }).then().subscribe();

        long requestTime = System.currentTimeMillis() - begin;
        log.info("Request time[{}]", requestTime);
        log.info("[{}]", requestResults);
        return String.format("Request time [%s]", requestTime);
    }

    @GetMapping("/test4")
    public Mono<String> test4() {
        WebClient webClient = WebClient.create();
        Mono<String> stringMono = webClient.get().uri("http://localhost:8080/s?keyword=aa").retrieve().bodyToMono(String.class);
        long begin = System.currentTimeMillis();
        stringMono.subscribe(s -> System.out.println(s));
        log.info("{}", System.currentTimeMillis() - begin);
        return Mono.just("stringMono");
    }
}
