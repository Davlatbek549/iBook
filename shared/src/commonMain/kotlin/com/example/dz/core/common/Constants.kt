package com.example.dz.core.common

object AppConstants {
    const val APP_NAME = "DZ"

    /**
     * Mirrors `PASSWORD_MIN_LENGTH` in dz-server. Checking it here turns a rejection that costs a
     * network round trip — up to a minute while the server wakes — into instant feedback. The
     * server still enforces it; this only saves the trip.
     */
    const val PASSWORD_MIN_LENGTH = 8
}
