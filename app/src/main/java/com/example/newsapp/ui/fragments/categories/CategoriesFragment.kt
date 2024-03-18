package com.example.newsapp.ui.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.FragmentCategoriesBinding
import com.example.newsapp.ui.adapter.CategoriesAdapter
import com.example.newsapp.ui.model.Category

class CategoriesFragment(
    private val onCategoryClick: (Category) -> Unit,
    private val onResume: (category: String) -> Unit
) : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var category = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        categoriesAdapter = CategoriesAdapter(Category.categories, onCategoryClick)
        binding.categoriesRecyclerView.adapter = categoriesAdapter
    }

    override fun onResume() {
        super.onResume()
        onResume.invoke(category)
    }
}