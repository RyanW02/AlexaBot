package uk.ryxn.alexa.commands.impl

import uk.ryxn.alexa.commands.AbstractCommand
import uk.ryxn.alexa.commands.CommandContext
import uk.ryxn.alexa.guilds.getWrapper
import uk.ryxn.alexa.utils.sendPlain

class StopCommand: AbstractCommand() {

    override val name = "stop"

    override val description = "Stops playback and clears the queue"

    override val aliases = emptyList<String>()

    override fun CommandContext.root() {
        val scheduler = guild.getWrapper().scheduler
        scheduler.looping = false
        scheduler.getQueue().clear()
        scheduler.next()

        channel.sendPlain("Stopped playback")
    }
}