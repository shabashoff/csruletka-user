package com.csruletka.dto.user

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "users")
class User {
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Column(name = "steam_info", nullable = true)
    var steamInfo: String? = null


    //private val id: Long? = null
    //private val
}
