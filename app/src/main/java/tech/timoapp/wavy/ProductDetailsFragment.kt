package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.timoapp.wavy.model.ProductDetailsUiModel
import tech.timoapp.wavy.ui.DetailsPageScreen

class ProductDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val productId = arguments?.getString("PRODUCT_ID")
        val selectedProduct = ProductListFragment.getMockProducts().find { it.article == productId }
        val uiModel = ProductDetailsUiModel(
            imageRes = selectedProduct?.imageRes ?: R.drawable.mock_product_image,
            category = selectedProduct?.category ?: "Бейдж",
            price = selectedProduct?.price ?: "1 234 ₽",
            article = selectedProduct?.article ?: "12345678",
            title = selectedProduct?.name ?: "Название товара",
            rating = "4,5",
            ordersCount = "8 236",
            stockCount = "947",
            description = "Блузка-боди скимс без рукавов от Moremio – это идеальное сочетание стиля и комфорта, которое подчеркнет вашу женственность и красоту. Соблазнительный вырез на груди и обтягивающий крой создают представительскую и элегантную модель, идеально подходящую как для вечерних летних выходов, так и для повседневной носки. Мягкая вискоза в составе ткани обеспечит комфорт в течение всего дня, а эластан придаст блузке-боди идеальную посадку по фигуре. Модель skims с запахом и стрингами добавит пикантности вашему образу, а складки на талии – изысканности. Благодаря эластичному материалу блузка-боди прекрасно подойдет для высоких девушек и женщин с пышными формами. Это универсальная модель, которая легко трансформируется из офисного варианта в вечерний наряд, просто добавив стильные аксессуары. Это та самая летняя блузка-боди из pinterest (пинтерест)"
        )

        return ComposeView(requireContext()).apply {
            setContent {
                DetailsPageScreen(
                    product = uiModel,
                    onBackClick = { findNavController().popBackStack() })
            }
        }
    }
}