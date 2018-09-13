package uk.ryxn.alexa.config

import com.moandjiezana.toml.Toml
import uk.ryxn.alexa.AlexaBot
import java.io.File
import java.util.*

open class TOMLWrapper {

    lateinit var toml: Toml

    private val fileName = this::class.java.getDeclaredAnnotation(FileName::class.java).name
    private val file = File(fileName)

    fun load() {
        if(!file.exists()) {
            file.createNewFile()
            file.createFromResource(fileName)
        }

        val content = file.inputStream().bufferedReader().readLines().joinToString("\n")
        toml = Toml().read(content)
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    inline fun<reified T> getGenericOrNull(key: String): T? {
        return try {
            when (T::class.java) {
                Boolean::class.java -> toml.getBoolean(key, null)
                Date::class.java -> toml.getDate(key, null)
                Double::class.java -> toml.getDouble(key, null)
                Long::class.java -> toml.getLong(key, null)
                String::class.java -> toml.getString(key, null)
                Toml::class.java -> toml.getTable(key)

                Int::class.java -> toml.getLong(key, null)?.toInt()

                else -> throw IllegalArgumentException()
            } as T?
        } catch(ex: Exception) {
            (ex as? IllegalArgumentException)?.printStackTrace()

            null
        }
    }

    inline fun<reified T> getGeneric(key: String, default: T) = getGenericOrNull<T>(key) ?: default

    fun<T> getListOrNull(key: String) = toml.getList<T>(key, null)

    fun<T> getListOrEmpty(key: String) = toml.getList<T>(key, listOf())

    fun<T> getList(key: String, default: List<T>) = toml.getList<T>(key, default)

    fun contains(key: String) = toml.contains(key)

    private fun File.createFromResource(resourceName: String) {
        AlexaBot::class.java.getResourceAsStream(resourceName).use { inStream ->
            outputStream().use { outStream ->
                inStream.copyTo(outStream)
            }
        }
    }
}