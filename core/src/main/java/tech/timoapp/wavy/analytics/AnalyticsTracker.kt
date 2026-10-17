package tech.timoapp.wavy.analytics

import androidx.compose.runtime.staticCompositionLocalOf

interface AnalyticsTracker {
    fun logReviewsScreenShow()
    fun logProductClick()
    fun logAddProductClick()
    fun logDeleteProductClick()
}

object NoOpAnalyticsTracker : AnalyticsTracker {
    override fun logReviewsScreenShow() = Unit

    override fun logProductClick() = Unit

    override fun logAddProductClick() = Unit

    override fun logDeleteProductClick() = Unit
}

val LocalAnalyticsTracker = staticCompositionLocalOf<AnalyticsTracker> { NoOpAnalyticsTracker }
