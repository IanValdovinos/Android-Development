package samuelvaldovinos.example.nasanews

class NasaNew {
    var title = ""
    var publicationDate = ""
    var description = ""
    var link = ""

    override fun toString(): String {
        return """
            Title = $title
            Publication Date = $publicationDate
            Description = $description
            Link = $link
        """.trimIndent()
    }
}