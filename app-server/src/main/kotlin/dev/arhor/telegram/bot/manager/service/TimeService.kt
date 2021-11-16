package dev.arhor.telegram.bot.manager.service

import java.time.Instant

fun interface TimeService {

    fun now(): Instant
}