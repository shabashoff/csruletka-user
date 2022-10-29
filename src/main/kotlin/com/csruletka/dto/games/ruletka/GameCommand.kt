package com.csruletka.dto.games.ruletka

data class GameCommand<T>(
    val command: String,
    val data: T
)