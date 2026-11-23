package com.example.albalab.model

data class ExchangeRateResponse(
    val result: String? = null,
    val base_code: String? = null,
    val rates: Map<String, Double> = emptyMap()
)
