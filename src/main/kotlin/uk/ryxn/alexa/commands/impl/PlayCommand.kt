package uk.ryxn.alexa.commands.impl

import kotlinx.coroutines.experimental.async
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.exceptions.ErrorResponseException
import uk.ryxn.alexa.audio.RequestType
import uk.ryxn.alexa.audio.TrackLoader
import uk.ryxn.alexa.commands.AbstractCommand
import uk.ryxn.alexa.commands.CommandContext
import uk.ryxn.alexa.guilds.getWrapper
import uk.ryxn.alexa.listeners.addReactCallback
import uk.ryxn.alexa.listeners.removeReactCallback
import uk.ryxn.alexa.utils.formatMillis
import uk.ryxn.alexa.utils.onComplete
import uk.ryxn.alexa.utils.sendPlain

class PlayCommand: AbstractCommand() {

    override val name = "play"

    override val description = "Plays a song"

    override val aliases = listOf("p")

    override fun CommandContext.root() {
        if(args.isEmpty()) {
            channel.sendPlain("You need to specify a search query")
            return
        }

        val query = args.joinToString(" ")
        queue(guild, channel, member,  query, false)
    }

    init {
        addSubcommand(object: AbstractCommand() {
            override val name = "-f"

            override val description = "Plays the first song of the selection"

            override val aliases = listOf("-first")

            override fun CommandContext.root() {
                if(args.isEmpty()) {
                    channel.sendPlain("You need to specify a search query")
                    return
                }

                val query = args.joinToString(" ")
                queue(guild, channel, member,  query, true)
            }
        })
    }

    fun queue(guild: Guild, channel: TextChannel, requester: Member, searchQuery: String, selectFirst: Boolean) {
        val amount =
                if(selectFirst) 1
                else 5

        val scheduler = guild.getWrapper().scheduler

        val type = RequestType.values().first { it.regex.matches(searchQuery) }
        val modifiedQuery =
                if(type == RequestType.YOUTUBE_SEARCH) "ytsearch: $searchQuery"
                else searchQuery

        async {
            TrackLoader.load(modifiedQuery, amount)
        }.onComplete { (tracks, isPlaylist) ->
            if(isPlaylist) { // Is playlist
                tracks.forEach { scheduler.queue(it, channel, requester) }
            } else {
                if(tracks.isEmpty()) {
                    channel.sendPlain("No videos found")
                }

                else if(selectFirst || tracks.size == 1) {
                    val track = tracks.first()
                    scheduler.queue(track, channel, requester)
                    channel.sendPlain("Queued **${track.info.title}** by **${track.info.author}** `[${track.duration.formatMillis()}]`")
                }

                else {
                    val content = tracks.withIndex().joinToString("\n") { (i, track) -> "${i+1}) **${track.info.title}** by **${track.info.author}** `[${track.duration.formatMillis()}]`" }
                    channel.sendPlain(content) { msg ->
                        try {
                            msg.addReaction("\u0031\u20E3").queue()
                            msg.addReaction("\u0032\u20E3").queue()

                            if (tracks.size >= 3) msg.addReaction("\u0033\u20E3").queue()
                            if (tracks.size >= 4) msg.addReaction("\u0034\u20E3").queue()
                            if (tracks.size >= 5) msg.addReaction("\u0035\u20E3").queue()
                        } catch(ex: ErrorResponseException) {} // Happens when user reacts before all emojis are added

                        msg.addReactCallback {
                            if(it.member != requester) return@addReactCallback

                            val track = when (it.emote.name) {
                                "\u0031\u20E3" -> tracks[0]
                                "\u0032\u20E3" -> tracks[1]
                                "\u0033\u20E3" -> tracks[2]
                                "\u0034\u20E3" -> tracks[3]
                                "\u0035\u20E3" -> tracks[4]
                                else -> throw IllegalArgumentException()
                            }

                            msg.removeReactCallback()

                            scheduler.queue(track, channel, requester)
                            channel.sendPlain("Queued **${track.info.title}** by **${track.info.author}** `[${track.duration.formatMillis()}]`")
                        }
                    }
                }
            }
        }
    }
}