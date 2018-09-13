package uk.ryxn.alexa.utils

import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User

fun User.sendPM(message: String) = openPrivateChannel().queue { it.sendPlain(message) }

fun User.sendPM(embed: MessageEmbed) = openPrivateChannel().queue { it.sendMessage(embed).queue() }