package ru.project.iidea.services

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.config
import ru.project.iidea.dao.*
import ru.project.iidea.data.User
import ru.project.iidea.utils.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


val googleClientId = config["google_client_id"].str
val googleClientSecret = config["google_client_secret"].str

val verifier = GoogleIdTokenVerifier.Builder(ApacheHttpTransport(), GsonFactory.getDefaultInstance())
    .apply {
        audience = listOf(googleClientId)
    }.build()


fun Route.users() = route("user") {

    post("auth") {
        process { (_, _) ->
            val token = call.receive<String>()
            withContext(Dispatchers.IO) {
                // False positive. Blocking is OK in IO dispatcher.
                @Suppress("BlockingMethodInNonBlockingContext")
                val reply = GoogleAuthorizationCodeTokenRequest(
                    ApacheHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    googleClientId,
                    googleClientSecret,
                    token,
                    ""
                ).execute()
                @Suppress("BlockingMethodInNonBlockingContext")
                val verif = reply.parseIdToken()
                if (verif == null) {
                    call.response.status(HttpStatusCode.Forbidden)
                    null
                } else {

                    val gId = verif.payload.subject
                    transaction {
                        val existingUser = User.idFromGoogle(gId)
                        if (existingUser != null) {
                            call.response.status(HttpStatusCode.OK)
                            existingUser
                        } else {
                            call.response.status(HttpStatusCode.Created)
                            User.registerNewUser(reply.accessToken)
                        }
                    }
                }
            }
        }
    }

    get("{id}") {
        process { (params, _) ->
            val id = requireNotNull(params["id"]).long
            User.fromDatabase(id)
        }
    }

    get("{id}/feed") {
        process(notFoundForEmpty = false) { (params, _) ->
            val id = requireNotNull(params["id"]).long
            val user = User.fromDatabase(id) ?: return@process null
            transaction {
                Projects.slice(Projects.id)
                    .select { Projects.type.inList(user.subscriptions) and not(Projects.host.eq(id)) }
                    .toList()
                    .map { it[Projects.id] }
            }
        }
    }

    put("/subscription/{tag}") {
        process(respond = null) { (params, caller) ->
            require(caller > 0)
            val tag = requireNotNull(params["tag"]).str
            transaction {
                Subscriptions.insert {
                    it[user] = caller
                    it[subscription] = tag
                }
            }
        }
    }

    delete("/subscription/{tag}") {
        process(respond = null) { (params, caller) ->
            require(caller > 0)
            val tag = requireNotNull(params["tag"]).str
            transaction {
                Subscriptions.deleteWhere {
                    Subscriptions.user.eq(caller) and Subscriptions.subscription.eq(tag)
                }
            }
        }
    }

    get("{id}/responses") {
        process { (params, _) ->
            val id = requireNotNull(params["id"]).long
            Responses.slice(Responses.id).select { Responses.from.eq(id) }.map { it[Responses.id] }.toList()
        }
    }

    post {
        process { (params, caller) ->
            require(caller > 0)
            transaction {
                Users.update({ Users.id.eq(caller) }) {
                    params["status"]?.str?.let { v -> it[status] = v }
                    params["description"]?.str?.let { v -> it[description] = v }
                    params["phone"]?.str?.let { v -> it[phone] = v }
                }
            }
        }
    }
}