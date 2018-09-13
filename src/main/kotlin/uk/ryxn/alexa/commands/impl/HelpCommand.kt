package uk.ryxn.alexa.commands.impl

import uk.ryxn.alexa.commands.AbstractCommand
import uk.ryxn.alexa.commands.CommandContext
import uk.ryxn.alexa.commands.CommandManager
import uk.ryxn.alexa.utils.sendPlain
import uk.ryxn.alexa.utils.sendPM

class HelpCommand: AbstractCommand() {

    override val name = "help"

    override val description = "Sends you a list of all the commands with their descriptions"

    override val aliases = emptyList<String>()

    override fun CommandContext.root() {
        channel.sendPlain("Check your DMs")
        user.sendPM(CommandManager.commands.joinToString("\n") { "**${it.name}** - ${it.description}" })
    }
}