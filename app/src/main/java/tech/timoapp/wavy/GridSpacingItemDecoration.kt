package tech.timoapp.wavy

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpacingDp: Int,
    private val verticalSpacingDp: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val density = view.context.resources.displayMetrics.density
        val hSpacingPx = (horizontalSpacingDp * density).toInt()
        val vSpacingPx = (verticalSpacingDp * density).toInt()

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * hSpacingPx / spanCount
        outRect.right = hSpacingPx - (column + 1) * hSpacingPx / spanCount

        if (position >= spanCount) {
            outRect.top = vSpacingPx
        }
    }
}
