package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class CsvReadTests {

    @TestFactory
    internal fun `can read disc attributes from a csv`() = listOf(
        "genre;Rap" to DiscAttributes.Genre("Rap"),
        "singer;Caparezza" to DiscAttributes.Singer("Caparezza"),
    )
    .map { (inputStr, expectedResult) ->
        dynamicTest("given $inputStr i expect $expectedResult") {
            val result = CsvReader.read(inputStr)

            expectedResult shouldBe result
    } }

}

object CsvReader{
    fun read(inputStr: String): DiscAttributes? {
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

