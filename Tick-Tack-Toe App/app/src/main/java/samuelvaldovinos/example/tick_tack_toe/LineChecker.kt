package samuelvaldovinos.example.tick_tack_toe

import android.content.Context
import android.util.Log

import android.widget.Toast


object LineChecker {
    private val TAG = "LineChecker"

    //These are all the possible ways to win by completing a row
    private val row1 = listOf<List<Boolean>>(
        listOf(true, true, true),
        listOf(false, false, false),
        listOf(false, false, false)
    )
    private val row2 = listOf<List<Boolean>>(
        listOf(false, false, false),
        listOf(true, true, true),
        listOf(false, false, false)
    )
    private val row3 = listOf<List<Boolean>>(
        listOf(false, false, false),
        listOf(false, false, false),
        listOf(true, true, true)
    )

    // This are all the possible ways to win by completing a column
    private val column1 = listOf<List<Boolean>>(
        listOf(true, false, false),
        listOf(true, false, false),
        listOf(true, false, false)
    )
    private val column2 = listOf<List<Boolean>>(
        listOf(false, true, false),
        listOf(false, true, false),
        listOf(false, true, false)
    )
    private val column3 = listOf<List<Boolean>>(
        listOf(false, false, true),
        listOf(false, false, true),
        listOf(false, false, true)
    )

    //These are all the possible ways to win by completing a diagonal line
    private val diagonal1 = listOf<List<Boolean>>(
        listOf(false, false, true),
        listOf(false, true, false),
        listOf(true, false, false)
    )
    private val diagonal2 = listOf<List<Boolean>>(
        listOf(true, false, false),
        listOf(false, true, false),
        listOf(false, false, true)
    )

    //This is an array of all the possible way to win
    private val possibleWins =
        listOf(row1, row2, row3, column1, column2, column3, diagonal1, diagonal2)

    private var missingSpot: MutableList<Int>? = mutableListOf<Int>()
    var missingSpotList: MutableList<MutableList<Int>> = mutableListOf()

    //This function checks all the possible ways to win against the provided matrix
    fun checkForLine(
        context: Context,
        matrix: MutableList<MutableList<Boolean>>
    ): List<List<Boolean>>? {
        var numberOfMatches =
            0 //This variable stores the how many cells match between the winning matrix and the provided matrix and returns the winning matrix in case there is one
        possibleWins.forEach { possibleWin ->
            for (row in 0..2) {
                for (column in 0..2) {
                    if (possibleWin[row][column] == matrix[row][column] && matrix[row][column]) {
                        numberOfMatches++
                    }
                }
            }

            //If we have 3 matches, it means that the provided matrix has a line of three marks
            if (numberOfMatches == 3) {
                val myToast = Toast.makeText(context, "Game Won", Toast.LENGTH_SHORT)
                myToast.show()
                Log.d(TAG, "game won with $possibleWin")
                return possibleWin //It returns the way in which the provided matrix won
            } else if (numberOfMatches == 2) { //If there were 2 matches, the provided matrix is close to win
                missingSpot = findMissingSpot(
                    matrix,
                    possibleWin
                ) //We store the missing cell to form a line into a variable

                missingSpotList.add(missingSpot!!)

            }
            numberOfMatches = 0 //This resets the numberOfMatches variable for the next loop
        }
        return null //If there as not a line found, return null
    }

    //This function return the missing cell to complete a line
    private fun findMissingSpot(
        playerMatrix: List<List<Boolean>>,
        winningMatrix: List<List<Boolean>>
    ): MutableList<Int>? {
        val missingMarkIndex = mutableListOf<Int>()
        for (row in 0..2) {
            for (column in 0..2) {
                if (!playerMatrix[row][column] && winningMatrix[row][column]) {
                    missingMarkIndex.add(row)
                    missingMarkIndex.add(column)
                    return missingMarkIndex
                }
            }
        }
        return null
    }

}