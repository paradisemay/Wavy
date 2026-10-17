package tech.timoapp.wavy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import org.koin.androidx.compose.koinViewModel
import tech.timoapp.wavy.viewmodel.ReviewsViewModel
import tech.timoapp.wavy.reviews.R
import tech.timoapp.wavy.model.BalanceModel
import tech.timoapp.wavy.model.FeedbackModel
import tech.timoapp.wavy.model.ProductFeedbacksModel
import tech.timoapp.wavy.model.ReviewsUiState
import tech.timoapp.wavy.analytics.LocalAnalyticsTracker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsRoute(
    modifier: Modifier = Modifier,
    viewModel: ReviewsViewModel = koinViewModel(),
    scrollToTopTrigger: Int = 0
) {
    val state = viewModel.uiState.value
    val analyticsTracker = LocalAnalyticsTracker.current
    val isRefreshing = state is ReviewsUiState.Loading
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state) {
        if (state is ReviewsUiState.Success) {
            analyticsTracker.logReviewsScreenShow()
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.loadReviewsData() },
        state = pullToRefreshState,
        modifier = modifier.fillMaxSize()
    ) {
        ReviewsScreen(
            state = state,
            onRetry = { viewModel.loadReviewsData() },
            scrollToTopTrigger = scrollToTopTrigger,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ReviewsScreen(
    state: ReviewsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    scrollToTopTrigger: Int = 0
) {
    when (state) {
        is ReviewsUiState.Loading -> {
            ReviewsSkeleton(modifier = modifier)
        }

        is ReviewsUiState.Error -> {
            ErrorStateScreen(
                onRetry = onRetry,
                modifier = modifier
            )
        }

        is ReviewsUiState.Success -> {
            ReviewsContent(
                balance = state.balance,
                products = state.products,
                isBalanceFromCache = state.isBalanceFromCache,
                onRefresh = onRetry,
                scrollToTopTrigger = scrollToTopTrigger,
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
    scrollToTopTrigger: Int = 0,
) {
    var selectedReview by remember {
        mutableStateOf<Pair<ProductFeedbacksModel, FeedbackModel>?>(null)
    }
    val listState = rememberLazyListState()
    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = if (isBalanceFromCache) 88.dp else 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BalanceCard(balance = balance)
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

        if (isBalanceFromCache) {
            CacheNotificationBanner(
                onClick = onRefresh,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
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

@Composable
fun ReviewsSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(8.dp))
                .purpleShimmerEffect()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp)
                .clip(RoundedCornerShape(8.dp))
                .purpleShimmerEffect()
        )

        repeat(2) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .shimmerEffect()
                            )
                            Box(
                                modifier = Modifier
                                    .width(56.dp)
                                    .height(12.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .shimmerEffect()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .purpleShimmerEffect()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .softPurpleShimmerEffect()
                )
            }
        }
    }
}
