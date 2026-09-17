package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tech.timoapp.wavy.adapter.ProductAdapter
import tech.timoapp.wavy.databinding.FragmentProductListBinding
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
        binding.rvProducts.adapter = myAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        val skeletonCount = calculateSkeletonCount()
        binding.rvSkeleton.adapter = SkeletonAdapter(skeletonCount)
        binding.rvSkeleton.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSkeleton.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        loadProduct(myAdapter)
    }

    private fun loadProduct(adapter: ProductAdapter) {
        binding.shimmerLayout.isVisible = true
        binding.shimmerLayout.startShimmer()
        binding.rvProducts.isVisible = false

        safeApiCall(
            apiCall = {
//                kotlinx.coroutines.delay(1_000)
                RetrofitClient.apiService.getProducts()
            },
            onError = {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.isVisible = false
            },
            onSuccess = { response ->
                binding.shimmerLayout.isVisible = false
                binding.shimmerLayout.stopShimmer()
                binding.rvProducts.isVisible = true

                val products = response.data.map { dto -> dto.toProduct() }
                adapter.submitList(products)
            }
        )
    }

    private fun calculateSkeletonCount(spanCount: Int = 2, itemHeightDp: Float = 280f): Int {
        val displayMetrics = resources.displayMetrics
        val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density
        return ((screenHeightDp / itemHeightDp).toInt() + spanCount - 1) * spanCount
    }
}

class SkeletonAdapter(private val count: Int = 10) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_skeleton, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int
    ) = Unit

    override fun getItemCount(): Int = count
}