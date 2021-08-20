package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CsvReadTests {

    @Test
    internal fun `can read a genre`() {
        val inputStr = "genre;Rap"
        val result = readFromCsv(inputStr)

        DiscAttributes.Genre("Rap") shouldBe result
    }

    @Test
    internal fun `can read a Singer`() {
        val inputStr = "singer;Caparezza"
        val result = readFromCsv(inputStr)

        DiscAttributes.Singer("Caparezza") shouldBe result
    }

    private fun readFromCsv(inputStr: String): DiscAttributes? {
        val elements: List<String> = inputStr.split(";")
        val (type, name) = elements
        return DiscAttributes.of(type, name)
    }


}

sealed class DiscAttributes{
    data class Singer(val stageName: String): DiscAttributes()
    data class Genre(val name: String): DiscAttributes()

    companion object {
        fun of(type: String, name: String) = when (type) {
            "genre" -> Genre(name)
            "singer" -> Singer(name)
            else -> null
        }
    }
}

