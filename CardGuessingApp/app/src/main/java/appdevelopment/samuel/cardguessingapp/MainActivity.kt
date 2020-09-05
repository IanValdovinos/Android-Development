package appdevelopment.samuel.cardguessingapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception

//Logcat tag for this activity
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // Suits and values are defined
    private val suits = listOf<String>("spades", "clubs", "diamonds", "hearts")
    private val values = listOf<String>(
        "ace",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "ten",
        "jack",
        "queen",
        "king"
    )
    private val listOfCards = mutableListOf<Card>()

    // Life variable
    private var life = 4

    // This variable keeps track if the user won the game or not
    private var gameWon = false

    // Views are being defined
    private lateinit var guessButton: Button
    private lateinit var textView: TextView
    private lateinit var userInput: EditText
    private lateinit var feedbackTextView: TextView
    private lateinit var lifeCounter: TextView
    private lateinit var hintTextView: TextView
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cards are being added to the card list
        suits.forEach { suit ->
            values.forEach { value ->
                listOfCards.add(Card(suit, value))
            }
        }

        // A deck is created
        val deck = Deck(listOfCards)
        deck.shuffle() // Shuffle the deck
        var cardToGuess: Card = deck.getCard() // The card to guess is being selected
        Log.d(TAG, "The correct card is: ${cardToGuess.getName()}") // The correct card is printed to the Logcat

        // Variables and widgets are wired up
        guessButton = findViewById(R.id.guess_button)
        textView = findViewById(R.id.userCard)
        userInput = findViewById(R.id.userInput)
        feedbackTextView = findViewById(R.id.feedbackTextView)
        lifeCounter = findViewById(R.id.lifeCounter)
        hintTextView = findViewById(R.id.hintTextView)
        restartButton = findViewById(R.id.restart_button)


        // Passes the user's card to the "Your Card" field and compares it against the correct card
        guessButton.setOnClickListener {
            if (!isGameOver()) {
                guessCard(cardToGuess)
            }
        }

        //restarts the game
        restartButton.setOnClickListener {
            it.visibility = View.GONE
            life = 4
            gameWon = false
            deck.shuffle()
            cardToGuess = deck.getCard()
            updateLifeCounter()
            textView.text = ""
            feedbackTextView.text = ""
            hintTextView.text = "Hints:"
        }

    }

    //This function check if the game is over or not
    private fun isGameOver(): Boolean {
        if (life > 0 && !gameWon) {
            // The game is NOT over yet
            return false
        } else {
            // The game is OVER
            val gameOverToast = Toast.makeText(this, "The game is over, click restart to play again", Toast.LENGTH_SHORT)
            gameOverToast.show()
            return true
        }
    }

    //This function controls all the calculation to guess a card
    private fun guessCard(cardToGuess: Card) {
        val userCardInput = userInput.text.toString()

        // The "try" block is ran in case a correct input format is inserted
        try {
            textView.text =
                userInput.text // The user input is carried out to the User's Card TextView
            val (userValue, _, userSuit) = userCardInput.toLowerCase()
                .split(" ") // The user input is split to suit and value
            val userCard: Card =
                Card(userSuit, userValue) // A card based on the user input is created
            Log.d(TAG, userCard.getName()) // the user input is printed in the logcat

            //This if statement defines the feedback based on the user's input
            if (isCardCorrect(cardToGuess, userCard)) {
                feedbackTextView.text = "Correct!"
                feedbackTextView.setTextColor(Color.GREEN)
                gameWon = true

                restartButton.visibility = View.VISIBLE
                userInput.text.clear()
            } else {
                feedbackTextView.text = "Incorrect!"
                feedbackTextView.setTextColor(Color.RED)

                life -= 1
                updateLifeCounter()

                if (life <= 0 ) {
                    restartButton.visibility = View.VISIBLE
                    userInput.text.clear()
                }

                val hint = when (life) {
                    3 -> "\n${Hint.firstHint(cardToGuess)}"
                    2 -> "\n${Hint.secondHint(cardToGuess)}"
                    1 -> "\nYou got 1 more attempt!"
                    else -> "\nYou Lost! The correct card was ${cardToGuess.getName()}"
                }
                hintTextView.append(hint)
            }
        } catch (e: Exception) { // This catch block is run in case the format of the user input is incorrect
            val myToast: Toast = Toast.makeText(this, "Incorrect Input", Toast.LENGTH_SHORT)
            myToast.show()
        }
        userInput.setText("") //The user edit text is cleared
    }

    //This function evaluates the user's card compared to the selected card
    private fun isCardCorrect(correctCard: Card, userCard: Card): Boolean {
        return correctCard.suit == userCard.suit && correctCard.value == userCard.value
    }

    //This function updates the life counter depending on the user's answer
    private fun updateLifeCounter() {
        lifeCounter.text = "Life: $life"
    }

}
