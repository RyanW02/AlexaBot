package uk.ryxn.alexa.commands

abstract class AbstractCommand {

    val subcommands = mutableListOf<AbstractCommand>()

    abstract val name: String
    abstract val description: String
    abstract val aliases: List<String>

    abstract fun CommandContext.root()

    fun addSubcommand(subCmd: AbstractCommand) {
        subcommands.add(subCmd)
    }

    fun executeRoot(commandContext: CommandContext) = commandContext.root()

    fun getSubCommandByName(name: String) = subcommands.firstOrNull { it.name.equals(name, true) }
}