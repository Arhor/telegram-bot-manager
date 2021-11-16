package dev.arhor.telegram.bot.manager.config

import dev.arhor.telegram.bot.manager.service.TimeService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

@Configuration(proxyBeanMethods = false)
@EnableJdbcAuditing(modifyOnCreate = false, dateTimeProviderRef = "instantDateTimeProviderUTC")
@EnableJdbcRepositories(basePackages = ["dev.arhor.telegram.bot.manager.data.*"])
@EnableTransactionManagement
class DatabaseConfig {

    @Bean
    fun instantDateTimeProviderUTC(timeService: TimeService) = DateTimeProvider { Optional.of(timeService.now()) }
}