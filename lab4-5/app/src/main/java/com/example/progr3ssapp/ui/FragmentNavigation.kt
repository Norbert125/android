package com.example.progr3ssapp.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * Csak akkor navigál, ha az action az aktuális képernyőről elérhető.
 * Gyors dupla koppintásnál a második kattintás már a következő képernyőn futna le,
 * ahol az action nem létezik, és a navigate() IllegalArgumentException-nel összeomlana.
 */
fun Fragment.navigateSafely(@IdRes actionId: Int, args: Bundle? = null) {
    val navController = findNavController()
    if (navController.currentDestination?.getAction(actionId) != null) {
        navController.navigate(actionId, args)
    }
}
