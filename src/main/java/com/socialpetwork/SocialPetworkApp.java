package com.socialpetwork;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SocialPetworkApp {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        Map<String, Object> props = new HashMap<>();
        props.put("spring.datasource.url", dotenv.get("DB_URL"));
        props.put("spring.datasource.username", dotenv.get("DB_USERNAME"));
        props.put("spring.datasource.password", dotenv.get("DB_PASSWORD"));

        SpringApplication app = new SpringApplication(SocialPetworkApp.class);
        app.setDefaultProperties(props);
        app.run(args);
    }
}

