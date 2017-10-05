package com.example.demo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties
class DemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(DemoApplication::class.java, *args)
}
