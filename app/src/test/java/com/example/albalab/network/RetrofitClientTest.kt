package com.example.albalab.network

import org.junit.Assert.assertEquals
import org.junit.Test

class RetrofitClientTest {

    @Test
    fun `usuario y webpay usan el gateway 9090`() {
        assertEquals(
            "http://192.168.1.100:9090/",
            RetrofitClient.BASE_URL
        )
    }

    @Test
    fun `el panel admin usa el puerto 9092`() {
        assertEquals(
            "http://192.168.1.100:9092/",
            RetrofitClient.ADMIN_BASE_URL
        )
    }

    @Test
    fun `la url de retorno de webpay apunta al gateway`() {
        assertEquals(
            "http://192.168.1.100:9090/api/webpay/retorno",
            RetrofitClient.WEBPAY_RETURN_URL
        )
    }
}
