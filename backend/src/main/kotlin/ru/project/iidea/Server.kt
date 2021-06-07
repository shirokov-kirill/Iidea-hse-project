package ru.project.iidea

import com.google.gson.JsonParser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level
import ru.project.iidea.dao.*
import ru.project.iidea.services.projects
import ru.project.iidea.services.responses
import ru.project.iidea.services.users
import ru.project.iidea.utils.obj
import ru.project.iidea.utils.str
import java.io.File


val config = JsonParser.parseString(File("config.json").readText()).obj
private val connectUrl = "jdbc:mysql://${config["host"].str}:3306/${config["database"].str}?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC"

data class UserPrincipal(val id: Long) : Principal

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
                    cred.name.toLongOrNull()?.let(::UserPrincipal)
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