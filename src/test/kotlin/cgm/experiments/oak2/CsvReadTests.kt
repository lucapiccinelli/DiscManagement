package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CsvReadTests {

    @Test
    internal fun name() {
        val inputStr = "genre;Rap"
        val result = readGenre(inputStr)

        Genre("Rap") shouldBe result
    }

    private fun readGenre(inputStr: String): Genre {
        val name = inputStr
            .split(";")
            .last()
        return Genre(name)
    }

}

data class Genre(val name: String)
