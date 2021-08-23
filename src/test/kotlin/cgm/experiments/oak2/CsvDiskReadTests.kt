package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CsvDiskReadTests {

    @Test
    internal fun `can read a csv with only one disk`() {
        val csvInput = """
            manager;Dario Pianta;dario.pianta@cgm.com
            genre;Rap
            singer;Caparezza
            song;titolo1;testo1
            song;titolo2;testo2;1;2011-10-21
            disc;Il sogno eretico;2011-03-01
        """.trimIndent()

        CsvReader.read(csvInput) shouldBe listOf(Disc(
            title = "Il sogno eretico",
            releaseDate = LocalDate.of(2011, 3, 1),
            manager = DiscAttribute.Manager("Dario Pianta", "dario.pianta@cgm.com"),
            genre = DiscAttribute.Genre("Rap"),
            singer = DiscAttribute.Singer("Caparezza"),
            songs = listOf(
                DiscAttribute.Song.NotASingle("titolo1", "testo1"),
                DiscAttribute.Song.Single("titolo2", "testo2", LocalDate.of(2011, 10, 21)))
        ))
    }

    @Test
    internal fun `can read a csv with more than one disk`() {
        val csvInput = """
            manager;Dario Pianta;dario.pianta@cgm.com
            genre;Rap
            singer;Caparezza
            song;titolo1;testo1
            song;titolo2;testo2;1;2011-10-21
            disc;Il sogno eretico;2011-03-01
            genre;Rap
            singer;Caparezza
            song;titolo1;testo1
            disc;Il sogno eretico2;2011-03-01
        """.trimIndent()

        CsvReader.read(csvInput) shouldBe listOf(
            Disc(
                title = "Il sogno eretico",
                releaseDate = LocalDate.of(2011, 3, 1),
                manager = DiscAttribute.Manager("Dario Pianta", "dario.pianta@cgm.com"),
                genre = DiscAttribute.Genre("Rap"),
                singer = DiscAttribute.Singer("Caparezza"),
                songs = listOf(
                    DiscAttribute.Song.NotASingle("titolo1", "testo1"),
                    DiscAttribute.Song.Single("titolo2", "testo2", LocalDate.of(2011, 10, 21)))
            ),
            Disc(
                title = "Il sogno eretico2",
                releaseDate = LocalDate.of(2011, 3, 1),
                manager = DiscAttribute.Manager("Dario Pianta", "dario.pianta@cgm.com"),
                genre = DiscAttribute.Genre("Rap"),
                singer = DiscAttribute.Singer("Caparezza"),
                songs = listOf(
                    DiscAttribute.Song.NotASingle("titolo1", "testo1"))
            ),
        )
    }
}
