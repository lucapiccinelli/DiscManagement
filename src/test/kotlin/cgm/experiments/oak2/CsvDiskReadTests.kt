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

class DiskBuilder {
    fun build(title: String, releaseDate: LocalDate) =
        Disc(title, releaseDate, manager, genre, singer, songs)

    lateinit var manager: DiscAttribute.Manager
    lateinit var genre: DiscAttribute.Genre
    lateinit var singer: DiscAttribute.Singer
    val songs: MutableList<DiscAttribute.Song> = mutableListOf()

    companion object {
        fun ofAttributes(attributes: List<DiscAttribute>) = DiskBuilder().apply {
            attributes.map {
                when (it) {
                    is DiscAttribute.Genre -> genre = it
                    is DiscAttribute.Manager -> manager = it
                    is DiscAttribute.Singer -> singer = it
                    is DiscAttribute.Song -> songs.add(it)
                }
            }
        }

        fun buildDisc(lines: List<Pair<String, List<String>>>): List<Disc> {
            var attributes = mutableListOf<DiscAttribute?>()
            val discs = mutableListOf<Disc>()

            lines.forEach { (type, data) ->
                if (type != "disc") {
                    attributes.add(DiscAttribute.of(type, data))
                } else {
                    discs.add(createDisk(attributes, data))
                    attributes.removeIf { it !is DiscAttribute.Manager }
                }
            }
            return discs
        }

        private fun createDisk(attributes: MutableList<DiscAttribute?>, discdata: List<String>): Disc {
            val diskBuilder: DiskBuilder = attributes
                .filterNotNull()
                .let(DiskBuilder::ofAttributes)

            val (title, dateStr) = discdata
            return diskBuilder.build(title, LocalDate.parse(dateStr))
        }
    }
}

data class Disc(
    val title: String,
    val releaseDate: LocalDate,
    val manager: DiscAttribute.Manager,
    val genre: DiscAttribute.Genre,
    val singer: DiscAttribute.Singer,
    val songs: List<DiscAttribute.Song>
    )

object CsvReader {
    fun read(csvInput: String): List<Disc> = csvInput
        .split("\n")
        .map { line -> line.split(";").run { first() to drop(1) } }
        .let(DiskBuilder::buildDisc)
}
