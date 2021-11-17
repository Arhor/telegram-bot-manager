package dev.arhor.telegram.bot.manager.data.repository

import dev.arhor.telegram.bot.manager.data.model.TelegramBot
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TelegramBotRepository : CrudRepository<TelegramBot, Long> {
}