package uk.ryxn.alexa.config

@Target(AnnotationTarget.CLASS)
annotation class FileName(val name: String = "config.toml")