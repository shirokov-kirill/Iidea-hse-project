package ru.project.iidea.data

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Responses

data class Response(
    val id: Int,
    val from: Int,
    val to: Int
) {

    constructor(db: ResultRow) : this(
        db[Responses.id],
        db[Responses.from],
        db[Responses.to]
    )


    companion object {

        fun fromDatabase(id: Int): Response? = transaction {
            val res = Responses.select { Responses.id.eq(id) }.firstOrNull() ?: return@transaction null
            Response(res)
        }

    }
}
