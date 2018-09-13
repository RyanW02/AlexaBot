package uk.ryxn.alexa.alexa

import uk.ryxn.alexa.alexa.json.JSONWrapper
import uk.ryxn.alexa.alexa.json.json

fun alexaResponse(wrapped: JSONWrapper, text: String) = json {
    "version" put wrapped.getGeneric("version", 1.0)
    "response" put json {
        "outputSpeech" put json {
            "type" put "PlainText"
            "text" put text
        }.getObject()
    }.getObject()
}.getMarshalled()