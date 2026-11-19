package com.example.albalab.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.albalab.R
import com.example.albalab.model.ProductoModel

@Composable
fun ProductoImagen(
    producto: ProductoModel,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val imagenLocal = producto.imagenLocal ?: imagenLocalPara(producto.nombre)
    val imagenRespaldo = painterResource(imagenLocal)

    if (!producto.imagenUrl.isNullOrBlank()) {
        AsyncImage(
            model = producto.imagenUrl,
            contentDescription = producto.nombre,
            modifier = modifier,
            contentScale = contentScale,
            placeholder = imagenRespaldo,
            error = imagenRespaldo
        )
    } else {
        Image(
            painter = imagenRespaldo,
            contentDescription = producto.nombre,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

private fun imagenLocalPara(nombre: String): Int {
    return when {
        nombre.contains("difusor", ignoreCase = true) -> R.drawable.difusor_aroma
        nombre.contains("bolsa", ignoreCase = true) -> R.drawable.bolsa_albalab
        else -> R.drawable.logo_albalab
    }
}
