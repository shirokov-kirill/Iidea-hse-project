package ru.project.iidea.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Subscriptions
import ru.project.iidea.dao.Users

data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val description: String,
    val subscriptions: List<String>,
    val state: String,
    val projects: List<Int>) {

    constructor(db: List<ResultRow>) : this(
        db.first()[Users.id],
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        db.mapNotNull { it.getOrNull(Subscriptions.subscription) }.distinct(),
        db.first()[Users.status],
        db.mapNotNull { it.getOrNull(Projects.id) }.distinct()
    )


    companion object {

        fun fromDatabase(id: Int): User? = transaction {

            val res = (Users leftJoin  Subscriptions leftJoin Projects)
                .slice(Users.id, Users.status, Subscriptions.subscription, Projects.id)
                .select {
                    Users.id.eq(id)
                }.toList()
            return@transaction if(res.isNotEmpty()) User(res) else null
        }

    }

}