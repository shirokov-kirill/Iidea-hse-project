package ru.project.iidea.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.util.*
import java.util.function.BiConsumer
import java.util.function.BinaryOperator
import java.util.function.Supplier
import java.util.stream.Collector
import java.util.stream.Stream
import java.util.stream.StreamSupport

inline fun <T> JsonElement.wrap(f: JsonElement.() -> T): T = try {
    this.f()
} catch (e: UnsupportedOperationException) {
    throw IllegalArgumentException(e)
}

inline val JsonElement.str: String
    get() = wrap { asString }

inline val JsonElement.bln: Boolean
    get() = wrap { asBoolean }

inline val JsonElement.int: Int
    get() = wrap { asInt }

inline val JsonElement.long: Long
    get() = wrap { asLong }

inline val JsonElement.obj: JsonObject
    get() = wrap { asJsonObject }

inline val JsonElement.arr: JsonArray
    get() = wrap { asJsonArray }

inline val JsonElement.flt: Float
    get() = wrap { asFloat }

inline val JsonElement.dbl: Double
    get() = wrap { asDouble }


operator fun JsonObject.set(key: String, value: String) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Char) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Boolean) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: Number) = addProperty(key, value)

operator fun JsonObject.set(key: String, value: JsonElement) = add(key, value)

operator fun JsonElement.get(vararg keys: String): JsonElement? {
    var o = this
    keys.forEach { o = o.obj[it] }
    return o
}

operator fun JsonObject.contains(key: String): Boolean = has(key)

fun JsonObject.merge(other: JsonObject) {
    for ((k, v) in other.entrySet()) {
        this[k] = v
    }
}

val jsonArrayCollector: Collector<String, JsonArray, JsonArray> = Collector.of(
    Supplier(::JsonArray),
    BiConsumer(JsonArray::add), { l, r ->
        l.addAll(r)
        l
    })