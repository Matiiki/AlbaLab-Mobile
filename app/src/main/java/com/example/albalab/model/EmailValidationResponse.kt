package com.example.albalab.model

data class EmailValidationResponse(
    val email: String? = null,
    val autocorrect: String? = null,
    val deliverability: String? = null,
    val quality_score: String? = null,
    val is_valid_format: Boolean? = null,
    val is_free_email: Boolean? = null,
    val is_disposable_email: Boolean? = null,
    val is_role_email: Boolean? = null,
    val is_catchall_email: Boolean? = null,
    val is_mx_found: Boolean? = null,
    val is_smtp_valid: Boolean? = null
)
