package com.example.progr3ssapp.navigation

import kotlinx.serialization.Serializable

/** Argumentum nélküli képernyők: `object`. */
@Serializable
object Login

@Serializable
object Register

/** A bejelentkezés utáni képernyőket összefogó beágyazott gráf. */
@Serializable
object MainGraph

@Serializable
object Home

/** Argumentumos képernyő: `data class`. Az alapértékek miatt az alsó menüből is megnyitható. */
@Serializable
data class Profile(
    val userId: String = "guest",
    val userName: String = "Guest"
)
