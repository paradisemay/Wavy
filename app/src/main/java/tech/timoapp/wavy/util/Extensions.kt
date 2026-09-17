package tech.timoapp.wavy.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale
import kotlin.math.roundToInt

fun <T> Fragment.safeApiCall(
    apiCall: suspend () -> Response<T>, onError: ((String) -> Unit)? = null, onSuccess: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    onSuccess(body)
                } else {
                    val msg = "Пустой ответ от сервера"
                    onError?.invoke(msg) ?: Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                val msg = "Ошибка сервера: ${response.code()}"
                Toast.makeText(
                    requireContext(), msg, Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            val msg = "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
            onError?.invoke(msg) ?: Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
    }
}

fun Double.formatPrice(): String {
    val rounded = this.roundToInt()
    return String.format(Locale.US, "%,d", rounded).replace(',', ' ') + " ₽"
}

fun Double.formatRating(): String {
    return String.format(Locale.US, "%.1f", this).replace(".", ",")
}