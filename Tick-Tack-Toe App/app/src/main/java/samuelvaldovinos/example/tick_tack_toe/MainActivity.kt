package samuelvaldovinos.example.tick_tack_toe

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //These are the different tokens that the player(s) can play with
    private var player1Mark = "X"
    private var player2Mark = "O"

    // This variable keeps track if there is a two player game in progress
    private var twoPlayerMode = false

    //This variable keeps track of who's turn it is in a two player mode
    var playerTurn = "player 1"

    //This variable keeps track of how many spots have been filled in the matrix
    private var numOfSpotsMarked = 0

    //This is a matrix of the button pad. aka game board
    private val buttonPad by lazy {
        listOf<List<Button>>(
            listOf(button1, button2, button3),
            listOf(button4, button5, button6),
            listOf(button7, button8, button9)
        )
    }

    //These two matrices keep track of where the 2 players have placed their tokens
    private var player1matrix = mutableListOf<MutableList<Boolean>>(
        mutableListOf<Boolean>(false, false, false),
        mutableListOf<Boolean>(false, false, false),
        mutableListOf<Boolean>(false, false, false)
    )
    private val player2matrix = mutableListOf<MutableList<Boolean>>(
        mutableListOf<Boolean>(false, false, false),
        mutableListOf<Boolean>(false, false, false),
        mutableListOf<Boolean>(false, false, false)
    )

    //These two variables keep track of the game state. They can disable buttons and show toasts depending on their state
    private var gameInProgress = false
    private var gameOver = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //We set all the buttons in the buttonPad array on clinkListener
        buttonPad.forEach { buttonRow ->
            buttonRow.forEach { button ->
                button.setOnClickListener {
                    Log.d(TAG, "$it as been selected")
                    val myButton = it as Button

                    //If the game is not over, proceed.
                    if (myButton.text.isEmpty() && !gameOver) {
                        when(playerTurn) {
                            "player 1" -> {
                                markButton(myButton, player1Mark) // We mark the button that the player has chosen
                                numOfSpotsMarked++
                            }
                            "player 2" -> {
                                markButton(myButton, player2Mark) // We mark the button that the player has chosen
                                numOfSpotsMarked++
                            }
                        }


                        //We store the coordinates of the button
                        val buttonColumnIndex = buttonRow.indexOf(myButton)
                        val buttonRowIndex = buttonPad.indexOf(buttonRow)

                        //In this line we set the corresponding player's matrix cell to true, meaning that they have chosen that cell
                        when (playerTurn) {
                            "player 1" -> {
                                player1matrix[buttonRowIndex][buttonColumnIndex] = true
                                val winningMatrix = LineChecker.checkForLine(this, player1matrix)
                                winningMatrix?.let { matrix ->
                                    //If the player has formed a line, highlight the buttons and set the gameOver variable to true
                                    highlightWiningMarks(matrix)
                                    gameOver = true
                                }
                            }
                            "player 2" -> {
                                player2matrix[buttonRowIndex][buttonColumnIndex] = true
                                val winningMatrix = LineChecker.checkForLine(this, player2matrix)
                                winningMatrix?.let { matrix ->
                                    //If the player has formed a line, highlight the buttons and set the gameOver variable to true
                                    highlightWiningMarks(matrix)
                                    gameOver = true
                                }
                            }
                        }

                        //If the game is NOT over and the board is NOT full, execute a computer selection.
                        if (numOfSpotsMarked != 9 && !gameOver) {
                            when (twoPlayerMode) {
                                false -> computerSelection()
                                true -> {
                                    playerTurn = when (playerTurn) {
                                        "player 1" -> "player 2"
                                        "player 2" -> "player 1"
                                        else -> ""
                                    }
                                }
                            }

                        }

                    } else {
                        //This else block shows a toast message depending on what is stopping the user from selecting a cell
                        val message = if (gameOver) "Game Over" else "This spot is already selected"
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        //This is the clear button. It resets the game clearing all the variables and texts
        clear_button.setOnClickListener {
            playerTurn = "player 1"
            LineChecker.missingSpotList.clear()
            gameOver = false
            numOfSpotsMarked = 0
            gameInProgress = false
            buttonPad.forEach { buttonRow ->
                buttonRow.forEach {
                    it.text = ""
                }
            }

            clearMatrix(player1matrix)
            clearMatrix(player2matrix)
            buttonPad.forEach { row ->
                row.forEach { button ->
                    button.setTextColor(getColor(R.color.markDefaultColor))
                }
            }
        }
    }

    //This function goes over the winning matrix and changes the button's text color that match with the winning matrix
    @RequiresApi(Build.VERSION_CODES.M)
    private fun highlightWiningMarks(winningMarks: List<List<Boolean>>) {
        for (row in 0..2) {
            for (column in 0..2) {
                if (winningMarks[row][column]) {
                    buttonPad[row][column].setTextColor(getColor(R.color.winningMarkColor))
                }
            }
        }
    }

    //This matrix sets all the values of the matrix provided to false
    private fun clearMatrix(matrix: MutableList<MutableList<Boolean>>) {
        for (row in 0..2) {
            for (column in 0..2) {
                matrix[row][column] = false
            }
        }
    }

    //This function marks the provided button with the provided token
    private fun markButton(button: Button, mark: String) {
        gameInProgress = true
        button.text = mark
    }

    //This function is the most complex of all, it selects a cell depending on the other player's placed tokens
    @RequiresApi(Build.VERSION_CODES.M)
    private fun computerSelection() {
        var computerButton: Button = button1
        val possibleWin =
            filterPossibleWins() // This line stores a single possible win to the possibleWin value

        //If possible win is not null:
        if (possibleWin != null) {
            val (row, column) = possibleWin //This line stores the coordinates of the possibleWin value to the row and column values
            Log.d(TAG, "There's a winning chance in spot $possibleWin")
            player2matrix[row][column] =
                true //This sets a player2matrix coordinate that matches with the row and column values to true
            computerButton =
                buttonPad[row][column] // This line set the computerButton to a button in the buttonPad that matches with the row and column coordinates

            //These lines check if a line was formed. If so, the line created will be highlighted in green
            val winningMatrix = LineChecker.checkForLine(this, player2matrix)
            winningMatrix?.let { matrix ->
                highlightWiningMarks(matrix)
                gameOver = true // This line ends the game
            }

        } else {
            //If there is not a winning chance, this code block will be executed
            Log.d(TAG, "There's no winning chance")
            //This loop will run until a random empty cell in the buttonPad is found to
            //place a mark
            while (true) {
                val randomRow = (0..2).random()
                val randomColumn = (0..2).random()

                val randomButton = buttonPad[randomRow][randomColumn]
                if (randomButton.text.isEmpty()) {
                    computerButton = randomButton
                    player2matrix[randomRow][randomColumn] = true

                    val winningMatrix = LineChecker.checkForLine(this, player2matrix)
                    winningMatrix?.let { matrix ->
                        highlightWiningMarks(matrix)
                        gameOver = true
                    }
                    break
                }
            }
        }

        // Regardless of how the cell was chosen (by analysing the best option or randomized) the
        // mark function will mark the button provided with the given mark
        markButton(computerButton, player2Mark)
        numOfSpotsMarked++
    }

    // This function goes through the missingSpotList in the LineChecker class and
    // return a missing spot that has not be chosen in the game board. Returns null if
    // the list is empty or if the spots have been already chosen.
    private fun filterPossibleWins(): List<Int>? {
        var listOfPoints = LineChecker.missingSpotList
        listOfPoints.forEach { possibleSlot ->
            if (buttonPad[possibleSlot[0]][possibleSlot[1]].text.isEmpty()) {
                return possibleSlot
            }
        }
        return null
    }


    // This function creates an options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mark_menu, menu)
        return true
    }

    // This function determines what to do depending on the option selected from the options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // If there is a game in progress, none of these actions will be executed and
        // a toast message will be thrown
        if (!gameInProgress) {
            item.isChecked = true

            when (item.itemId) {
                R.id.mnu_X -> {
                    player1Mark = "X"
                    player2Mark = "O"
                    twoPlayerMode = false
                }
                R.id.mnu_O -> {
                    player1Mark = "O"
                    player2Mark = "X"
                    twoPlayerMode = false
                }

                R.id.mnu_twoPlayer -> {
                    twoPlayerMode = true
                }
            }

        } else {
            val myToast = Toast.makeText(this, "There's a game unfinished", Toast.LENGTH_SHORT)
            myToast.show()
        }
        return true
    }
}