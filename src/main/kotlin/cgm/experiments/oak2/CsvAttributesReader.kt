package cgm.experiments.oak2

object CsvAttributesReader{
    fun read(inputStr: String): DiscAttribute? {
        val elements: List<String> = inputStr.split(";")
        return DiscAttribute.of(elements.first(), elements.drop(1))
    }
}