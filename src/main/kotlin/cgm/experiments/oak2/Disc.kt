package cgm.experiments.oak2

import java.time.LocalDate

data class Disc(
    val title: String,
    val releaseDate: LocalDate,
    val manager: DiscAttribute.Manager,
    val genre: DiscAttribute.Genre,
    val singer: DiscAttribute.Singer,
    val songs: List<DiscAttribute.Song>
    )