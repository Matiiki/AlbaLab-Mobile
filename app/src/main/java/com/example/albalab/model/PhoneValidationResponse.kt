package com.example.albalab.model

data class PhoneValidationResponse(
    val valid: Boolean? = null,
    val number: String? = null,
    val local_format: String? = null,
    val international_format: String? = null,
    val country_prefix: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val location: String? = null,
    val carrier: String? = null,
    val line_type: String? = null
)
