package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.timoapp.wavy.adapter.ProductAdapter
import tech.timoapp.wavy.databinding.FragmentProductListBinding
import tech.timoapp.wavy.model.ProductListUiState
import tech.timoapp.wavy.viewmodel.ProductListViewModel

class ProductListFragment : Fragment() {

    private val viewModel: ProductListViewModel by viewModel()
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

        observeUiState(myAdapter)
    }

    private fun observeUiState(adapter: ProductAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ProductListUiState.Loading -> {
                            binding.shimmerLayout.isVisible = true
                            binding.shimmerLayout.startShimmer()
                            binding.rvProducts.isVisible = false
                        }

                        is ProductListUiState.Success -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.rvProducts.isVisible = true
                            adapter.submitList(state.products)
                        }

                        is ProductListUiState.Error -> {
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                        }
                    }
                }
            }
        }
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
