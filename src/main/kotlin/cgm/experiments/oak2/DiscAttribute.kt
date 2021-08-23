package cgm.experiments.oak2

import java.time.LocalDate

sealed class DiscAttribute{
    data class Singer(val stageName: String): DiscAttribute()
    data class Genre(val name: String): DiscAttribute()
    data class Manager(val name: String, val email: String) : DiscAttribute()
    sealed class Song(open val title: String, open val lyrics: String) : DiscAttribute(){
        data class Single(override val title: String, override val lyrics: String, val singleDate: LocalDate): Song(title, lyrics)
        data class NotASingle(override val title: String, override val lyrics: String): Song(title, lyrics)

        companion object {
            fun of(attributes: List<String>) =
                if (attributes.size > 2) Single(attributes[0], attributes[1], LocalDate.parse(attributes[3]))
                else NotASingle(attributes[0], attributes[1])
        }
    }

    companion object {
        fun of(type: String, attributes: List<String>) = when (type) {
            "genre" -> Genre(attributes[0])
            "singer" -> Singer(attributes[0])
            "manager" -> Manager(attributes[0], attributes[1])
            "song" -> Song.of(attributes)
            else -> null
        }
    }
}