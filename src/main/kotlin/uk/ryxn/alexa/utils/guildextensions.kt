package uk.ryxn.alexa.utils

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageChannel
import uk.ryxn.alexa.audio.GuildAudioManager
import uk.ryxn.alexa.audio.GuildAudioManagerFactory
import java.util.concurrent.TimeUnit

private val audioManagers = mutableMapOf<Long, GuildAudioManager>()

fun Guild.getMusicManager(): GuildAudioManager {
    val am = audioManagers[idLong]
    return if(am == null) {
        audioManagers[idLong] = GuildAudioManagerFactory().create(this)
        getMusicManager()
    } else am
}

fun MessageChannel.sendEmbed(title: String, message: String, color: Int, inline: Boolean = false, callback: Callback<Message> = {}) {
    sendMessage(EmbedBuilder()
            .setColor(color)
            .addField(title, message, inline)
            .build())
            .queue(callback)
}

fun MessageChannel.sendEmbed(title: String, message: String, color: Colors = Colors.GREEN, inline: Boolean = false, callback: Callback<Message> = {}) = sendEmbed(title, message, color.denary, inline, callback)

fun MessageChannel.sendEmbed(message: String, color: Colors = Colors.GREEN, callback: Callback<Message> = {}) {
    sendMessage(EmbedBuilder()
            .setColor(color.denary)
            .setDescription(message)
            .build())
            .queue(callback)
}

fun MessageChannel.sendPlain(contents: String) = sendMessage(contents).queue()
fun MessageChannel.sendPlain(contents: String, callback: Callback<Message>) = sendMessage(contents).queue(callback)

enum class Colors(val denary: Int) {
    GREEN(2335514),
    RED(11010048),
    ORANGE(16740864),
    LIME(7658240),
    BLUE(472219),
}

fun Long.formatMillis() = String.format("%d min, %d sec",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this)))