package dev.arhor.telegram.bot.manager.web;

import org.springframework.security.core.Authentication;

interface OwnerResolver {

    fun resolveOwnerId(authentication: Authentication): String
}
