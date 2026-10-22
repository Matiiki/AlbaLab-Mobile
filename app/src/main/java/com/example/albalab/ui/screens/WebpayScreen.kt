package com.example.albalab.ui.screens

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.albalab.model.WebpayRequest
import com.example.albalab.network.RetrofitClient

@Composable
fun WebpayScreen(
    montoTotal: Double,
    modifier: Modifier = Modifier,
    onPagoTerminado: () -> Unit
) {

    var webpayUrl by remember {
        mutableStateOf<String?>(null)
    }

    var webpayToken by remember {
        mutableStateOf<String?>(null)
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    var mensajeError by remember {
        mutableStateOf<String?>(null)
    }

    var intento by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(montoTotal, intento) {
        cargando = true
        mensajeError = null
        webpayUrl = null
        webpayToken = null

        try {
            val tiempoActual = System.currentTimeMillis().toString()

            val request = WebpayRequest(
                ordenCompra = "ORDEN-$tiempoActual",
                idSesion = "SESION-$tiempoActual",
                monto = montoTotal,
                urlRetorno = RetrofitClient.WEBPAY_RETURN_URL
            )

            val response =
                RetrofitClient.apiService
                    .crearTransaccionWebpay(request)

            if (response.url.isBlank() || response.token.isBlank()) {
                mensajeError = "El backend respondió sin URL o token de Webpay."
            } else {
                webpayUrl = response.url
                webpayToken = response.token
            }

        } catch (e: Exception) {
            e.printStackTrace()
            mensajeError = e.message ?: "Error desconocido al conectar con el backend."
        } finally {
            cargando = false
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            cargando -> {
                CircularProgressIndicator()
            }

            webpayUrl != null && webpayToken != null -> {
                val url = webpayUrl!!
                val token = webpayToken!!

                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true

                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val nuevaUrl = request?.url?.toString().orEmpty()

                                    if (nuevaUrl.contains("/api/webpay/retorno")) {
                                        onPagoTerminado()
                                        return true
                                    }

                                    return false
                                }
                            }

                            val postData = "token_ws=$token".toByteArray(Charsets.UTF_8)
                            postUrl(url, postData)
                        }
                    }
                )
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Error al conectar con Webpay"
                    )

                    mensajeError?.let {
                        Text(text = it)
                    }

                    Button(
                        onClick = {
                            intento++
                        }
                    ) {
                        Text("REINTENTAR")
                    }
                }
            }
        }
    }
}
