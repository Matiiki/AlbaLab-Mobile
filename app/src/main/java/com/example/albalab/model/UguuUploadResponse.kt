package com.example.albalab.model

data class UguuFile(
    val hash: String? = null,
    val filename: String? = null,
    val url: String? = null,
    val size: Long? = null,
    val dupe: Boolean? = null
)

data class UguuUploadResponse(
    val success: Boolean = false,
    val files: List<UguuFile> = emptyList()
)
