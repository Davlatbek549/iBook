package com.example.dz.core.platform

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale

actual fun deviceRegionCode(): String? = NSLocale.currentLocale.countryCode?.takeIf { it.length == 2 }
