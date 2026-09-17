package tech.timoapp.wavy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsControllerCompat
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext
import tech.timoapp.wavy.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            KoinContext(GlobalContext.get()) {
                MainScreen()
            }
        }
    }
}
