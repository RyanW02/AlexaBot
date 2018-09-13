package uk.ryxn.alexa

import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import uk.ryxn.alexa.alexa.AlexaServer
import uk.ryxn.alexa.audio.AudioManager
import uk.ryxn.alexa.commands.CommandManager
import uk.ryxn.alexa.config.Config
import uk.ryxn.alexa.config.config
import uk.ryxn.alexa.listeners.ReactListener

fun main(args: Array<String>) = AlexaBot().run()

fun getJDA() = AlexaBot.jda

class AlexaBot: Runnable {

    companion object {
        lateinit var jda: JDA
    }

    override fun run() {
        config = Config()
        config.load()

        AudioManager().load()

        jda = JDABuilder(AccountType.BOT)
                .setToken(config.token)
                .setAudioEnabled(true)
                .addEventListener(
                        CommandManager(), ReactListener())
                .buildBlocking()

        launch {
            AlexaServer().run()
        }
    }
}