package tech.timoapp.wavy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        val container = findViewById<FragmentContainerView>(R.id.fragment_container)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.tab_products -> {
                    val currentDestinationId = navController.currentDestination?.id
                    if (currentDestinationId == R.id.productDetailsFragment) {
                        navController.popBackStack(R.id.productListFragment, false)
                    } else if (currentDestinationId == R.id.productListFragment) {
                        val currentFragment = navHostFragment.childFragmentManager.fragments
                            .firstOrNull { it is ProductListFragment } as? ProductListFragment
                        currentFragment?.scrollToTop()
                    }
                }
                R.id.tab_reviews -> {
                    val currentFragment = navHostFragment.childFragmentManager.fragments
                        .firstOrNull { it is ReviewsFragment } as? ReviewsFragment
                    currentFragment?.scrollToTop()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(container) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}