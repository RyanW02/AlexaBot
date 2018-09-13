package uk.ryxn.alexa.commands

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import uk.ryxn.alexa.commands.impl.HelpCommand
import uk.ryxn.alexa.commands.impl.PlayCommand
import uk.ryxn.alexa.commands.impl.SkipCommand
import uk.ryxn.alexa.commands.impl.StopCommand
import uk.ryxn.alexa.config.config

class CommandManager: ListenerAdapter() {

    companion object {
        val commands = mutableListOf(
                HelpCommand(),
                PlayCommand(),
                SkipCommand(),
                StopCommand()
        )
    }

    override fun onGuildMessageReceived(e: GuildMessageReceivedEvent) {
        if(e.author.isBot) return

        val raw = e.message.contentStripped

        val prefix = config.prefix ?: return
        if(!raw.startsWith(prefix)) return

        val split = raw.substring(prefix.length).split(" ")

        val root = split[0]
        val args =
                if(split.size > 1) split.subList(1, split.size)
                else emptyList()

        val ctx = CommandContext(e.author, e.member, e.guild, e.channel, e.message, args)
        val cmd = commands.firstOrNull { it.name.equals(root, true) || it.aliases.any { it.equals(root, true) } } ?: return

        if(args.isEmpty()) cmd.executeRoot(ctx)
        else {
            var subCmd = cmd
            var i = 0
            for(arg in args) {
                val tmp = subCmd.subcommands.firstOrNull { it.name.equals(arg, true) || it.aliases.any { it.equals(arg, true) } } ?: break
                subCmd = tmp
                i++
            }

            val subCtx = CommandContext(ctx.user, ctx.member, ctx.guild, ctx.channel, ctx.msg, args.subList(i, args.size))
            subCmd.executeRoot(subCtx)
        }
    }
}