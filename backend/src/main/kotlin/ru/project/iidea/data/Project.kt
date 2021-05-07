package ru.project.iidea.data

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects

data class Project(
    val id: Int,
    val type: String,
    val name: String,
    val hostId: Int,
    val description: String,
    val status: String
) {
    constructor(db: ResultRow) : this(
        db[Projects.id],
        db[Projects.type],
        db[Projects.name],
        db[Projects.host],
        db[Projects.description],
        db[Projects.status]
    )

    companion object {

        fun fromDatabase(id: Int): Project? = transaction {
            val res = Projects.select { Projects.id.eq(id) }.firstOrNull() ?: return@transaction null
            Project(res)
        }

    }
}
