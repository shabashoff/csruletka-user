package com.csruletka.dto.user

import com.csruletka.dto.task.TaskInProgress
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
    var inventory: MutableList<UserInventoryItem> = arrayListOf()

    @Type(type = "json")
    @Column(name = "task", nullable = true)
    var tasks: MutableList<TaskInProgress> = arrayListOf()

    fun removeSkins(ids: List<String>): List<UserInventoryItem> {
        val inventoryMap = inventory.associateBy { it.id }.toMutableMap()
        val skinsToSend = arrayListOf<UserInventoryItem>()

        ids.forEach {
            if (inventoryMap.contains(it)) {
                skinsToSend.add(inventoryMap.remove(it)!!)
            } else {
                throw IllegalArgumentException("Cannot find item in user skins")
            }
        }

        inventory = inventoryMap.values.toMutableList()

        return skinsToSend
    }
}
