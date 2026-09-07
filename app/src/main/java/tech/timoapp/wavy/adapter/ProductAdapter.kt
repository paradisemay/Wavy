package tech.timoapp.wavy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import tech.timoapp.wavy.R
import tech.timoapp.wavy.databinding.ItemProductBinding
import tech.timoapp.wavy.model.Product

class ProductAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder, position: Int
    ) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)

        holder.itemView.setOnClickListener {
            onItemClicked(currentProduct.id)
        }
    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.ivProduct.load(product.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.mock_product_image)
                error(R.drawable.mock_product_image)
            }
            binding.tvProductName.text = product.name
            binding.tvProductArticle.text = product.article
            binding.tvProductPrice.text = product.price
            binding.tvProductTag.text = product.category
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