package uk.ryxn.alexa.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager

lateinit var playerManager: AudioPlayerManager

class AudioManager() {

    fun load() {
        playerManager = DefaultAudioPlayerManager()
        playerManager.configuration.isFilterHotSwapEnabled = true

        setOf<AudioSourceManager>(
                YoutubeAudioSourceManager(true),
                SoundCloudAudioSourceManager(true),
                HttpAudioSourceManager(),
                BandcampAudioSourceManager(),
                VimeoAudioSourceManager(),
                TwitchStreamAudioSourceManager(),
                BeamAudioSourceManager()
        ).forEach(playerManager::registerSourceManager)

        AudioSourceManagers.registerRemoteSources(playerManager)
    }
}