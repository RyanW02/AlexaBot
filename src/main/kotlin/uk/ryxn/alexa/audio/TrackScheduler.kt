package uk.ryxn.alexa.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import uk.ryxn.alexa.utils.formatMillis
import uk.ryxn.alexa.utils.sendEmbed
import uk.ryxn.alexa.utils.sendPlain
import java.util.*

class TrackScheduler(val player: AudioPlayer, val guildId: Long, val shard: JDA): AudioEventAdapter() {

    private val queue = LinkedList<AudioTrackWrapper>()
    var playing: AudioTrackWrapper? = null
    var looping = false

    init {
        player.addListener(this)
        getGuild().audioManager.sendingHandler = AudioPlayerSendHandler(player)
    }

    fun queue(track: AudioTrack, channel: TextChannel, requester: Member) = queue(AudioTrackWrapper(track, channel, requester))

    fun queue(wrapper: AudioTrackWrapper) {
        queue.add(wrapper)
        playing ?: next()
    }

    fun getQueue() = queue

    fun next() {
        player.stopTrack()

        if(looping && playing != null) queue.add(AudioTrackWrapper(playing!!.track.makeClone(), playing!!.channel, playing!!.requester))

        if(queue.isEmpty()) {
            playing = null
            getGuild().audioManager.closeAudioConnection()
            return
        }

        val wrapper = queue.pop() ?: return
        playing = wrapper

        if(!getGuild().audioManager.isConnected)  {
            if(!wrapper.requester.voiceState.inVoiceChannel()) {
                wrapper.channel.sendPlain("You are not in a voice channel")
                return
            }

            getGuild().audioManager.openAudioConnection(playing!!.requester.voiceState.channel)
        }

        player.startTrack(wrapper.track, false)
    }

    override fun onTrackStart(player: AudioPlayer, track: AudioTrack) {
        val wrapper = playing ?: return

        wrapper.channel.sendEmbed("Music", "Now playing: **${wrapper.track.info.title}** `[${wrapper.track.duration.formatMillis()}]`")
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if(endReason.mayStartNext) next()
    }

    private fun getGuild() = shard.getGuildById(guildId)
}
