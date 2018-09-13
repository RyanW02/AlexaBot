package uk.ryxn.alexa.audio

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel

data class AudioTrackWrapper(val track: AudioTrack, val channel: TextChannel, val requester: Member)