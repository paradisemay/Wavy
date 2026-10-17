package tech.timoapp.wavy.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.timoapp.wavy.R

sealed class Screen(val route: String) {
    data object Products : Screen("products")
    data object Reviews : Screen("reviews")
    data object ProductDetails : Screen("products/{productId}") {
        fun createRoute(productId: String) = "products/$productId"
    }
}

sealed class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val iconRes: Int
) {
    data object Products : BottomNavItem(
        route = Screen.Products.route,
        titleRes = R.string.menu_products,
        iconRes = tech.timoapp.wavy.core.R.drawable.ic_box
    )

    data object Reviews : BottomNavItem(
        route = Screen.Reviews.route,
        titleRes = R.string.menu_reviews,
        iconRes = tech.timoapp.wavy.core.R.drawable.ic_review
    )
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var productsScrollToTopTrigger by remember { mutableIntStateOf(0) }
    var reviewsScrollToTopTrigger by remember { mutableIntStateOf(0) }

    val navItems = listOf(
        BottomNavItem.Products,
        BottomNavItem.Reviews
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(tech.timoapp.wavy.core.R.color.background_color),
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val currentRoute = currentDestination?.route

                navItems.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { destination ->
                        destination.route == item.route || (item.route == Screen.Products.route && destination.route?.startsWith("products") == true)
                    } == true

                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = stringResource(item.titleRes)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(item.titleRes),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
                            selectedTextColor = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
                            indicatorColor = colorResource(tech.timoapp.wavy.core.R.color.light_purple),
                            unselectedIconColor = colorResource(tech.timoapp.wavy.core.R.color.sub_text_color),
                            unselectedTextColor = colorResource(tech.timoapp.wavy.core.R.color.sub_text_color)
                        ),
                        onClick = {
                            if (isSelected) {
                                if (item == BottomNavItem.Products) {
                                    if (currentRoute?.startsWith("products/") == true) {
                                        navController.popBackStack(Screen.Products.route, inclusive = false)
                                    } else {
                                        productsScrollToTopTrigger++
                                    }
                                } else if (item == BottomNavItem.Reviews) {
                                    reviewsScrollToTopTrigger++
                                }
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Products.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Products.route) {
                ProductListScreen(
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetails.createRoute(productId))
                    },
                    scrollToTopTrigger = productsScrollToTopTrigger
                )
            }

            composable(Screen.Reviews.route) {
                ReviewsRoute(
                    scrollToTopTrigger = reviewsScrollToTopTrigger
                )
            }

            composable(
                route = Screen.ProductDetails.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId").orEmpty()
                ProductDetailsRoute(
                    productId = productId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
