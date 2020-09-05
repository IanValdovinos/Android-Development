package appdevelopment.samuel.cardguessingapp

object Hint {
    fun firstHint(correctCard: Card): String {
        return when (correctCard.suit.toLowerCase()) {
            "spades" -> "The card's suit is Spades"
            "clubs" -> "The card's suit is Clubs"
            "hearts" -> "The card's suit is Hearts"
            "diamonds" -> "The card's suit is Diamonds"
            else -> "An error occurred"
        }
    }

    fun secondHint(correctCard: Card): String {
        return when (correctCard.getNumericValue()) {
            in 1..5 -> "The card is between Ace and 5"
            in 6..10 -> "The card is between 6 and 10"
            in 10..13 -> "The card is between Jack and King"
            else -> "An error occurred"
        }
    }
}