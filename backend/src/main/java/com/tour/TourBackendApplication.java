package com.tour;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tour.mapper")
public class TourBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TourBackendApplication.class, args);
    }
}
