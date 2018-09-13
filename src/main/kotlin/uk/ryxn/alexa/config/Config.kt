package uk.ryxn.alexa.config

lateinit var config: Config

@FileName("config.toml")
class Config: TOMLWrapper() {

    val token: String?
        get() = getGenericOrNull<String>("bot.token")
    val prefix: String?
        get() = getGenericOrNull<String>("bot.prefix")

    val responseGuild: Long?
        get() = toml.getLong("alexa.response-guild")
    val responseChannel: Long?
        get() = toml.getLong("alexa.response-channel")
    val requestUser: Long?
        get() = toml.getLong("alexa.request-user")
}