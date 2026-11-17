package com.example.dz.core.platform

import java.util.Locale

actual fun deviceRegionCode(): String? = Locale.getDefault().country.takeIf { it.length == 2 }
