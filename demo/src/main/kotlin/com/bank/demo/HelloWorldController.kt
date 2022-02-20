package com.bank.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/hello")
class HelloWorladController {
    @GetMapping
    fun helloWorld(): String = "Hello, this is REST endpoint"
}