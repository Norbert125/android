package com.example.progr3ssapp.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/** Az OkHttp/Retrofit példányt egyszer építjük fel, és mindenhol ezt használjuk. */
object RetrofitClient {

    // Fizikai telefon: a számítógép LAN IP-je (a telefon és a gép ugyanazon a Wi-Fi-n legyen).
    // Emulátoron "http://10.0.2.2:8080/" kell (az a gép localhost-jának álneve).
    // Perjellel kell végződnie, a Retrofit ehhez fűzi hozzá a @POST("...") útvonalat.
    private const val BASE_URL = "http://10.0.54.71:8080/"

    val json = Json {
        ignoreUnknownKeys = true // a backend új mezői ne okozzanak hibát
        explicitNulls = false
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // a törzseket is a Logcatbe írja
            }
        )
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AuthApi::class.java)
    }
}
