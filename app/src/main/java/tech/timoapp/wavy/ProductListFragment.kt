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
import tech.timoapp.wavy.model.ProductListUiEvent
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
        val myAdapter = ProductAdapter(
            onItemClicked = { productId ->
                val bundle = Bundle().apply {
                    putString("PRODUCT_ID", productId)
                }
                findNavController().navigate(
                    R.id.action_productListFragment_to_productDetailsFragment, bundle
                )
            },
            onDeleteClicked = { productId ->
                viewModel.deleteProduct(productId)
            }
        ).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.rvProducts.adapter = myAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        val skeletonCount = calculateSkeletonCount()
        binding.rvSkeleton.adapter = SkeletonAdapter(skeletonCount)
        binding.rvSkeleton.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSkeleton.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.dark_purple)
        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.loadProducts() }
        binding.btnRetry.setOnClickListener { viewModel.loadProducts() }
        binding.btnEmptyRetry.setOnClickListener { viewModel.loadProducts() }
        binding.fabAddProduct.setOnClickListener { viewModel.addProduct() }

        observeUiState(myAdapter)
        observeEvents()
    }

    fun scrollToTop() {
        binding.rvProducts.smoothScrollToPosition(0)
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is ProductListUiEvent.ProductAdded -> {
                            android.widget.Toast.makeText(
                                requireContext(),
                                R.string.success_product_added,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                            binding.rvProducts.smoothScrollToPosition(0)
                        }
                        is ProductListUiEvent.ProductDeleted -> {
                            android.widget.Toast.makeText(
                                requireContext(),
                                R.string.success_product_deleted,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ProductListUiEvent.Error -> {
                            val message = event.messageRes?.let { getString(it) }
                                ?: event.message
                                ?: getString(R.string.error_default)
                            android.widget.Toast.makeText(
                                requireContext(),
                                message,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun observeUiState(adapter: ProductAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ProductListUiState.Loading -> {
                            if (!binding.swipeRefreshLayout.isRefreshing) {
                                binding.shimmerLayout.isVisible = true
                                binding.shimmerLayout.startShimmer()
                                binding.rvProducts.isVisible = false
                            }
                            binding.layoutError.isVisible = false
                            binding.layoutEmpty.isVisible = false
                        }

                        is ProductListUiState.Success -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.layoutError.isVisible = false
                            binding.layoutEmpty.isVisible = false
                            binding.rvProducts.isVisible = true
                            adapter.submitList(state.products)
                        }

                        is ProductListUiState.Empty -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.layoutError.isVisible = false
                            binding.rvProducts.isVisible = false
                            binding.layoutEmpty.isVisible = true
                        }

                        is ProductListUiState.Error -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.layoutEmpty.isVisible = false
                            binding.rvProducts.isVisible = false
                            binding.layoutError.isVisible = true
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
