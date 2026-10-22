package com.example.progr3ssapp

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.progr3ssapp.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    // A helyi hálózati engedélykérés eredménye (a launchert még az onCreate előtt regisztrálni kell)
    private val requestLocalNetworkPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(
                    this,
                    R.string.local_network_permission_required,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. A splash screen telepítése a super.onCreate() ELŐTT
        //    (ez állítja át a témát a postSplashScreenTheme-re, amit az AppCompat igényel)
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Csak valódi indításkor kérjük: forgatás után ne jelenjen meg újra a dialógus
        if (savedInstanceState == null) {
            requestLocalNetworkPermissionIfNeeded()
        }

        // 2. A splash látható marad, amíg a kezdeti betöltés tart.
        //    Csak valódi indításkor várunk: forgatás utáni újraépítéskor nincs splash,
        //    ott a feltétel csak 1,5 másodpercre befagyasztaná a képernyőt.
        var isLoading = savedInstanceState == null
        if (isLoading) {
            lifecycleScope.launch {
                delay(1500L) // ide jön majd a valódi inicializálás (token, adatbázis stb.)
                isLoading = false
            }
        }
        splashScreen.setKeepOnScreenCondition { isLoading }

        // 3. Egyedi fade-out animáció kilépéskor
        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val view = splashScreenViewProvider.view
            ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).apply {
                duration = 500L
                interpolator = AccelerateDecelerateInterpolator()
                doOnEnd { splashScreenViewProvider.remove() }
                start()
            }
        }

        enableEdgeToEdge()

        // Binding a setContentView(R.layout...) helyett
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        applyWindowInsets()

        // NavController a NavHostFragment-ből
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Felső szintű képernyők: ezeken nincs "vissza" nyíl a toolbaron.
        // A Login is ide tartozik, különben értelmetlen vissza nyíl jelenne meg rajta.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.loginFragment, R.id.homeFragment, R.id.profileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupBottomNav()
    }

    private fun setupBottomNav() {
        // Összeköti a menüt a NavControllerrel (a kijelölt fül követi az aktuális képernyőt)
        binding.bottomNav.setupWithNavController(navController)

        // A beépített kattintáskezelő a gráf kezdőpontjáig (loginFragment) ürítené a back stacket,
        // de az belépés után már nincs rajta, így minden fülváltás új képernyőt rakna a stackre.
        // Ezért a homeFragment-ig ürítünk (ugyanaz a recept, mint a Compose-os MainGraph-nál).
        binding.bottomNav.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId, null, navOptions {
                popUpTo(R.id.homeFragment) { saveState = true } // megjegyzi a fülek állapotát
                launchSingleTop = true // nem rak egymásra két ugyanolyan fület
                restoreState = true // visszaállítja a fül állapotát, ha visszatérünk rá
            })
            true
        }

        // Az alsó menü elrejtése Login és Register alatt
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment ->
                    binding.bottomNav.visibility = View.GONE
                else -> binding.bottomNav.visibility = View.VISIBLE
            }
            // Az alsó térköz a menü láthatóságától függ (lásd applyWindowInsets)
            ViewCompat.requestApplyInsets(binding.root)
        }
    }
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            // Látható alsó menünél a BottomNavigationView maga kerüli ki a navigációs sávot,
            // rejtett menünél (Login/Register) a gyökér tolja feljebb a tartalmat
            val bottom = if (binding.bottomNav.isVisible) 0 else maxOf(bars.bottom, ime.bottom)
            view.setPadding(bars.left, bars.top, bars.right, bottom)
            // A felső és oldalsó részt már kezeltük, csak az alsót adjuk tovább a gyerekeknek
            insets.inset(bars.left, bars.top, bars.right, 0)
        }
    }

    private fun requestLocalNetworkPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CINNAMON_BUN && // API 37+
            checkSelfPermission(Manifest.permission.ACCESS_LOCAL_NETWORK) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestLocalNetworkPermission.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }

    // A toolbar "vissza" nyila (pl. Register -> Login)
    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
}
