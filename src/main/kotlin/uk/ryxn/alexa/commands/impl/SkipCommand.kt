package uk.ryxn.alexa.commands.impl

import uk.ryxn.alexa.commands.AbstractCommand
import uk.ryxn.alexa.commands.CommandContext
import uk.ryxn.alexa.guilds.getWrapper
import uk.ryxn.alexa.utils.sendPlain

class SkipCommand: AbstractCommand() {

    override val name = "skip"

    override val description = "Skips the current song"

    override val aliases = emptyList<String>()

    override fun CommandContext.root() {
        val scheduler = guild.getWrapper().scheduler
        scheduler.next()

        channel.sendPlain("${member.effectiveName} skipped the song")
    }
}