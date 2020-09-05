package appdevelopment.samuel.cardguessingapp

import android.util.Log

class Deck(private val listOfCards: List<Card>) {
    var cards = listOfCards

    fun shuffle() {
        cards = cards.shuffled()
    }

    fun printDeck(){
        cards.forEach{
            Log.d(TAG, it.getName())
        }
    }

    fun organizeDeck(){
        cards = listOfCards
    }

    fun getCard() = cards.first()
}