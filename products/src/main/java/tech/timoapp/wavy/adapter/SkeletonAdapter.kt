package tech.timoapp.wavy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tech.timoapp.wavy.products.R

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
