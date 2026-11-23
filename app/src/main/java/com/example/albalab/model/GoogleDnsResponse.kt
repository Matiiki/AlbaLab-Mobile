package com.example.albalab.model

data class GoogleDnsResponse(
    val Status: Int = 0,
    val TC: Boolean = false,
    val RD: Boolean = false,
    val RA: Boolean = false,
    val AD: Boolean = false,
    val CD: Boolean = false,
    val Question: List<Any>? = null,
    val Answer: List<Any>? = null
)
