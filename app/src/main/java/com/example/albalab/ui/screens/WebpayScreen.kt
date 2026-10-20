package com.example.albalab.ui.screens

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.albalab.model.WebpayRequest
import com.example.albalab.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun WebpayScreen(
    montoTotal: Double,
    modifier: Modifier = Modifier,
    onPagoTerminado: () -> Unit
) {
    var webpayUrl by remember { mutableStateOf<String?>(null) }
    var webpayToken by remember { mutableStateOf<String?>(null) }
    var cargando by remember { mutableStateOf(true) }

    // 1. Llamar al backend para crear la transacción apenas se abre esta pantalla
    LaunchedEffect(Unit) {
        try {
            val request = WebpayRequest(monto = montoTotal)
            // Llama a tu servidor Spring Boot
            val response = RetrofitClient.apiService.crearTransaccionWebpay(request)
            webpayUrl = response.url
            webpayToken = response.token
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cargando = false
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (cargando) {
            // Muestra un círculo de carga mientras el backend responde
            CircularProgressIndicator()
        } else if (webpayUrl != null && webpayToken != null) {
            // 2. Mostrar la página de Transbank cuando tenemos los datos
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        webViewClient = object : WebViewClient() {
                            // Detectamos cuándo Transbank nos devuelve a la app
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                val url = request?.url?.toString() ?: ""
                                // Si la URL contiene nuestra ruta de retorno, cerramos el WebView
                                if (url.contains("/api/webpay/retorno")) {
                                    onPagoTerminado()
                                    return true
                                }
                                return super.shouldOverrideUrlLoading(view, request)
                            }
                        }
                    }
                },
                update = { webView ->
                    // Webpay exige que el token se envíe mediante POST
                    val postData = "token_ws=$webpayToken".toByteArray()
                    webView.postUrl(webpayUrl!!, postData)
                }
            )
        } else {
            Text("Error al conectar con Webpay")
        }
    }
}