package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate

class CsvReadTests {

    @TestFactory
    internal fun `can read disc attributes from a csv`() = listOf(
        "genre;Rap" to DiscAttributes.Genre("Rap"),
        "singer;Caparezza" to DiscAttributes.Singer("Caparezza"),
        "manager;Dario Pianta;dario.pianta@cgm.com" to DiscAttributes.Manager("Dario Pianta", "dario.pianta@cgm.com"),
        "song;Il dito medio di Galileo;del testo a caso" to DiscAttributes.Song.NotASingle("Il dito medio di Galileo", "del testo a caso"),
        "song;Il dito medio di Galileo;del testo a caso;1;2021-08-20" to DiscAttributes.Song.Single("Il dito medio di Galileo", "del testo a caso", LocalDate.of(2021 , 8, 20))
    )
    .map { (inputStr, expectedResult) ->
        dynamicTest("given $inputStr i expect $expectedResult") {
            val result = CsvReader.read(inputStr)

            result shouldBe expectedResult
    } }

}

object CsvReader{
    fun read(inputStr: String): DiscAttributes? {
        val elements: List<String> = inputStr.split(";")
        return DiscAttributes.of(elements.first(), elements.drop(1))
    }

}

sealed class DiscAttributes{
    data class Singer(val stageName: String): DiscAttributes()
    data class Genre(val name: String): DiscAttributes()
    data class Manager(val name: String, val email: String) : DiscAttributes()
    sealed class Song(open val title: String, open val lyrics: String) : DiscAttributes(){
        data class Single(override val title: String, override val lyrics: String, val singleDate: LocalDate): Song(title, lyrics)
        data class NotASingle(override val title: String, override val lyrics: String): Song(title, lyrics)
    }

    companion object {
        fun of(type: String, attributes: List<String>) = when (type) {
            "genre" -> Genre(attributes[0])
            "singer" -> Singer(attributes[0])
            "manager" -> Manager(attributes[0], attributes[1])
            "song" ->
                if(attributes.size > 2) Song.Single(attributes[0], attributes[1], LocalDate.parse(attributes[3]))
                else Song.NotASingle(attributes[0], attributes[1])
            else -> null
        }
    }
}

fun soundASingle(song: DiscAttributes.Song.Single){
    println("sounding this song: ${song.title} released on ${song.singleDate}")
}

fun listenASong(song: DiscAttributes.Song){
    println("sounding this song: ${song.title}")
}

fun emailASong(song: DiscAttributes.Song.Single){
    emailme("sounding this song: ${song.title} released on ${song.singleDate}")
}

fun emailme(s: String) {
    TODO("Not yet implemented")
}
