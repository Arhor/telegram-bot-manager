package dev.arhor.telegram.bot.manager.data.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("telegram_bots")
class TelegramBot(
    @Id
    @Column
    override var id: Long?,

    @Column
    var owner: String,

    @Column
    var username: String,

    @Column
    var apitoken: String,
) : BusinessObject<Long>()
