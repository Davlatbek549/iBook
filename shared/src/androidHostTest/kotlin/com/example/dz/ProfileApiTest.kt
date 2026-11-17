package com.example.dz

import com.example.dz.core.error.AppError
import com.example.dz.core.result.AppResult
import com.example.dz.data.remote.api.ApiConfig
import com.example.dz.data.remote.api.KtorProfileApi
import com.example.dz.data.remote.api.createAuthHttpClient
import com.example.dz.data.repository.RemoteProfileRepository
import com.example.dz.domain.model.BirthDate
import com.example.dz.domain.model.Gender
import com.example.dz.domain.model.PostalAddress
import com.example.dz.domain.model.ProfileVisibility
import com.example.dz.domain.model.SpecialityKind
import com.example.dz.domain.model.UsernameProblem
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

/** The app's side of dz-server's `/users/me` and username check, against a stand-in server. */
class ProfileApiTest {

    private val baseUrl = "https://example.com/api/v1"

    private val serverProfile = """
        {
          "user":{"id":"u1","name":"Amelia Hart","email":"amelia@hart.co","emailVerified":true,
                  "username":"amelia.reads","avatarUrl":null,"bannerUrl":"https://example.com/b.png"},
          "bio":"Slow mornings","phoneNumber":"+44 7700 900 123","phoneCountry":"GB",
          "language":"en-GB","currentGoalMinutes":20,"dateOfBirth":"1999-03-14","gender":"FEMALE",
          "specialityKind":"STUDY","speciality":"Literature","address":{"country":"GB","city":"London","street":null,"postalCode":"N1 7GU"},
          "profileVisibility":"PRIVATE","booksRead":0,"friendsCount":0,"collectionsCount":0
        }
    """.trimIndent()

    private fun session() = FakeLocalDataSource().apply {
        saveUserSession("u1", "Amelia", "amelia@hart.co", "access", "refresh", true)
    }

