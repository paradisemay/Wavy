package tech.timoapp.wavy.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.timoapp.wavy.reviews.R
import tech.timoapp.wavy.model.FeedbackModel
import tech.timoapp.wavy.model.ProductFeedbacksModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailBottomSheet(
    product: ProductFeedbacksModel,
    feedback: FeedbackModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colorResource(R.color.background_color),
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            ProductHeader(product = product)

            Spacer(modifier = Modifier.height(16.dp))

            CommentCard(
                feedback = feedback,
                isExpanded = true,
                backgroundColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewDetailBottomSheetPreview() {
    ReviewDetailBottomSheet(
        product = mockProductFeedback,
        feedback = mockFeedback1,
        onDismiss = {}
    )
}
