package com.example.dz.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Error body every dz-server endpoint returns on failure.
 *
 * [code] names the reason — for auth failures it matches an
 * `AppError.AuthReason` entry exactly, so it maps across without a lookup
 * table. Other modules contribute their own codes, so an unrecognised one is
 * treated as unknown rather than as a parsing failure.
 */
@Serializable
data class ApiErrorDto(
    val code: String? = null,
    val message: String? = null,
    val fieldErrors: Map<String, String>? = null
)
