package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate

class CsvReadTests {

    @TestFactory
    internal fun `can read disc attributes from a csv`() = listOf(
        "genre;Rap" to DiscAttribute.Genre("Rap"),
        "singer;Caparezza" to DiscAttribute.Singer("Caparezza"),
        "manager;Dario Pianta;dario.pianta@cgm.com" to DiscAttribute.Manager("Dario Pianta", "dario.pianta@cgm.com"),
        "song;Il dito medio di Galileo;del testo a caso" to DiscAttribute.Song.NotASingle("Il dito medio di Galileo", "del testo a caso"),
        "song;Il dito medio di Galileo;del testo a caso;1;2021-08-20" to DiscAttribute.Song.Single("Il dito medio di Galileo", "del testo a caso", LocalDate.of(2021 , 8, 20))
    )
    .map { (inputStr, expectedResult) ->
        dynamicTest("given $inputStr i expect $expectedResult") {
            val result = CsvReader.read(inputStr)

            result shouldBe expectedResult
    } }

}

object CsvReader{
    fun read(inputStr: String): DiscAttribute? {
        val elements: List<String> = inputStr.split(";")
        return DiscAttribute.of(elements.first(), elements.drop(1))
    }

}

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
