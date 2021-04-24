package ru.project.iidea

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
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Responses
import ru.project.iidea.dao.Subscriptions
import ru.project.iidea.dao.Users
import ru.project.iidea.services.projects
import ru.project.iidea.services.responses
import ru.project.iidea.services.users

private val connectUrl = "jdbc:mysql://localhost:3306/hse_project?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=UTC"

data class UserPrincipal(val id: Int) : Principal

fun main() {
    Database.connect(connectUrl, "com.mysql.cj.jdbc.Driver", "root", "dashwood")
    transaction {
        SchemaUtils.create(Users, Projects, Subscriptions, Responses)
    }
    embeddedServer(Netty, 8000) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Authentication) {
            basic {
                validate { cred ->
                    cred.name.toIntOrNull()?.let(::UserPrincipal)
                }
            }
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