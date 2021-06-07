package ru.project.iidea.utils

import com.google.gson.JsonObject
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.project.iidea.UserPrincipal

typealias Ctx = PipelineContext<Unit, ApplicationCall>

data class RequestCtx(val params: JsonObject, val sender: Long)

private fun Parameters.toJson(): JsonObject = json {
    forEach { s, list ->
        s to list.first()
    }
}

object NoAutoResponse;

suspend fun <T> Ctx.process(
    notFoundForEmpty: Boolean = true,
    respond: ((T) -> Any?)? = { it },
    func: suspend (RequestCtx) -> T
) {
    try {
        val params = call.parameters.toJson()
        if (call.request.httpMethod != HttpMethod.Get
            && call.request.contentType().withoutParameters() == ContentType.Application.Json
        ) {
            val body = call.receive<JsonObject>()
            params.merge(body)
        }
        val caller = requireNotNull(call.principal<UserPrincipal>()).id
        val result = func(RequestCtx(params, caller))
        if(result == NoAutoResponse) {
            return
        }
        if (respond != null) {
            if (notFoundForEmpty && result is List<*> && result.isEmpty()) {
                return call.respond(HttpStatusCode.NotFound)
            }
            call.respond(respond(result) ?: HttpStatusCode.NotFound)
        } else {
            if (result is HttpStatusCode) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        call.respond(HttpStatusCode.BadRequest)
    } catch (e: Exception) {
        e.printStackTrace()
        call.respond(HttpStatusCode.InternalServerError, e.localizedMessage)
    }
}


fun <T : Any> verifyInsert(block: () -> InsertStatement<T>): List<ResultRow> {
    val res = block().resultedValues
    require(res != null && res.isNotEmpty())
    return res
}
