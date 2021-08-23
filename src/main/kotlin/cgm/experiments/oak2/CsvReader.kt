package cgm.experiments.oak2

object CsvReader {
    fun read(csvInput: String): List<Disc> = csvInput
        .split("\n")
        .map { line -> line.split(";").run { first() to drop(1) } }
        .let(DiskBuilder::buildDisc)
}