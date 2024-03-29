package com.csruletka.dto.games.ruletka

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import javax.persistence.*


@Entity(name = "ruletka_history")
@TypeDef(name = "json", typeClass = JsonType::class)
class RuletkaHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "winner_ticket", nullable = false)
    var winnerTicket: Int? = null

    @Type(type = "json")
    @Column(name = "winner", nullable = false)
    var winner: SkinsInGame? = null


    @Type(type = "json")
    @Column(name = "users", nullable = false)
    var users: List<SkinsInGame>? = null

    @Column(name = "created_date_time", nullable = false)
    var createdDateTime: LocalDateTime? = null
}