package tictactoe

import kotlin.math.abs

fun main() {
    // Empty tic-tac-toe grid
    var gameState = "_________"

    printGame(gameState)
    var coords: String
    var player = 'X'

    while (evaluateGame(gameState) == "Game not finished") {
        do {
            print("Please insert the coordinates for your move in the format \"row, column\": ")
            coords = readln()
        } while (!validInput(coords, gameState))

        val coordinates = getCoordinatePair(coords)
        gameState = updateState(coordinates, player, gameState)
        player = if (player == 'X') 'O' else 'X'

        printGame(gameState)
    }

    println(evaluateGame(gameState))
}

/**
 * Given a list of coordinates, a player character (X or O) and a gameState, updates and returns the new gameState.
 */
fun updateState(coords: List<Int>, player: Char, gameState: String): String {
    val stringPosition = (coords[0]-1) * 3 + coords[1]-1
    val charArray = gameState.toCharArray()
    charArray[stringPosition] = player

    return String(charArray)
}

/**
 * Given a coordinate string in the format "x y" and a game state string, returns true if the input is valid, according
 * to the gameState.
 */
fun validInput(coordinates: String, gameState: String): Boolean {
    if (!coordinates.matches("[0-9] [0-9]".toRegex())) {
        println("You should enter numbers!")
        return false
    }

    val coordinatePair = getCoordinatePair(coordinates)
    if (coordinatePair.any { it !in 1..3 }) {
        println("Coordinates should be from 1 to 3!")
        return false
    }

    val stringPosition = (coordinatePair[0]-1) * 3 + coordinatePair[1]-1
    if (gameState[stringPosition] !in arrayOf('_', ' ')) {
        println("This cell is occupied! Choose another one!")
        return false
    }

    return true
}

/**
 * Given a string that looks like "x y" returns a List containing x and y as integers
 */
fun getCoordinatePair(coordinates: String): List<Int> {
    return coordinates.split(' ').map { it.toInt() }
}

/**
 * Given a game string, evaluates the current state, returning a string with the result
 */
fun evaluateGame(gameState: String): String {
    val leftDiagonalRange = 0..8 step 4
    val rightDiagonalRange = 2..6 step 2
    val getRow = { row: Int, game: String -> game.filterIndexed { index, _ -> index in row * 3..row * 3 + 2 } }
    val getColumn = { col: Int, game: String -> game.filterIndexed { index, _ -> index in col..8 step 3 } }
    val getLeftDiagonal = { game: String -> game.filterIndexed { index, _ -> index in leftDiagonalRange } }
    val getRightDiagonal = { game: String -> game.filterIndexed { index, _ -> index in rightDiagonalRange } }
    // represents the line and columns scanned
    val lineColumnRange = 0..2
    var totalXs = 0
    var totalOs = 0
    var xWins = 0
    var oWins = 0

    for (index in lineColumnRange) {
        val row = getRow(index, gameState)
        val column = getColumn(index, gameState)
        val xInRow = row.count { it == 'X' }
        val xInColumn = column.count { it == 'X' }
        val oInRow = row.count { it == 'O' }
        val oInColumn = column.count { it == 'O' }
        totalXs += xInRow
        totalOs += oInRow
        when {
            xInRow == 3 -> xWins++
            xInColumn == 3 -> xWins++
            oInRow == 3 -> oWins++
            oInColumn == 3 -> oWins++
        }
    }
    val leftDiagonal = getLeftDiagonal(gameState)
    val rightDiagonal = getRightDiagonal(gameState)
    val xInLeftDiagonal = leftDiagonal.count { it == 'X' }
    val xInRightDiagonal = rightDiagonal.count { it == 'X' }
    val oInLeftDiagonal = leftDiagonal.count { it == 'O' }
    val oInRightDiagonal = rightDiagonal.count { it == 'O' }
    when {
        xInLeftDiagonal == 3 -> xWins++
        xInRightDiagonal == 3 -> xWins++
        oInLeftDiagonal == 3 -> oWins++
        oInRightDiagonal == 3 -> oWins++
    }

    val result = when {
        xWins > 1 || oWins > 1 -> "Impossible"
        xWins == 1 && oWins == 1 -> "Impossible"
        abs(totalXs - totalOs) > 1 -> "Impossible"
        xWins == 1 -> "X wins"
        oWins == 1 -> "O wins"
        totalOs + totalXs == 9 -> "Draw"
        else -> "Game not finished"
    }

    return result
}

fun printGame(game: String) {
    println("---------")
    for (line in game.chunked(3)) {
        print("| ${line[0]} ${line[1]} ${line[2]} |\n")
    }
    println("---------")
}
