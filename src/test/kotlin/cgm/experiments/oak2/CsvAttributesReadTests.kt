package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate

class CsvAttributesReadTests {
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
            val result = CsvAttributesReader.read(inputStr)

            result shouldBe expectedResult
    } }
}
