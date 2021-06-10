package ru.project.iidea.services

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import io.ktor.http.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Responses
import ru.project.iidea.data.Project
import ru.project.iidea.data.Response
import ru.project.iidea.utils.int
import ru.project.iidea.utils.long
import ru.project.iidea.utils.process



fun Route.responses() = route("response") {

    route("{id}") {

        get {
            process { (params, _) ->
                val id = requireNotNull(params["id"]).long
                Response.fromDatabase(id)
            }
        }

        delete {
            process(respond = null) { (params, caller) ->
                require(caller > 0)
                val id = requireNotNull(params["id"]).long
                val response = Response.fromDatabase(id) ?: return@process HttpStatusCode.NotFound
                val project = Project.fromDatabase(response.to) ?: return@process HttpStatusCode.Gone
                if (response.from != caller && project.hostId != caller) {
                    return@process HttpStatusCode.Forbidden
                }
                transaction {
                    Responses.deleteWhere { Responses.id.eq(id) }
                }
            }
        }

    }

}