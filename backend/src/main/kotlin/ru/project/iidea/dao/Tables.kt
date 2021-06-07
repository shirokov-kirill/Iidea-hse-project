package ru.project.iidea.dao

import org.jetbrains.exposed.sql.Table

object Registrations : Table("registrations") {

    val googleId = text("id")
    val userId = long("id") references Users.id

    override val primaryKey = PrimaryKey(googleId)

}

object Users : Table("users") {


    val id = long("id").autoIncrement()
    val status = varchar("status", 255)

    override val primaryKey = PrimaryKey(id)
}

object Subscriptions : Table("subscriptions") {

    val user = long("user") references Users.id
    val subscription = varchar("subscription", 255)

}

object Projects : Table("projects") {
    val id = long("id").autoIncrement()
    val type = varchar("type", 255)
    val name = text("name")
    val description = text("description")
    val status = varchar("status", 255)
    val host = long("host") references Users.id

    override val primaryKey = PrimaryKey(id)
}

object Responses : Table("responses") {

    val id = long("id").autoIncrement()
    val from = long("from") references Users.id
    val to = long("to") references Projects.id

    override val primaryKey = PrimaryKey(id)

}