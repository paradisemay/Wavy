package tech.timoapp.wavy.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.model.ProductListUiEvent
import tech.timoapp.wavy.products.R
import tech.timoapp.wavy.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = koinViewModel(),
    scrollToTopTrigger: Int = 0
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val lazyPagingItems = viewModel.productsPagingFlow.collectAsLazyPagingItems()
    val listState = rememberLazyGridState()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ProductListUiEvent.ProductAdded -> {
                    Toast.makeText(
                        context,
                        R.string.success_product_added,
                        Toast.LENGTH_SHORT
                    ).show()
                    lazyPagingItems.refresh()
                    listState.animateScrollToItem(0)
                }

                is ProductListUiEvent.ProductDeleted -> {
                    Toast.makeText(
                        context,
                        R.string.success_product_deleted,
                        Toast.LENGTH_SHORT
                    ).show()
                    lazyPagingItems.refresh()
                }

                is ProductListUiEvent.Error -> {
                    val message = event.messageRes?.let(resources::getString)
                        ?: event.message
                        ?: resources.getString(tech.timoapp.wavy.core.R.string.error_default)
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    val appendState = lazyPagingItems.loadState.append
    LaunchedEffect(appendState) {
        if (appendState is LoadState.Error) {
            val message = appendState.error.localizedMessage
                ?: resources.getString(tech.timoapp.wavy.core.R.string.error_default)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val refreshState = lazyPagingItems.loadState.refresh
    val isRefreshing = refreshState is LoadState.Loading && lazyPagingItems.itemCount > 0
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(tech.timoapp.wavy.core.R.color.background_color))
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { lazyPagingItems.refresh() },
            state = pullToRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                refreshState is LoadState.Loading && lazyPagingItems.itemCount == 0 -> {
                    ProductListSkeleton(modifier = Modifier.fillMaxSize())
                }

                refreshState is LoadState.Error && lazyPagingItems.itemCount == 0 -> {
                    ErrorStateScreen(
                        onRetry = { lazyPagingItems.retry() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                refreshState is LoadState.NotLoading && lazyPagingItems.itemCount == 0 -> {
                    EmptyProductList(
                        onRetry = { lazyPagingItems.refresh() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    ProductListGrid(
                        lazyPagingItems = lazyPagingItems,
                        listState = listState,
                        onProductClick = onProductClick,
                        onDeleteClick = { viewModel.deleteProduct(it) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.addProduct() },
            containerColor = colorResource(tech.timoapp.wavy.core.R.color.dark_purple),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = stringResource(R.string.action_add_product),
                tint = Color.White
            )
        }
    }
}

@Composable
fun ProductListGrid(
    lazyPagingItems: LazyPagingItems<Product>,
    listState: androidx.compose.foundation.lazy.grid.LazyGridState,
    onProductClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = listState,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val product = lazyPagingItems[index]
            if (product != null) {
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onDeleteClick = { onDeleteClick(product.id) }
                )
            }
        }

        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(2) }) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    CircularProgressIndicator(
                        color = colorResource(tech.timoapp.wavy.core.R.color.dark_purple),
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(tech.timoapp.wavy.core.R.color.background_color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(tech.timoapp.wavy.core.R.color.background_color))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.mock_product_image),
                    error = painterResource(R.drawable.mock_product_image),
                    modifier = Modifier.fillMaxSize()
                )

                if (product.category.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .background(
                                color = colorResource(tech.timoapp.wavy.core.R.color.white),
                                shape = RoundedCornerShape(48.dp)
                            )
                            .padding(horizontal = 9.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = product.category,
                            color = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x88FFFFFF))
                        .clickable(
                            role = Role.Button,
                            onClick = onDeleteClick
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = stringResource(R.string.action_delete_product),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Text(
                text = product.price,
                color = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 6.dp)
            )

            Text(
                text = product.name,
                color = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Text(
                text = product.article,
                color = colorResource(tech.timoapp.wavy.core.R.color.sub_text_color),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp, bottom = 20.dp)
            )
        }
    }
}

@Composable
fun ProductCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(tech.timoapp.wavy.core.R.color.background_color)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .size(width = 70.dp, height = 20.dp)
                        .background(
                            color = colorResource(tech.timoapp.wavy.core.R.color.white),
                            shape = RoundedCornerShape(48.dp)
                        )
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
                    .size(width = 100.dp, height = 18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 6.dp)
                    .size(width = 130.dp, height = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 6.dp)
                    .size(width = 60.dp, height = 12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun ProductListSkeleton(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        userScrollEnabled = false,
        modifier = modifier.fillMaxSize()
    ) {
        items(8) {
            ProductCardSkeleton()
        }
    }
}

@Composable
fun EmptyProductList(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_box),
            contentDescription = null,
            tint = colorResource(tech.timoapp.wavy.core.R.color.dark_purple),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.empty_product_list_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(tech.timoapp.wavy.core.R.color.main_text_color),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.empty_product_list_subtitle),
            fontSize = 14.sp,
            color = colorResource(tech.timoapp.wavy.core.R.color.sub_text_color),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(tech.timoapp.wavy.core.R.color.dark_purple),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(180.dp)
        ) {
            Text(
                text = stringResource(tech.timoapp.wavy.core.R.string.action_refresh),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
