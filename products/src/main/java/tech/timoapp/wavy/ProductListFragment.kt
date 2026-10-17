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
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.timoapp.wavy.adapter.ProductAdapter
import tech.timoapp.wavy.adapter.ProductLoadStateAdapter
import tech.timoapp.wavy.adapter.SkeletonAdapter
import tech.timoapp.wavy.products.R
import tech.timoapp.wavy.products.databinding.FragmentProductListBinding
import tech.timoapp.wavy.model.ProductListUiEvent
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

        val footerAdapter = ProductLoadStateAdapter { myAdapter.retry() }
        binding.rvProducts.adapter = myAdapter.withLoadStateFooter(footerAdapter)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == myAdapter.itemCount && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding.rvProducts.layoutManager = gridLayoutManager
        binding.rvProducts.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        val skeletonCount = calculateSkeletonCount()
        binding.rvSkeleton.adapter = SkeletonAdapter(skeletonCount)
        binding.rvSkeleton.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvSkeleton.addItemDecoration(GridSpacingItemDecoration(2, 8, 10))

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.dark_purple)
        binding.swipeRefreshLayout.setOnRefreshListener { myAdapter.refresh() }
        binding.btnRetry.setOnClickListener { myAdapter.retry() }
        binding.btnEmptyRetry.setOnClickListener { myAdapter.refresh() }
        binding.fabAddProduct.setOnClickListener { viewModel.addProduct() }

        observePagingData(myAdapter)
        observeLoadState(myAdapter)
        observeEvents(myAdapter)
    }

    fun scrollToTop() {
        binding.rvProducts.smoothScrollToPosition(0)
    }

    private fun observePagingData(adapter: ProductAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsPagingFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadState(adapter: ProductAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect { loadStates ->
                    val refreshState = loadStates.refresh
                    val isListEmpty = adapter.itemCount == 0

                    when (refreshState) {
                        is LoadState.Loading -> {
                            if (!binding.swipeRefreshLayout.isRefreshing) {
                                binding.shimmerLayout.isVisible = true
                                binding.shimmerLayout.startShimmer()
                                binding.rvProducts.isVisible = false
                            }
                            binding.layoutError.isVisible = false
                            binding.layoutEmpty.isVisible = false
                        }

                        is LoadState.Error -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            if (isListEmpty) {
                                binding.layoutError.isVisible = true
                                binding.rvProducts.isVisible = false
                                binding.layoutEmpty.isVisible = false
                            } else {
                                binding.layoutError.isVisible = false
                                binding.rvProducts.isVisible = true
                                android.widget.Toast.makeText(
                                    requireContext(),
                                    refreshState.error.localizedMessage ?: getString(R.string.error_default),
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        is LoadState.NotLoading -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            binding.shimmerLayout.stopShimmer()
                            binding.shimmerLayout.isVisible = false
                            binding.layoutError.isVisible = false
                            if (isListEmpty) {
                                binding.rvProducts.isVisible = false
                                binding.layoutEmpty.isVisible = true
                            } else {
                                binding.layoutEmpty.isVisible = false
                                binding.rvProducts.isVisible = true
                            }
                        }
                    }

                    val appendState = loadStates.append
                    if (appendState is LoadState.Error) {
                        android.widget.Toast.makeText(
                            requireContext(),
                            appendState.error.localizedMessage ?: getString(R.string.error_default),
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun observeEvents(adapter: ProductAdapter) {
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
                            adapter.refresh()
                            binding.rvProducts.smoothScrollToPosition(0)
                        }

                        is ProductListUiEvent.ProductDeleted -> {
                            android.widget.Toast.makeText(
                                requireContext(),
                                R.string.success_product_deleted,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                            adapter.refresh()
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

    private fun calculateSkeletonCount(spanCount: Int = 2, itemHeightDp: Float = 280f): Int {
        val displayMetrics = resources.displayMetrics
        val screenHeightDp = displayMetrics.heightPixels / displayMetrics.density
        return ((screenHeightDp / itemHeightDp).toInt() + spanCount - 1) * spanCount
    }
}
