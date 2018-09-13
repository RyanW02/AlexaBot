package uk.ryxn.alexa.utils

import kotlinx.coroutines.experimental.Deferred

fun<T> Deferred<T>.onComplete(block: (T) -> Unit) = invokeOnCompletion { block(getCompleted()) }