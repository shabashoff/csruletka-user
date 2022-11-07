package com.csruletka.dto.task

import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity(name = "task_template")
@TypeDef(name = "json", typeClass = JsonType::class)
class TaskTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean? = null

    @Column(name = "prize", nullable = false)
    var prize: Double? = null

    @Type(type = "json")
    @Column(name = "data", nullable = false)
    var data: TaskData? = null
}
