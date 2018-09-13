package uk.ryxn.alexa.guilds

import net.dv8tion.jda.core.entities.Guild

private val guildWrappers = mutableMapOf<Guild, GuildWrapper>()

fun Guild.getWrapper(): GuildWrapper {
    val wrapper = guildWrappers[this]

    if(wrapper == null) {
        guildWrappers[this] = GuildWrapper(this)
        return getWrapper()
    }

    return wrapper
}