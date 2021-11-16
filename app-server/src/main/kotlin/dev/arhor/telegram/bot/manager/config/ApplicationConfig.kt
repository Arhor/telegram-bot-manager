package dev.arhor.telegram.bot.manager.config

import dev.arhor.telegram.bot.manager.service.TimeService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.temporal.ChronoUnit

@Configuration(proxyBeanMethods = false)
class ApplicationConfig {

    @Bean
    fun timeService() = TimeService { Instant.now().truncatedTo(ChronoUnit.MILLIS) }
}