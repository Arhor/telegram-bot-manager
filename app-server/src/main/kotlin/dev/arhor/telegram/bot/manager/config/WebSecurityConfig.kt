package dev.arhor.telegram.bot.manager.config

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.util.*


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration(proxyBeanMethods = false)
class WebSecurityConfig(
    private val adminServer: AdminServerProperties,
    private val security: SecurityProperties,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {

        http.authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .oauth2Login()
            .and()
            .csrf()
            .disable()
            .headers()
            .xssProtection()
            .and()
            .contentSecurityPolicy("script-src 'self'")
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        with(security.user) {
            auth.inMemoryAuthentication()
                .withUser(name)
                .password("{noop}$password")
                .roles("USER")
        }
    }

    // TODO: fix configurer method to wor correctly
    @Suppress("unused")
    private fun configureHttpSecurityForSpringBootAdmin(http: HttpSecurity) {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler().apply {
            setTargetUrlParameter("redirectTo")
            setDefaultTargetUrl(adminServer.path("/"))
        }

        http
            .authorizeRequests { authorizeRequests ->
                authorizeRequests
                    .antMatchers(adminServer.path("/assets/**")).permitAll()
                    .antMatchers(adminServer.path("/actuator/info")).permitAll()
                    .antMatchers(adminServer.path("/actuator/health")).permitAll()
                    .antMatchers(adminServer.path("/login")).permitAll()
                    .anyRequest().authenticated()

            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage(adminServer.path("/login"))
                    .successHandler(successHandler).and()
            }

            .logout { logout -> logout.logoutUrl(adminServer.path("/logout")) }

            .httpBasic(Customizer.withDefaults())

            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers(
                        AntPathRequestMatcher(adminServer.path("/instances"), HttpMethod.POST.toString()),
                        AntPathRequestMatcher(adminServer.path("/instances/*"), HttpMethod.DELETE.toString()),
                        AntPathRequestMatcher(adminServer.path("/actuator/**"))
                    )
            }
            .rememberMe { rememberMe ->
                rememberMe
                    .key(UUID.randomUUID().toString())
                    .tokenValiditySeconds(1209600)
            }
    }
}
