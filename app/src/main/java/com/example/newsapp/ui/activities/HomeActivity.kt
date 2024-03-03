package com.example.newsapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityHomeBinding
import com.example.newsapp.ui.fragments.CategoriesFragment
import com.example.newsapp.ui.fragments.NewsFragment
import com.example.newsapp.ui.fragments.SettingsFragment
import com.example.newsapp.ui.fragments.search.SearchFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val categoriesFragment = CategoriesFragment({
        loadFragment(NewsFragment(it) {
            initNewsAppBar(it.id)
        })
    }, { initCategoryAppBar() })

    private val searchFragment = SearchFragment {
        binding.appBar.root.isVisible = true
        supportFragmentManager.popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(categoriesFragment)
        onNavClicked()
    }


    private fun onNavClicked() {
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                }

                R.id.categories -> {
                    loadFragment(categoriesFragment)
                }
            }
            binding.root.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

    }

    private fun initCategoryAppBar() {
        binding.appBar.apply {
            root.isVisible = true
            toolbar.appTitle.text = getText(R.string.news_app)
            toolbar.appTitle.isVisible = true
            toolbar.menuIcon.isVisible = true
            toolbar.searchIcon.isVisible = false
            toolbar.menuIcon.setOnClickListener {
                binding.root.open()
            }
        }
    }

    private fun initNewsAppBar(category: String) {
        binding.appBar.apply {
            toolbar.appTitle.text = getText(R.string.news_app)
            toolbar.appTitle.isVisible = true
            toolbar.appTitle.text = category
            toolbar.menuIcon.isVisible = true
            toolbar.searchIcon.isVisible = true
            toolbar.menuIcon.setOnClickListener {
                binding.root.open()
            }
            toolbar.searchIcon.setOnClickListener {
                onSearchIconClickListener()
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("")
            .commit()
    }

    private fun onSearchIconClickListener() {
        loadFragment(searchFragment)
        binding.appBar.root.isVisible = false
    }
}

