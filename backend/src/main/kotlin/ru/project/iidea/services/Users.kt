package ru.project.iidea.services

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.*
import ru.project.iidea.data.User
import ru.project.iidea.utils.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


val verifier = GoogleIdTokenVerifier.Builder(ApacheHttpTransport(), JacksonFactory())
    .apply {
        audience = listOf("621982323742-2oselqnv8k1ce2qvrbcqnjjameejaihc.apps.googleusercontent.com")
    }.build()


fun Route.users() = route("user") {

    post("auth") {
        process { (_, _) ->
            val token = call.receive<String>()
            suspendCoroutine<Long?> { cont ->
                launch(Dispatchers.IO) {
                    try {
                        val verif = verifier.verify(token)
                        if (verif == null) {
                            call.response.status(HttpStatusCode.Forbidden)
                            cont.resume(null)
                        } else {
                            val gId = verif.payload.subject
                            transaction {
                                val existingUser = User.idFromGoogle(gId)
                                if (existingUser != null) {
                                    call.response.status(HttpStatusCode.OK)
                                    cont.resume(existingUser)
                                } else {
                                    val newUser = verifyInsert {
                                        Users.insert {
                                            it[status] = "SEEKING"
                                        }
                                    }.first()[Users.id]
                                    verifyInsert {
                                        Registrations.insert { r ->
                                            r[googleId] = gId
                                            r[userId] = newUser
                                        }
                                    }
                                    call.response.status(HttpStatusCode.Created)
                                    cont.resume(newUser)
                                }
                            }
                        }
                    } catch (t: Throwable) {
                        cont.resumeWithException(t)
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
                    .select { Projects.type.inList(user.subscriptions) }
                    .toList()
                    .map { it[Projects.id] }
            }
        }
    }

    put("/subscription/{tag}") {
        process(respond = null) { (params, caller) ->
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
}