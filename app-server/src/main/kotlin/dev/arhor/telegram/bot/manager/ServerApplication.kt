package dev.arhor.telegram.bot.manager

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

@SpringBootApplication(proxyBeanMethods = false)
class ServerApplication : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        /* no-op */
    }
}