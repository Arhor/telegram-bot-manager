package dev.arhor.telegram.bot.manager.web

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component

@Component
class OwnerResolverImpl : OwnerResolver {

    companion object {
        private const val SEPARATOR = ":"
    }

    @Override
    override fun resolveOwnerId(authentication: Authentication): String {
        return when (authentication) {
            is OAuth2AuthenticationToken -> resolveOwnerIdFromOAuth2Token(authentication)
            else -> authentication.name
        }
    }

    private fun resolveOwnerIdFromOAuth2Token(oAuth2Token: OAuth2AuthenticationToken): String {
        val registrationId = oAuth2Token.authorizedClientRegistrationId
        val name = oAuth2Token.name
        return "$registrationId$SEPARATOR$name"
    }
}
