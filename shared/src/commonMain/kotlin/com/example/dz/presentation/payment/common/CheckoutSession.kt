package com.example.dz.presentation.payment.common

import com.example.dz.data.local.LocalDataSource
import com.example.dz.domain.model.Purchase

/** Carries the reviewed order through the existing checkout screens, scoped to its account. */
class CheckoutSession(private val local: LocalDataSource) {
    private var owner: String? = null
    private var order: Purchase? = null

    var purchase: Purchase?
        get() = order.takeIf { owner == local.getUserId() }
        set(value) {
            owner = local.getUserId()
            order = value
        }
}
