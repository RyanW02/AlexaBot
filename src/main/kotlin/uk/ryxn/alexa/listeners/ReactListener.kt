package uk.ryxn.alexa.listeners

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageReaction
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import uk.ryxn.alexa.utils.Callback
import java.util.concurrent.TimeUnit

private val callbacks = mutableMapOf<Long, Callback<Reaction>>()

fun Message.addReactCallback(callback: Callback<Reaction>) {
    val id = idLong
    callbacks[id] = callback

    launch {
        delay(1, TimeUnit.MINUTES)
        removeReactCallback()
    }
}

fun Message.removeReactCallback() = callbacks.remove(idLong)

class ReactListener: ListenerAdapter() {

    override fun onMessageReactionAdd(e: MessageReactionAddEvent) {
        val msgId = e.messageIdLong

        callbacks[msgId]?.invoke(Reaction(e.member, e.reactionEmote))
    }
}

data class Reaction(val member: Member, val emote: MessageReaction.ReactionEmote)