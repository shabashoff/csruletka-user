package com.csruletka.repository

import com.csruletka.dto.games.ruletka.RuletkaHistory
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.ReactorCrudRepository

@Repository
interface RuletkaHistoryRepository : ReactorCrudRepository<RuletkaHistory, Long> {
}