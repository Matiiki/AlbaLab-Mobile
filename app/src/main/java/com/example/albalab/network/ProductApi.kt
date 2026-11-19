package com.example.albalab.network

import com.example.albalab.model.ProductoModel
import com.example.albalab.model.ProductoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductApi {

    @GET("api/productos")
    suspend fun listarProductos(
        @Header("Authorization") authorization: String? = null
    ): Response<List<ProductoModel>>

    @POST("api/productos")
    suspend fun crearProducto(
        @Header("Authorization") authorization: String,
        @Body request: ProductoRequest
    ): Response<ProductoModel>

    @PUT("api/productos/{id}")
    suspend fun actualizarProducto(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body request: ProductoRequest
    ): Response<ProductoModel>

    @DELETE("api/productos/{id}")
    suspend fun eliminarProducto(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    ): Response<Unit>
}
