package tech.timoapp.wavy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import tech.timoapp.wavy.databinding.ItemProductLoadStateBinding

class ProductLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ProductLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = ItemProductLoadStateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LoadStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemProductLoadStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.layoutError.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
        }
    }
}
