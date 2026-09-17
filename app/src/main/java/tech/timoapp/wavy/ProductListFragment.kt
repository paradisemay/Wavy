package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import tech.timoapp.wavy.adapter.ProductAdapter
import tech.timoapp.wavy.databinding.FragmentProductListBinding
import tech.timoapp.wavy.model.Product

class ProductListFragment : Fragment() {

    companion object {
        fun getMockProducts() = listOf(
            Product(
                article = "435789101",
                name = "Блузка боди на запах без рукавов",
                price = "732 — 1 104 ₽",
                category = "Одежда",
                imageRes = R.drawable.mock_product_image
            ),
            Product(
                article = "435789102",
                name = "Чехол для iPad Air 1",
                price = "1 653 ₽",
                category = "Техника",
                imageRes = R.drawable.mock_product_image_3
            ),
            Product(
                article = "435789103",
                name = "Механический милый ластик для школы и офиса",
                price = "126 ₽",
                category = "Канцелярия",
                imageRes = R.drawable.mock_product_image_4
            ),
            Product(
                article = "435789104",
                name = "Боди блузка с длинными рукавами",
                price = "2 459 ₽",
                category = "Одежда",
                imageRes = R.drawable.mock_product_image_2
            ),
            Product(
                article = "435789105",
                name = "Худи оверсайз Maybeс",
                price = "2 660 ₽",
                category = "Одежда",
                imageRes = R.drawable.mock_product_image_5
            ),
            Product(
                article = "435789106",
                name = "Apple · Watch Series 8 GPS 45mm",
                price = "27 660 ₽",
                category = "Техника",
                imageRes = R.drawable.mock_product_image_6
            ),
            Product(
                article = "435789107",
                name = "Блузка боди на запах без рукавов",
                price = "2 235 ₽",
                category = "Одежда",
                imageRes = R.drawable.mock_product_image_7
            ),
            Product(
                article = "435789108",
                name = "Точилка механическая",
                price = "138 ₽",
                category = "Техника",
                imageRes = R.drawable.mock_product_image_8
            ),
        )
    }

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myData = getMockProducts()
        val myAdapter = ProductAdapter(myData) {
            val bundle = Bundle()
            bundle.putString("PRODUCT_ID", it)
            findNavController().navigate(
                R.id.action_productListFragment_to_productDetailsFragment,
                bundle
            )
        }
        binding.rvProducts.adapter = myAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))
    }
}
