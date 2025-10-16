package org.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    println("시작 돠었습니다.")
    SpringApplication.run(Application::class.java, *args)
}