package com.example.integracje5;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Integracje5Application {


    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Integracje5Application.class);
        builder.headless(false).run(args);


        new Thread(() -> new MyFrame(new SpaceXService(new RestTemplate()))).start();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
