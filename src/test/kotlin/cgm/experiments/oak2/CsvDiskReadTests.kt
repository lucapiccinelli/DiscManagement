package cgm.experiments.oak2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CsvDiskReadTests {

    @Test
    internal fun name() {
        val csvInput = """
            manager;Dario Pianta;dario.pianta@cgm.com
            genre;Rap
            singer;Caparezza
            song;titolo1;testo1
            song;titolo2;testo2;1;2011-10-21
            disc;Il sogno eretico;2011-03-01
        """.trimIndent()

        CsvReader.read(csvInput) shouldBe Disk(
            title = "Il sogno eretico",
            releaseDate = LocalDate.of(2011, 3, 1),
            manager = DiscAttribute.Manager("Dario Pianta", "dario.pianta@cgm.com"),
            genre = DiscAttribute.Genre("Rap"),
            singer = DiscAttribute.Singer("Caparezza"),
            songs = listOf(
                DiscAttribute.Song.NotASingle("titolo1", "testo1"),
                DiscAttribute.Song.Single("titolo2", "testo2", LocalDate.of(2011, 10, 21))))
    }
}

class DiskBuilder {
    fun build(title: String, releaseDate: LocalDate) =
        Disk(title, releaseDate, manager, genre, singer, songs)

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
    }
}

data class Disk(
    val title: String,
    val releaseDate: LocalDate,
    val manager: DiscAttribute.Manager,
    val genre: DiscAttribute.Genre,
    val singer: DiscAttribute.Singer,
    val songs: List<DiscAttribute.Song>
    )

object CsvReader {
    fun read(csvInput: String) = csvInput
        .split("\n")
        .let { lines ->
            val diskBuilder: DiskBuilder = lines
                .dropLast(1)
                .mapNotNull(CsvAttributesReader::read)
                .let(DiskBuilder::ofAttributes)

            val (title, dateStr) = lines.last().split(";").drop(1)
            diskBuilder.build(title, LocalDate.parse(dateStr))
        }

}
