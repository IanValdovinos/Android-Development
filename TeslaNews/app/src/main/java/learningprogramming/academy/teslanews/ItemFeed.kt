package learningprogramming.academy.teslanews

class ItemFeed(){

    var title: String =""
    var description: String =""
    var newsLink: String = ""
    var publicationDate: String = ""

    override fun toString(): String {
        return """
            title = $title
            description = $description
            news link = $newsLink
            publication date = $publicationDate
        """.trimIndent()
    }
}