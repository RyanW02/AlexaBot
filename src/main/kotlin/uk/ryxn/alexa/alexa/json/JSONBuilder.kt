package uk.ryxn.alexa.alexa.json

import org.json.JSONArray
import org.json.JSONObject

inline fun json(startingCode: String = "{}", block: JSONBuilder.() -> Unit) = JSONBuilder(startingCode).also { block(it) }

class JSONBuilder(startingCode: String) {

    private val json = JSONObject(startingCode)

    infix fun String.put(value: Any) = json.put(this, value)
    infix fun String.put(value: List<String>) = this.put(value.toTypedArray())
    infix fun String.put(value: Array<String>) = json.put(this, JSONArray(value))

    fun getMarshalled() = json.toString()
    fun getObject() = json
}