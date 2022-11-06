package com.csruletka.dto.user

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "users")
@TypeDef(name = "json", typeClass = JsonType::class)
class User {
    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null

    @Type(type = "json")
    @Column(name = "steam_info", nullable = true)
    var steamInfo: SteamUserInfo? = null

    @Type(type = "json")
    @Column(name = "inventory", nullable = true)
    var inventory: MutableList<UserInventoryItem>? = null
}
