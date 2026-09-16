package tech.timoapp.wavy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.timoapp.wavy.R
import tech.timoapp.wavy.model.BalanceModel
import tech.timoapp.wavy.model.FeedbackModel
import tech.timoapp.wavy.model.ProductFeedbacksModel
import tech.timoapp.wavy.model.ReviewsUiState

@Composable
fun ReviewsScreen(
    state: ReviewsUiState, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    when (state) {
        is ReviewsUiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.background_color)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(R.color.dark_purple)
                )
            }
        }

        is ReviewsUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.background_color))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = state.message, color = colorResource(R.color.main_text_color)
                    )
                    Button(onClick = onRetry) {
                        Text(text = "Повторить")
                    }
                }
            }
        }

        is ReviewsUiState.Success -> {
            ReviewsContent(
                balance = state.balance,
                products = state.products,
                isBalanceFromCache = state.isBalanceFromCache,
                onRefresh = onRetry,
                modifier = modifier,
            )
        }
    }
}

@Composable
fun ReviewsContent(
    balance: BalanceModel,
    products: List<ProductFeedbacksModel>,
    modifier: Modifier = Modifier,
    isBalanceFromCache: Boolean = false,
    onRefresh: () -> Unit = {},
) {
    var selectedReview by remember {
        mutableStateOf<Pair<ProductFeedbacksModel, FeedbackModel>?>(null)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceCard(balance = balance)
            }

            if (isBalanceFromCache) {
                item {
                    CacheNotificationBanner(onClick = onRefresh)
                }
            }

            if (balance.chartBars.isNotEmpty()) {
                item {
                    SalesChartCard(balance = balance)
                }
            }

            items(
                items = products, key = { it.id }) { product ->
                ReviewBlock(
                    product = product, onFeedbackClick = { feedback ->
                        selectedReview = product to feedback
                    })
            }
        }

        selectedReview?.let { (product, feedback) ->
            ReviewDetailBottomSheet(
                product = product, feedback = feedback, onDismiss = { selectedReview = null })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenSuccessPreview() {
    ReviewsContent(
        balance = balanceModel, products = listOf(mockProductFeedback)
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenLoadingPreview() {
    ReviewsScreen(
        state = ReviewsUiState.Loading, onRetry = {})
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenErrorPreview() {
    ReviewsScreen(
        state = ReviewsUiState.Error("Не удалось загрузить данные"), onRetry = {})
}
