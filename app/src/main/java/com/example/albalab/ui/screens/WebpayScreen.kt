package com.example.albalab.ui.screens

import android.graphics.Bitmap
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.albalab.model.ColaboradorRequest
import com.example.albalab.viewmodel.WebpayViewModel

@Composable
fun WebpayScreen(
    pagoId: Int,
    montoTotal: Double,
    token: String,
    concepto: String = "ORDEN",
    ordenCompra: String? = null,
    colaboracion: ColaboradorRequest? = null,
    modifier: Modifier = Modifier,
    viewModel: WebpayViewModel = viewModel(),
    onPagoTerminado: () -> Unit
) {

    LaunchedEffect(pagoId) {
        viewModel.iniciarPago(
            pagoId = pagoId,
            montoTotal = montoTotal,
            tokenUsuario = token,
            ordenCompra = ordenCompra,
            colaboracion = colaboracion
        )
    }

    // La navegación solo ocurre cuando el backend ya guardó la colaboración
    // (o cuando terminó un pago de carrito, que no registra colaboración).
    LaunchedEffect(viewModel.pagoTerminado) {
        if (viewModel.pagoTerminado) {
            onPagoTerminado()
        }
    }

    val cargando = viewModel.cargando
    val webpayUrl = viewModel.webpayUrl
    val webpayToken = viewModel.webpayToken
    val mensajeError = viewModel.mensajeError
    val errorColaboracion = viewModel.errorColaboracion
    val confirmando = viewModel.confirmando
    val guardandoColaboracion = viewModel.guardandoColaboracion
    val pagoAprobado = viewModel.pagoAprobado
    val pagoTerminado = viewModel.pagoTerminado

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

            webpayUrl != null && webpayToken != null && !pagoAprobado -> {
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
                                            tokenUsuario = token
                                        )

                                        return true
                                    }

                                    return false
                                }

                                // Algunos WebView no llaman shouldOverrideUrlLoading
                                // cuando el retorno de Transbank llega por POST.
                                override fun onPageStarted(
                                    view: WebView?,
                                    url: String?,
                                    favicon: Bitmap?
                                ) {
                                    super.onPageStarted(view, url, favicon)

                                    if (url?.contains("/api/webpay/retorno") == true) {
                                        viewModel.confirmarPago(
                                            tokenUsuario = token
                                        )
                                    }
                                }
                            }

                            val postData =
                                "token_ws=$tokenTransaccion".toByteArray(Charsets.UTF_8)
                            postUrl(url, postData)
                        }
                    }
                )
            }

            guardandoColaboracion -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Pago aprobado. Guardando colaboración...",
                        textAlign = TextAlign.Center
                    )
                }
            }

            pagoAprobado && pagoTerminado -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Pago completado. Redirigiendo...",
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = if (pagoAprobado) {
                            "El pago fue aprobado"
                        } else {
                            "No se pudo completar el pago"
                        },
                        textAlign = TextAlign.Center
                    )

                    mensajeError?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center
                        )
                    }

                    errorColaboracion?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.error
                        )
                    }

                    if (pagoAprobado) {
                        Text(
                            text = "Reintentar el guardado no volverá a realizar el cobro.",
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.reintentarProceso()
                        }
                    ) {
                        Text(
                            text = if (pagoAprobado) {
                                "REINTENTAR GUARDADO"
                            } else {
                                "REINTENTAR PAGO"
                            }
                        )
                    }
                }
            }
        }

        val textoOverlay = when {
            guardandoColaboracion -> "Guardando colaboración..."
            confirmando -> "Confirmando pago..."
            else -> null
        }

        if (textoOverlay != null) {
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
                        text = textoOverlay,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
