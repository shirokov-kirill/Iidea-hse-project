package ru.project.iidea.services

import io.ktor.http.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Responses
import ru.project.iidea.data.Project
import ru.project.iidea.data.Response
import ru.project.iidea.utils.int
import ru.project.iidea.utils.process

fun Route.responses() = route("response") {

    route("{id}") {

        get {
            process { (params, _) ->
                val id = requireNotNull(params["id"]).int
                Response.fromDatabase(id)
            }
        }

        delete {
            process(respond = null) { (params, caller) ->
                val id = requireNotNull(params["id"]).int
                val project = Response.fromDatabase(id) ?: return@process HttpStatusCode.NotFound
                if (project.from != caller) {
                    return@process HttpStatusCode.Forbidden
                }
                transaction {
                    Responses.deleteWhere { Responses.id.eq(id) }
                }
            }
        }

    }

}