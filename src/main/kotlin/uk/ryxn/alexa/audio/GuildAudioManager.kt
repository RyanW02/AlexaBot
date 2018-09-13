package uk.ryxn.alexa.audio

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.core.entities.Member

abstract class GuildAudioManager {

    abstract fun getPlayerManager(): AudioPlayerManager
    abstract fun getPlayer(): AudioPlayer
    abstract fun getScheduler(): TrackScheduler
    abstract fun getEqualizer(): EqualizerFactory

    abstract val voteSkips: MutableList<Member>
}
