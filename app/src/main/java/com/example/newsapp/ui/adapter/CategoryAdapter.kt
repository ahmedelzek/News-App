package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.databinding.ItemCategoryBinding
import com.example.newsapp.ui.model.Category

class CategoriesAdapter(
    private val categories: List<Category>,
    val onCategoryClick: (category: Category) -> Unit
) : Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoriesViewHolder(private val itemBinding: ItemCategoryBinding) :
        ViewHolder(itemBinding.root) {
        fun bind(category: Category) {
            itemBinding.itemCategoryNameTv.text = category.title
            itemBinding.itemCategoryImage.setImageResource(category.imageId)
            itemBinding.itemCategoryContainer.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemBinding.root.context,
                    category.backgroundColorId
                )
            )
            itemBinding.itemCategoryContainer.setOnClickListener {
                onCategoryClick.invoke(category)
            }
        }

    }
}
