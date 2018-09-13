package uk.ryxn.alexa.alexa

import org.json.JSONObject
import spark.kotlin.ignite
import uk.ryxn.alexa.alexa.json.JSONWrapper
import uk.ryxn.alexa.audio.TrackLoader
import uk.ryxn.alexa.commands.CommandContext
import uk.ryxn.alexa.commands.impl.PlayCommand
import uk.ryxn.alexa.config.config
import uk.ryxn.alexa.getJDA
import uk.ryxn.alexa.guilds.getWrapper
import uk.ryxn.alexa.utils.getMusicManager

class AlexaServer: Runnable {

    private val guild = getJDA().getGuildById(config.responseGuild ?: -1)
    private val channel = guild.getTextChannelById(config.responseChannel ?: -1)
    private val member = guild.members.first { it.user.idLong == config.requestUser }

    override fun run() {
        val http = ignite()
        http.port(3355)

        http.post("/") {
            val scheduler = guild.getWrapper().scheduler
            val wrapped = JSONWrapper(JSONObject(request.body()))
            val intent = JSONWrapper(wrapped.getGeneric("request.intent", JSONObject()))

            response.status(200)
            response.header("Content-Type", "application/json;charset=UTF-8")

            val res = when(intent.getGeneric("name", "")) {
                "say" -> {
                    channel.sendMessage(intent.getGeneric("slots.message.value", "")).queue()

                    alexaResponse(wrapped, "Sent")
                }

                "add" -> {
                    val query = wrapped.getGenericOrNull<String>("request.intent.slots.song.value")

                    if(query == null) {
                        alexaResponse(wrapped, "Invalid query")
                    } else {
                        PlayCommand().queue(guild, channel, member, query, true)

                        alexaResponse(wrapped, "Queuing $query")
                    }
                }

                "skip" -> {
                    scheduler.next()
                    alexaResponse(wrapped, "Skipping the current song")
                }

                "AMAZON.StopIntent" -> {
                    scheduler.looping = false
                    scheduler.getQueue().clear()
                    scheduler.next()
                    alexaResponse(wrapped, "Stopping playback")
                }

                else -> alexaResponse(wrapped, "Invalid")
            }

            res
        }
    }
}