package tech.timoapp.wavy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext
import tech.timoapp.wavy.analytics.FirebaseAnalyticsTracker
import tech.timoapp.wavy.analytics.LocalAnalyticsTracker
import tech.timoapp.wavy.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            val analyticsTracker = remember { FirebaseAnalyticsTracker(applicationContext) }
            CompositionLocalProvider(LocalAnalyticsTracker provides analyticsTracker) {
                KoinContext(GlobalContext.get()) {
                    MainScreen()
                }
            }
        }
    }
}
