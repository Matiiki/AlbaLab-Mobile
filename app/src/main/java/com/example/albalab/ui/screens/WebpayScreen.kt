package com.example.albalab.ui.screens

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.viewmodel.WebpayViewModel

@Composable
fun WebpayScreen(
    pagoId: Int,
    montoTotal: Double,
    token: String,
    concepto: String = "ORDEN",
    modifier: Modifier = Modifier,
    viewModel: WebpayViewModel = viewModel(),
    onPagoTerminado: () -> Unit
) {

    LaunchedEffect(pagoId) {
        viewModel.iniciarPago(
            pagoId = pagoId,
            montoTotal = montoTotal,
            tokenUsuario = token,
            concepto = concepto
        )
    }

    val cargando = viewModel.cargando
    val webpayUrl = viewModel.webpayUrl
    val webpayToken = viewModel.webpayToken
    val mensajeError = viewModel.mensajeError
    val confirmando = viewModel.confirmando

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        when {
            cargando -> {
                CircularProgressIndicator()
            }

            webpayUrl != null && webpayToken != null -> {
                val url = webpayUrl
                val tokenTransaccion = webpayToken

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
                                        viewModel.confirmarPago(
                                            tokenUsuario = token,
                                            onPagoAprobado = onPagoTerminado
                                        )

                                        return true
                                    }

                                    return false
                                }
                            }

                            val postData = "token_ws=$tokenTransaccion".toByteArray(Charsets.UTF_8)
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
                        text = "No se pudo completar el pago"
                    )

                    mensajeError?.let {
                        Text(text = it)
                    }

                    Button(
                        onClick = {
                            viewModel.reintentar(
                                montoTotal = montoTotal,
                                tokenUsuario = token,
                                concepto = concepto
                            )
                        }
                    ) {
                        Text("REINTENTAR")
                    }
                }
            }
        }

        if (confirmando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Text(
                        text = "Confirmando pago...",
                        color = Color.White
                    )
                }
            }
        }
    }
}
