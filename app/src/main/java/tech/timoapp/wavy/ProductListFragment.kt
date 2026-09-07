package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import tech.timoapp.wavy.adapter.ProductAdapter
import tech.timoapp.wavy.databinding.FragmentProductListBinding
import tech.timoapp.wavy.model.Product
import tech.timoapp.wavy.model.toProduct
import tech.timoapp.wavy.network.RetrofitClient
import tech.timoapp.wavy.util.safeApiCall

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        val myAdapter = ProductAdapter {
            val bundle = Bundle().apply {
                putString("PRODUCT_ID", it)
            }
            findNavController().navigate(
                R.id.action_productListFragment_to_productDetailsFragment, bundle
            )
        }
        loadProduct(myAdapter)
        binding.rvProducts.adapter = myAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))
    }

    private fun loadProduct(adapter: ProductAdapter) {
        safeApiCall(
            apiCall = { RetrofitClient.apiService.getProducts() },
            onSuccess = { response ->
                val products = response.data.map { dto -> dto.toProduct() }
                adapter.submitList(products)
            }
        )
    }
}
