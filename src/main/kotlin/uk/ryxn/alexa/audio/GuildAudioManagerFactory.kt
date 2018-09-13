package uk.ryxn.alexa.audio

import com.sedmelluq.discord.lavaplayer.filter.equalizer.EqualizerFactory
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member

class GuildAudioManagerFactory {

    fun create(guild: Guild): GuildAudioManager {
        return object: GuildAudioManager() {

            private val player = getPlayerManager().createPlayer()
            private val equalizer = EqualizerFactory()
            private val ts = TrackScheduler(player, guild.idLong, guild.jda)

            override fun getPlayerManager() = playerManager

            override fun getPlayer() = player

            override fun getScheduler() = ts

            override fun getEqualizer() = equalizer

            override val voteSkips = mutableListOf<Member>()
        }
    }
}