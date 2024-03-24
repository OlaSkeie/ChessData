package org.example.Data.ChessCom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.example.Model.AllGames
import org.example.Model.ChessComArchives

class ChessComDataSource {
    private val client = HttpClient(CIO) {
        defaultRequest {
            url("https://api.chess.com/pub/player/")
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true

            })
        }
    }

    //får lenker til nye api-er,
    // som er alle kamper i alle måneder du har spilt
    suspend fun getChessMonthlyGamesList(username: String): ChessComArchives? {
        return try {
            client.get("https://api.chess.com/pub/player/$username/games/archives")
                .body()
        } catch (e: Exception) {
            println("Error during HTTP request for ChessCom: $e")
            return null
        }

    }

    suspend fun getChessGamesForMonth(username: String, allMonths: List<String>, numberOfMonths: Int = 1): List<AllGames?> {
        var months = numberOfMonths

        if(numberOfMonths > allMonths.size){
            months = allMonths.size - 1
        }


        val listOfAllGames: MutableList<AllGames> = mutableListOf()
        var counter = 0
        //Vil starte på det siste elementet siden det er nyeste måneden
        var indexCheck = allMonths.size - 1
        while (counter < months) {
            try {
                listOfAllGames.add(
                    client.get(allMonths[indexCheck])
                        .body()
                )
                counter++
                indexCheck--

            } catch (e: Exception) {
                println("Error during HTTP request for ChessCom: $e")
            }

        }
        return listOfAllGames
    }
}
