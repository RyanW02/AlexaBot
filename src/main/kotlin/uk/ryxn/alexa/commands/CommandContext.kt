package uk.ryxn.alexa.commands

import net.dv8tion.jda.core.entities.*

data class CommandContext(val user: User, val member: Member, val guild: Guild, val channel: TextChannel, val msg: Message, val args: List<String>)