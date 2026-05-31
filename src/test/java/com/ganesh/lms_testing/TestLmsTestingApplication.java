package com.ganesh.lms_testing;

import org.springframework.boot.SpringApplication;

public class TestLmsTestingApplication {

    public static void main(String[] args) {
        SpringApplication.from(LmsTestingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
