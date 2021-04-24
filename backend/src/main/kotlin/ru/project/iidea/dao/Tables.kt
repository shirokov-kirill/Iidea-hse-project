package ru.project.iidea.dao

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {


    val id = integer("id").autoIncrement()
    val status = varchar("status", 255)

    override val primaryKey = PrimaryKey(id)
}

object Subscriptions : Table("subscriptions") {

    val user = integer("user") references Users.id
    val subscription = varchar("subscription", 255)

}

object Projects : Table("projects") {
    val id = integer("id").autoIncrement()
    val type = varchar("type", 255)
    val name = text("name")
    val description = text("description")
    val status = varchar("status", 255)
    val host = integer("host") references Users.id

    override val primaryKey = PrimaryKey(id)
}

object Responses : Table("responses") {

    val id = integer("id").autoIncrement()
    val from = integer("from") references Users.id
    val to = integer("to") references Projects.id

    override val primaryKey = PrimaryKey(id)

}