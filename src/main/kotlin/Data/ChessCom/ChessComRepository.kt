package org.example.Data.ChessCom

import org.example.Model.AllGames
import org.example.Model.ChessComArchives
import org.example.Model.Game

class ChessComRepository {
    private val chessDataSource = ChessComDataSource()
    //får lenker til nye api-er,
    // som er alle kamper i alle måneder du har spilt
    suspend fun getChessMonthlyGamesList(username: String): ChessComArchives? {
        return chessDataSource.getChessMonthlyGamesList(username)
    }

    suspend fun getChessGamesForMonth(username: String, allMonths: List<String>, numberOfMonths: Int = 1): List<AllGames?>{
        return chessDataSource.getChessGamesForMonth(username, allMonths, numberOfMonths)
    }

    suspend fun getBlackAndWhite(username : String, result : String, numberOfMonths: Int = 1){
        val monthlyList = getChessMonthlyGamesList(username)
        val monthlyGames = getChessGamesForMonth(username, monthlyList!!.archives, numberOfMonths)

        val accuracyWhite = getAverageAccuracyForWhite(result, username, monthlyGames)
        val accuracyBlack = getAverageAccuracyForBlack(result, username, monthlyGames)
        println("White: $accuracyWhite")
        println("Black: $accuracyBlack")

    }
    fun getGamesWithParams(color : String, result : String, username : String, listOfAllGames : List<AllGames?>) : MutableList<List<Game>?>{
        val listOfFilteredGames : MutableList<List<Game>?> = mutableListOf()
        for(item in listOfAllGames){
            if(color == "white"){//denne if checken vil ikke legge til så mye ekstra tid siden det vil aldri være altfor mange elementer
                listOfFilteredGames.add(item?.games?.filter{it.white.username == username && it.white.result == result && it.accuracies != null})
            }
            else{
                listOfFilteredGames.add(item?.games?.filter{it.black.username == username && it.black.result == result && it.accuracies != null})
            }

        }

        return listOfFilteredGames
    }

    fun getAverageAccuracyForWhite(result : String, username : String, listOfAllGames : List<AllGames?>) : Double{
        val listOfGames = getGamesWithParams("white", result, username, listOfAllGames)
        var accuracy = 0.0
        var numberOfGames = 0
        for(item in listOfGames){
            for(game in item!!){
                accuracy += game.accuracies!!.white
                numberOfGames++
            }
        }
        return accuracy / numberOfGames
    }


    fun getAverageAccuracyForBlack( result : String, username : String, listOfAllGames : List<AllGames?>) : Double{
        val listOfGames = getGamesWithParams("black", result, username, listOfAllGames)
        var accuracy = 0.0
        var numberOfGames = 0
        for(item in listOfGames){
            for(game in item!!){
                accuracy += game.accuracies!!.black
                numberOfGames++
            }
        }
        return accuracy / numberOfGames
    }

    suspend fun getGamesShouldHaveNotLostWhite(username : String, numberOfMonths: Int = 1){
        val listOfFilteredGames : MutableList<List<Game>?> = mutableListOf()
        val monthlyList = getChessMonthlyGamesList(username)
        val monthlyGames = getChessGamesForMonth(username, monthlyList!!.archives, numberOfMonths)
        val listOfWrongGames : MutableList<String> = mutableListOf()
        for(item in monthlyGames){
            listOfFilteredGames.add(item?.games?.filter{
                it.white.username == username && (it.white.result == "resigned" || it.white.result == "checkmated") && it.accuracies != null && it.accuracies.white > it.accuracies.black})
            }
        for(item in listOfFilteredGames){
            println(item)
        }


        }




    }

suspend fun main(){
    val a = ChessComRepository()
    a.getGamesShouldHaveNotLostWhite("OlaSkeie", 3)
}
