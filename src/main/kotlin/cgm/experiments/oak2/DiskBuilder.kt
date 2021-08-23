package cgm.experiments.oak2

import java.time.LocalDate

class DiskBuilder {
    fun build(title: String, releaseDate: LocalDate) =
        Disc(title, releaseDate, manager, genre, singer, songs)

    lateinit var manager: DiscAttribute.Manager
    lateinit var genre: DiscAttribute.Genre
    lateinit var singer: DiscAttribute.Singer
    val songs: MutableList<DiscAttribute.Song> = mutableListOf()

    companion object {
        fun buildDisc(lines: List<Pair<String, List<String>>>): List<Disc> {
            val attributes = mutableListOf<DiscAttribute?>()
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

        private fun ofAttributes(attributes: List<DiscAttribute>) = DiskBuilder().apply {
            attributes.map {
                when (it) {
                    is DiscAttribute.Genre -> genre = it
                    is DiscAttribute.Manager -> manager = it
                    is DiscAttribute.Singer -> singer = it
                    is DiscAttribute.Song -> songs.add(it)
                }
            }
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