package ru.project.iidea

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.gson.JsonParser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import ru.project.iidea.dao.*
import ru.project.iidea.data.User
import ru.project.iidea.services.projects
import ru.project.iidea.services.responses
import ru.project.iidea.services.users
import ru.project.iidea.services.verifier
import ru.project.iidea.utils.obj
import ru.project.iidea.utils.str
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


val config = JsonParser.parseString(File("config.json").readText()).obj
private val connectUrl =
    "jdbc:mysql://${config["host"].str}:3306/${config["database"].str}?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC"

data class UserPrincipal(val id: Long, val token: GoogleIdToken?) : Principal

fun main() {
    Database.connect(connectUrl, "com.mysql.cj.jdbc.Driver", config["user"].str, config["password"].str)
//    transaction {
//        SchemaUtils.create(Users, Projects, Subscriptions, Responses, Registrations)
//    }
    embeddedServer(Netty, 8000) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Authentication) {
            basic {
                validate { cred ->
                    cred.name.toLongOrNull()?.let { id ->
                        if (id == -1L) {
                            UserPrincipal(-1, null) //Guest user
                        } else {
                            withContext(Dispatchers.IO) {
                                // False positive. Blocking is OK in IO dispatcher.
                                @Suppress("BlockingMethodInNonBlockingContext")
                                val token = runCatching { verifier.verify(cred.password) }.getOrNull()
                                if (token != null) {
                                    val tokenId = transaction { User.idFromGoogle(token.payload.subject) }
                                    if (id != tokenId) {
                                        null //Invalid id/token pair
                                    } else {
                                        UserPrincipal(id, token)
                                    }
                                } else {
                                    null // Invalid token -> Forbidden
                                }
                            }
                        }
                    }
                }
            }
        }
        install(CallLogging) {
            level = Level.DEBUG
        }
        routing {
            authenticate {
                users()
                projects()
                responses()
            }
        }
    }.start(wait = true)
}