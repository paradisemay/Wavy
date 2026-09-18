package tech.timoapp.wavy.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsTracker(context: Context) : AnalyticsTracker {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun logReviewsScreenShow() {
        firebaseAnalytics.logEvent("on_reviews_screen_show", null)
    }

    override fun logProductClick() {
        firebaseAnalytics.logEvent("on_product_click", null)
    }

    override fun logAddProductClick() {
        firebaseAnalytics.logEvent("on_add_product_click", null)
    }

    override fun logDeleteProductClick() {
        firebaseAnalytics.logEvent("on_delete_product_click", null)
    }
}
