package dev.arhor.telegram.bot.manager.config

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableAdminServer
class AdminServerConfig {
}