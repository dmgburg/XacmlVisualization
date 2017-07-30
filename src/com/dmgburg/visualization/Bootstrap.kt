package com.dmgburg.visualization

import org.springframework.boot.SpringApplication
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Import


@Import(JsonController::class)
class Bootstrap : SpringBootServletInitializer() {

}

fun main(args: Array<String>) {
    val run = SpringApplication.run(Bootstrap::class.java, *args)
    println("******************************")
    println("My custom startup message")
    println("******************************")
}