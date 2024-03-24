package org.example.Model

import kotlinx.serialization.Serializable
//Getting apis to all months
@Serializable
data class ChessComArchives(
    val archives : List<String>
)

//Chess game api

@Serializable
data class AllGames(
    val games : List<Game>
)

@Serializable
data class Game(
    val url : String,
    val pgn : String,
    val time_class : String,
    val initial_setup : String,
    val fen : String,
    val white : White,
    val black : Black,
    val accuracies : Accuracy? = null
)

@Serializable
data class Accuracy(
    val white : Double,
    val black : Double
)

@Serializable
data class White(
    val rating : Int,
    val result : String,
    val username : String
)

@Serializable
data class Black(
    val rating : Int,
    val result : String,
    val username : String
)
