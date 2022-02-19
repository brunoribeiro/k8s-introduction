package io.github.brunoribeiro.hello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class HelloWorldK8sApplication

fun main(args: Array<String>) {
    runApplication<HelloWorldK8sApplication>(*args)
}

@RestController
class HelloWorldK8sController {
    @RequestMapping("/")
    fun hello(): String = """
     |  Hello World k8s! 
     |  ${System.getenv("USERNAME")?.let { "USERNAME: $it" }.orEmpty()}
     |  ${System.getenv("POD_NAME")?.let { "NAME $it" }.orEmpty()}
     |  ${System.getenv("POD_IP")?.let { "IP: $it" }.orEmpty()}
    """.trimIndent()
}
