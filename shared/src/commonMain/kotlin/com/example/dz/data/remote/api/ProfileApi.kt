package com.example.dz.data.remote.api

import com.example.dz.core.error.AppError
import com.example.dz.data.remote.ProfileBackendException
import com.example.dz.data.remote.dto.ApiErrorDto
import com.example.dz.domain.model.ProfileImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/** dz-server's `/users/me` and the username check Edit Profile makes while someone types. */
interface ProfileApi {
    suspend fun profile(): ProfileResponseDto
    suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileResponseDto
    suspend fun usernameAvailability(username: String): UsernameAvailabilityDto

    /** Replaces the picture with [jpeg], already cut to the server's exact size. */
    suspend fun uploadImage(image: ProfileImage, jpeg: ByteArray): ProfileResponseDto

    suspend fun removeImage(image: ProfileImage): ProfileResponseDto
}

class KtorProfileApi(private val client: HttpClient, private val baseUrl: String) : ProfileApi {

    override suspend fun profile(): ProfileResponseDto =
        client.get("$baseUrl/users/me").body()

    override suspend fun updateProfile(request: UpdateProfileRequestDto): ProfileResponseDto =
        withProfileErrors {
            client.put("$baseUrl/users/me") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }

    override suspend fun usernameAvailability(username: String): UsernameAvailabilityDto =
        client.get("$baseUrl/users/username-availability") {
            parameter("username", username)
        }.body()

    /**
     * A multipart part named `image`. The filename matters: Spring only binds a part to a
     * `MultipartFile` when it has one, and answers "part missing" otherwise.
     */
    override suspend fun uploadImage(image: ProfileImage, jpeg: ByteArray): ProfileResponseDto =
        withProfileErrors {
            client.put("$baseUrl/users/me/${image.path}") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                "image",
                                jpeg,
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=\"${image.path}.jpg\"")
                                },
                            )
                        }
                    )
                )
            }.body()
        }

    override suspend fun removeImage(image: ProfileImage): ProfileResponseDto =
        withProfileErrors { client.delete("$baseUrl/users/me/${image.path}").body() }

    private val ProfileImage.path: String
        get() = when (this) {
            ProfileImage.Photo -> "avatar"
            ProfileImage.Banner -> "banner"
        }

    /**
     * Turns the server's profile refusals into [ProfileBackendException], so a taken username or a
     * locked date of birth reaches the screen as itself rather than as "check your connection".
     * Anything else is rethrown for `runRemote` to map by status, as every other call is.
     */
    private suspend fun <T> withProfileErrors(block: suspend () -> T): T =
        try {
            block()
        } catch (error: ResponseException) {
            val code = runCatching { error.response.body<ApiErrorDto>().code }.getOrNull()
            val reason = when (code) {
                VALIDATION_FAILED -> AppError.ProfileReason.Invalid
                else -> AppError.ProfileReason.entries.firstOrNull { it.name == code }
            }
            throw reason?.let(::ProfileBackendException) ?: error
        }

    private companion object {
        const val VALIDATION_FAILED = "ValidationFailed"
    }
}

@Serializable
data class ProfileResponseDto(
    val user: ProfileUserDto,
    val bio: String? = null,
    val phoneNumber: String? = null,
    val phoneCountry: String? = null,
    val language: String? = null,
    val currentGoalMinutes: Int? = null,
    /** ISO date, `1999-03-14`. */
    val dateOfBirth: String? = null,
    /** `FEMALE`, `MALE` or `PREFER_NOT_TO_SAY`. */
    val gender: String? = null,
    /** `WORK` or `STUDY`. */
    val specialityKind: String? = null,
    val speciality: String? = null,
    val address: AddressDto? = null,
    /** `OPEN` or `PRIVATE`. An older server leaves it out, which means open. */
    val profileVisibility: String = "OPEN",
)

@Serializable
data class ProfileUserDto(
    val id: String,
    val name: String,
    val email: String? = null,
    /** Absent from an older server, which does not gate on verification at all — see UserDto. */
    val emailVerified: Boolean = true,
    val username: String? = null,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
)

@Serializable
data class AddressDto(
    val country: String? = null,
    val city: String? = null,
    val street: String? = null,
    val postalCode: String? = null,
)

/** A full replacement: see `EditableProfile` for why every field is always sent. */
@Serializable
data class UpdateProfileRequestDto(
    val name: String,
    val username: String? = null,
    val avatarUrl: String? = null,
    val bannerUrl: String? = null,
    val bio: String? = null,
    val phoneNumber: String? = null,
    val phoneCountry: String? = null,
    val language: String? = null,
    val currentGoalMinutes: Int? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val specialityKind: String? = null,
    val speciality: String? = null,
    val address: AddressDto? = null,
    val profileVisibility: String? = null,
)

@Serializable
data class UsernameAvailabilityDto(
    val username: String,
    val available: Boolean,
    /** `Taken` or `Invalid`. */
    val reason: String? = null,
    val suggestions: List<String> = emptyList(),
)
