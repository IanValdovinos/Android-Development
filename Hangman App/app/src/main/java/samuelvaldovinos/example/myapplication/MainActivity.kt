package samuelvaldovinos.example.myapplication
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

const val WORD_TO_GUESS = "WordToGuess"
const val LETTERS_GUESSED = "LettersGuessed"
const val CORRECT_LETTERS_GUESSED = "CorrectLettersGuessed"
const val GAME_WON = "GameWon"
const val ATTEMPT_COUNT = "AttemptCount"
const val LETTER_COUNT = "LetterCount"
const val WRONG_LETTERS_TEXT_VIEW = "WrongLettersTextView"

class Sounds(context: Context) {
    val cheeringSound by lazy {MediaPlayer.create(context, R.raw.audience_cheering)}
    val audienceBooing by lazy { MediaPlayer.create(context, R.raw.boo) }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val wordList = listOf<String>(
            "earth",
            "space",
            "technology",
            "alligator",
            "music",
            "programming",
            "keyboard",
            "learning",
            "android",
            "anonymous",
            "science",
            "books",
            "internet",
            "ironman",
            "phone",
            "airplane",
            "football"
    )
    private var wordToGuess = wordList.random()

//    private var lettersGuessed = mutableListOf<String>()
//    private var correctLetters = mutableListOf<String>()
    private var lettersGuessed = ArrayList<String>()
    private var correctLetters = ArrayList<String>()

    private var attemptCount = 0
    private var letterCount = 0

    private var wordToGuessDashed = ""

    private var gameWon = false

    private var wrongLettersTextView = ""

    private val mySounds = Sounds(this)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val testWord = wordList[2]
//        val dashedTestWord = createDashedWord(testWord, MutableList<String>())
//        Log.d(TAG, ".onCreate: The dashed version of the word $testWord is $dashedTestWord")



        Log.d(TAG, ".onCreate: The correct word is $wordToGuess")
        wordToGuessDashed = createDashedWord(wordToGuess, correctLetters)
        textView_word.text = wordToGuessDashed

        button_guess.setOnClickListener{
            if (attemptCount < 6 && !gameWon) {
                    if(editText.text.isNotEmpty()){
                        val userLetter = editText.text.toString().toLowerCase(Locale.ROOT)
                        editText.text.clear()

                        if(wordToGuess.contains(userLetter)){
                            if(correctLetters.contains(userLetter)){
                                Toast.makeText(this, "Letter already chosen. Correct Letter", Toast.LENGTH_SHORT).show()
                            }else {
                                correctLetters.add(userLetter)
                                textView_word.text = createDashedWord(wordToGuess, correctLetters)

                                letterCount += wordToGuess.count {letter ->
                                    userLetter == letter.toString()
                                }
                                checkWordCompletion(wordToGuess, letterCount)

                                Log.d(TAG, ".onCreate: Number of letter guessed is $letterCount")
                                Log.d(TAG, ".onCreate: Length of the correctLetters array is ${correctLetters.size}")
                            }
                        } else {
                            if(lettersGuessed.contains(userLetter)){
                                Toast.makeText(this, "Letter already chosen. Wrong letter.", Toast.LENGTH_SHORT).show()
                            }else {
                                attemptCount++
                                textView_letters.append("$userLetter ")
                                wrongLettersTextView = textView_letters.text.toString()

                                setHangmanImage()
                            }
                        }
                        lettersGuessed.add(userLetter)
                    } else {
                        Toast.makeText(this, "Guess a letter and type it!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                gameOverMessage()
            }
        }

        button_restart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setHangmanImage() {
        var image: Int = 0

        when (attemptCount) {
            0 -> image = R.drawable.picture1
            1 -> image = R.drawable.picture2
            2 -> image = R.drawable.picture3
            3 -> image = R.drawable.picture4
            4 -> image = R.drawable.picture5
            5 -> image = R.drawable.picture6
            6 -> {
                image = R.drawable.picture7
                gameOverMessage()
            }
            else -> throw IllegalStateException()
        }

        imageContainer.setImageDrawable(getDrawable(image))
    }

    private fun checkWordCompletion(wordToGuess: String, letterNum: Int) {
        if (wordToGuess.length == letterNum){
            gameWon = true
            gameOverMessage()
        }
    }

    private fun gameOverMessage() {
        button_restart.visibility = View.VISIBLE
        Toast.makeText(this, "Game Over! The correct word is $wordToGuess", Toast.LENGTH_LONG)
            .show()
        textView_word.text = wordToGuess
        if(gameWon){
            textView_word.setTextColor(Color.GREEN)
            mySounds.cheeringSound.start()
        } else{
            textView_word.setTextColor(Color.RED)
            mySounds.audienceBooing.start()
        }

    }

    private fun createDashedWord(word: String, except: MutableList<String> = mutableListOf()): String {
        var dashedWord = ""

        for(letterIndex in word.indices){
            val currentLetter = word.get(letterIndex).toString()
            if (currentLetter != " "){
                if (except.contains(currentLetter)){
                    dashedWord += currentLetter
                } else {
                    dashedWord += "_"
                }
            } else {
                dashedWord += " "
            }

            dashedWord += " "
        }
        return dashedWord
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(WORD_TO_GUESS, wordToGuess)

        outState.putStringArrayList(LETTERS_GUESSED, lettersGuessed)
        outState.putStringArrayList(CORRECT_LETTERS_GUESSED, correctLetters)

        outState.putBoolean(GAME_WON, gameWon)

        outState.putInt(ATTEMPT_COUNT, attemptCount)
        outState.putInt(LETTER_COUNT, letterCount)

        outState.putString(WRONG_LETTERS_TEXT_VIEW, wrongLettersTextView)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        wordToGuess = savedInstanceState.getString(WORD_TO_GUESS, "")
        lettersGuessed = savedInstanceState.getStringArrayList(LETTERS_GUESSED) as ArrayList<String>
        correctLetters = savedInstanceState.getStringArrayList(CORRECT_LETTERS_GUESSED) as ArrayList<String>
        gameWon = savedInstanceState.getBoolean(GAME_WON)
        attemptCount = savedInstanceState.getInt(ATTEMPT_COUNT)
        letterCount = savedInstanceState.getInt(LETTER_COUNT)
        wrongLettersTextView = savedInstanceState.getString(WRONG_LETTERS_TEXT_VIEW, "")

        textView_letters.text = wrongLettersTextView
        textView_word.text = createDashedWord(wordToGuess, correctLetters)
        checkWordCompletion(wordToGuess, letterCount)
        setHangmanImage()
    }
}
