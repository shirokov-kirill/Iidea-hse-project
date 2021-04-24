package ru.project.iidea.services

import io.ktor.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Responses
import ru.project.iidea.dao.Subscriptions
import ru.project.iidea.data.User
import ru.project.iidea.utils.int
import ru.project.iidea.utils.process
import ru.project.iidea.utils.str


fun Route.users() = route("user") {

    get("{id}") {
        process { (params, _) ->
            val id = requireNotNull(params["id"]).int
            User.fromDatabase(id)
        }
    }

    get("{id}/feed") {
        process(notFoundForEmpty = false) { (params, _) ->
            val id = requireNotNull(params["id"]).int
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
            val id = requireNotNull(params["id"]).int
            Responses.slice(Responses.id).select { Responses.from.eq(id) }.map { it[Responses.id] }.toList()
        }
    }
}