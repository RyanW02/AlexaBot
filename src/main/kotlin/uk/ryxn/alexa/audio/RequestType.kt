package uk.ryxn.alexa.audio

enum class RequestType(val regex: Regex) {
    YOUTUBE_URL(Regex("https?://(m|www\\.?)youtu(be.com|.be)/watch\\?v=([\\w]+)")),
    SOUNDCLOUD_URL(Regex("(?:https?)://(?:www.)?soundcloud.com/(?:.*)")),
    HTTP_URL(Regex("https?://(.*\\.)?([\\w]+)\\.([\\w]+)(/.*)?")),
    YOUTUBE_SEARCH(Regex(".*")),
}