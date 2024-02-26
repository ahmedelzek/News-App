package com.example.newsapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityHomeBinding
import com.example.newsapp.ui.fragments.CategoriesFragment
import com.example.newsapp.ui.fragments.NewsFragment
import com.example.newsapp.ui.fragments.SettingsFragment


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var categoriesFragment: CategoriesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        loadFragment(categoriesFragment)
        onNavClicked()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
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

    private fun initFragments() {
        categoriesFragment = CategoriesFragment {
            binding.toolbarContent.toolbarText.text = it.title
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsFragment(it.id), "")
                .addToBackStack("")
                .commit()
        }
    }

}