    @Test
    fun `the profile is read from the server's shape, with the dial code split off`() = runBlocking {
        val engine = MockEngine { respond(serverProfile, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json")) }
        createAuthHttpClient(session(), ApiConfig(baseUrl), engine).use { client ->
            val result = RemoteProfileRepository(KtorProfileApi(client, baseUrl), session()).getEditableProfile()
            val profile = assertIs<AppResult.Success<com.example.dz.domain.model.EditableProfile>>(result).data
            assertEquals("amelia.reads", profile.username)
            assertEquals("7700 900 123", profile.phoneNumber)
            assertEquals("GB", profile.phoneCountry)
            assertEquals(BirthDate(1999, 3, 14), profile.dateOfBirth)
            assertEquals(Gender.Female, profile.gender)
            assertEquals(SpecialityKind.Study, profile.specialityKind)
            assertEquals(PostalAddress("GB", "London", null, "N1 7GU"), profile.address)
            assertEquals(ProfileVisibility.Private, profile.visibility)
        }
    }

    @Test
    fun `saving sends the server's shape, joins the dial code and leaves blanks out`() = runBlocking {
        var sent: JsonObject? = null
        val engine = MockEngine { request ->
            if (request.method == HttpMethod.Put) {
                sent = Json.parseToJsonElement((request.body as TextContent).text).jsonObject
            }
            respond(serverProfile, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val local = session()
        createAuthHttpClient(local, ApiConfig(baseUrl), engine).use { client ->
            val repository = RemoteProfileRepository(KtorProfileApi(client, baseUrl), local)
            val profile = (repository.getEditableProfile() as AppResult.Success).data
            repository.saveEditableProfile(profile.copy(name = "Amelia Hart", bio = "   ", username = "Amelia.Reads"))
        }
        val body = sent!!
        assertEquals("Amelia Hart", body.getValue("name").jsonPrimitive.content)
        assertEquals("amelia.reads", body.getValue("username").jsonPrimitive.content)
        assertEquals("+44 7700 900 123", body.getValue("phoneNumber").jsonPrimitive.content)
        assertEquals("GB", body.getValue("phoneCountry").jsonPrimitive.content)
        assertEquals("1999-03-14", body.getValue("dateOfBirth").jsonPrimitive.content)
        assertEquals("FEMALE", body.getValue("gender").jsonPrimitive.content)
        assertEquals("STUDY", body.getValue("specialityKind").jsonPrimitive.content)
        assertEquals("PRIVATE", body.getValue("profileVisibility").jsonPrimitive.content)
        assertEquals("London", body.getValue("address").jsonObject.getValue("city").jsonPrimitive.content)
        // Fields this screen does not edit still travel, or the server would clear them.
        assertEquals("en-GB", body.getValue("language").jsonPrimitive.content)
        assertEquals(20, body.getValue("currentGoalMinutes").jsonPrimitive.content.toInt())
        // A whitespace-only bio is no bio.
        assertFalse("bio" in body)
    }

    @Test
    fun `a saved name is remembered for the screens that read the session`() = runBlocking {
        val engine = MockEngine { respond(serverProfile, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json")) }
        val local = session()
        createAuthHttpClient(local, ApiConfig(baseUrl), engine).use { client ->
            val repository = RemoteProfileRepository(KtorProfileApi(client, baseUrl), local)
            val profile = (repository.getEditableProfile() as AppResult.Success).data
            repository.saveEditableProfile(profile)
        }
        // The server answered "Amelia Hart"; the session said "Amelia".
        assertEquals("Amelia Hart", local.getUserName())
        assertEquals("access", local.getToken())
    }

    @Test
    fun `a taken username comes back as a profile error, not a network one`() = runBlocking {
        val engine = MockEngine {
            respond("""{"code":"UsernameTaken","message":"That username is taken"}""", HttpStatusCode.Conflict,
                headersOf(HttpHeaders.ContentType, "application/json"))
        }
        createAuthHttpClient(session(), ApiConfig(baseUrl), engine).use { client ->
            val result = RemoteProfileRepository(KtorProfileApi(client, baseUrl), session())
                .saveEditableProfile(com.example.dz.domain.model.EditableProfile(name = "Amelia", username = "amelia"))
            val error = assertIs<AppResult.Error>(result).error
            assertEquals(AppError.Profile(AppError.ProfileReason.UsernameTaken), error)
        }
    }

    @Test
    fun `a locked date of birth comes back as itself`() = runBlocking {
        val engine = MockEngine {
            respond("""{"code":"DateOfBirthLocked","message":"Date of birth can only be set once"}""", HttpStatusCode.Conflict,
                headersOf(HttpHeaders.ContentType, "application/json"))
        }
        createAuthHttpClient(session(), ApiConfig(baseUrl), engine).use { client ->
            val result = RemoteProfileRepository(KtorProfileApi(client, baseUrl), session())
                .saveEditableProfile(com.example.dz.domain.model.EditableProfile(name = "Amelia"))
            assertEquals(AppError.Profile(AppError.ProfileReason.DateOfBirthLocked), assertIs<AppResult.Error>(result).error)
        }
    }

    @Test
    fun `the username check asks with the username and reads the answer`() = runBlocking {
        var asked: String? = null
        val engine = MockEngine { request ->
            asked = request.url.parameters["username"]
            respond("""{"username":"amelia","available":false,"reason":"Taken","suggestions":["amelia.hart","amelia_reads"]}""",
                HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))
        }
        createAuthHttpClient(session(), ApiConfig(baseUrl), engine).use { client ->
            val result = RemoteProfileRepository(KtorProfileApi(client, baseUrl), session()).checkUsername("amelia")
            val answer = assertIs<AppResult.Success<com.example.dz.domain.model.UsernameAvailability>>(result).data
            assertEquals("amelia", asked)
            assertEquals(UsernameProblem.Taken, answer.problem)
            assertEquals(listOf("amelia.hart", "amelia_reads"), answer.suggestions)
        }
    }

    @Test
    fun `birth dates read and write the ISO form and reject impossible ones`() {
        assertEquals(BirthDate(2000, 2, 29), BirthDate.parseIso("2000-02-29"))
        assertEquals(null, BirthDate.parseIso("1999-02-29"))
        assertEquals("1999-03-14", BirthDate(1999, 3, 14).toIso())
        // 2026-11-13T00:00:00Z
        assertEquals(BirthDate(2026, 11, 13), BirthDate.fromEpochMillis(1_794_528_000_000L))
    }
}
