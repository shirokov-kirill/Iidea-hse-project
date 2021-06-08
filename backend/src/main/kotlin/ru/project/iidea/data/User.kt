package ru.project.iidea.data

import com.google.api.client.auth.oauth2.BearerToken.authorizationHeaderAccessMethod
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.oauth2.Oauth2
import com.google.api.services.people.v1.PeopleService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.project.iidea.dao.Projects
import ru.project.iidea.dao.Registrations
import ru.project.iidea.dao.Subscriptions
import ru.project.iidea.dao.Users
import ru.project.iidea.utils.verifyInsert
import java.io.IOException

data class User(
    val id: Long,
    val name: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val description: String,
    val subscriptions: List<String>,
    val state: String,
    val projects: List<Long>
) {

    constructor(db: List<ResultRow>) : this(
        db.first()[Users.id],
        db.first()[Users.name],
        db.first()[Users.surname],
        "",
        db.first()[Users.email],
        "",
        "",
        db.first()[Users.description],
        db.mapNotNull { it.getOrNull(Subscriptions.subscription) }.distinct(),
        db.first()[Users.status],
        db.mapNotNull { it.getOrNull(Projects.id) }.distinct()
    )


    companion object {

        fun fromDatabase(id: Long): User? = transaction {

            val res = (Users leftJoin Subscriptions leftJoin Projects)
                .slice(Users.id, Users.status, Subscriptions.subscription, Projects.id)
                .select {
                    Users.id.eq(id)
                }.toList()
            if (res.isNotEmpty()) User(res) else null
        }

        fun idFromGoogle(googleId: String): Long? = transaction {
            val res = Registrations
                .slice(Registrations.userId)
                .select { Registrations.googleId.eq(googleId) }.toList()
            if (res.isNotEmpty()) res.first()[Registrations.userId] else null
        }


        @Throws(IOException::class)
        fun registerNewUser(token: String): Long {
            val transport = ApacheHttpTransport()
            val factory = GsonFactory.getDefaultInstance()
            val cred = Credential(authorizationHeaderAccessMethod()).setAccessToken(token)
            val oauth = Oauth2.Builder(transport, factory, cred).apply {
                applicationName = "Iidea"

            }.build()
            val info = oauth.userinfo().get().execute()
            return transaction {
                val newUser = verifyInsert {
                    Users.insert {
                        it[status] = "SEEKING"
                        it[name] = info.givenName
                        it[surname] = info.familyName
                        it[email] = info.email
                        it[description] = ""
                    }
                }.first()[Users.id]
                verifyInsert {
                    Registrations.insert {
                        it[googleId] = info.id
                        it[userId] = newUser
                    }
                }
                newUser
            }
        }
    }

}