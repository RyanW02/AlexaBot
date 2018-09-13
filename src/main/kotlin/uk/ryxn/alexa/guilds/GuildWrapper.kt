package uk.ryxn.alexa.guilds

import net.dv8tion.jda.core.entities.Guild
import uk.ryxn.alexa.audio.TrackScheduler
import uk.ryxn.alexa.utils.getMusicManager

class GuildWrapper(val guild: Guild) {

    val scheduler: TrackScheduler
        get() = guild.getMusicManager().getScheduler()
}