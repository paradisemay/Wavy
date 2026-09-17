package tech.timoapp.wavy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import tech.timoapp.wavy.R
import tech.timoapp.wavy.databinding.ItemProductBinding
import tech.timoapp.wavy.domain.model.Product

class ProductAdapter(
    private val onItemClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val holder = ProductViewHolder(binding)
        binding.root.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onItemClicked(getItem(position).id)
            }
        }
        binding.ivDelete.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item.isRemovable) {
                    onDeleteClicked(item.id)
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder, position: Int
    ) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.ivProduct.load(product.imageUrl) {
                placeholder(R.drawable.bg_shimmer_rounded)
                error(R.drawable.bg_shimmer_rounded)
            }
            binding.tvProductName.text = product.name
            binding.tvProductArticle.text = product.article
            binding.tvProductPrice.text = product.price
            binding.tvProductTag.text = product.category
            binding.ivDelete.isVisible = product.isRemovable
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}