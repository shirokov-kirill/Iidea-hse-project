package ru.project.iidea.services

import io.ktor.http.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Responses
import ru.project.iidea.data.Project
import ru.project.iidea.utils.*

fun Route.projects() = route("project") {

    put {
        process { (params, caller) ->
            require(caller > 0)
            transaction {
                verifyInsert {
                    Projects.insert {
                        it[name] = requireNotNull(params["name"]).str
                        it[type] = requireNotNull(params["type"]).str
                        it[description] = requireNotNull(params["description"]).str
                        it[status] = requireNotNull(params["status"]).str
                        it[host] = caller
                    }
                }.first()[Projects.id]
            }
        }
    }

    post("search") {
        process(notFoundForEmpty = false) { (params, _) ->
            transaction {
                val query = params["query"]?.str?.let(::escapeForLike)
                val tags = params["tags"]?.arr?.map { it.str }
                val excludedTags = params["excludedTags"]?.arr?.map { it.str } ?: emptyList()
                Projects.slice(Projects.id).select {
                    var res: Op<Boolean> = Projects.type.notInList(excludedTags)
                    if (tags != null) res = res and Projects.type.inList(tags)
                    if (query != null) res =
                        res and (Projects.name.like("%$query%") or Projects.description.like("%$query%"))
                    res

                }.map { it[Projects.id] }
            }
        }
    }

    route("{id}") {
        get {
            process { (params, _) ->
                val id = requireNotNull(params["id"]).long
                Project.fromDatabase(id)
            }
        }

        post {
            process(respond = null) { (params, caller) ->
                require(caller > 0)
                val id = requireNotNull(params["id"]).long
                val project = Project.fromDatabase(id) ?: return@process HttpStatusCode.NotFound
                if (project.hostId != caller) {
                    return@process HttpStatusCode.Forbidden
                }
                transaction {
                    Projects.update({ Projects.id.eq(id) }) {
                        params["name"]?.str?.let { v -> it[name] = v }
                        params["type"]?.str?.let { v -> it[type] = v }
                        params["description"]?.str?.let { v -> it[description] = v }
                        params["status"]?.str?.let { v -> it[status] = v }
                    }
                }
            }
        }

        delete {
            process(respond = null) { (params, caller) ->
                require(caller > 0)
                val id = requireNotNull(params["id"]).long
                val project = Project.fromDatabase(id) ?: return@process HttpStatusCode.NotFound
                if (project.hostId != caller) {
                    return@process HttpStatusCode.Forbidden
                }
                transaction {
                    Projects.deleteWhere { Projects.id.eq(id) }
                    Responses.deleteWhere { Responses.to.eq(id) }
                }
            }
        }

        route("responses") {
            get {
                process(notFoundForEmpty = false) { (params, _) ->
                    val id = requireNotNull(params["id"]).long
                    transaction {
                        Responses
                            .slice(Responses.id)
                            .select { Responses.to.eq(id) }
                            .map { it[Responses.id] }
                            .toList()
                    }
                }
            }

            put {
                process { (params, caller) ->
                    require(caller > 0)
                    val id = requireNotNull(params["id"]).long
                    transaction {
                        verifyInsert {
                            Responses.insert {
                                it[from] = caller
                                it[to] = id
                            }
                        }.first()[Responses.id]
                    }
                }
            }
        }


    }


}