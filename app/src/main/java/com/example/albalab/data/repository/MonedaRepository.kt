package com.example.albalab.data.repository

import com.example.albalab.network.RetrofitClient
import java.util.concurrent.ConcurrentHashMap

class MonedaRepository {

    private var cache: Map<String, Double>? = null
    private var cacheBase: String? = null
    private var cacheTime: Long = 0L
    private val ttlMs = 60L * 60L * 1000L

    suspend fun obtenerTasas(base: String = "CLP"): Result<Map<String, Double>> {
        val now = System.currentTimeMillis()
        if (cache != null && cacheBase == base && now - cacheTime < ttlMs) {
            return Result.success(cache!!)
        }
        return try {
            val response = RetrofitClient.exchangeRateService.obtenerTasas(base)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                cache = body.rates
                cacheBase = base
                cacheTime = now
                Result.success(body.rates)
            } else {
                Result.failure(Exception("No se pudieron obtener las tasas (código ${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
