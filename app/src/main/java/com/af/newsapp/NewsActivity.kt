package com.af.newsapp

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.af.newsapp.databinding.ActivityNewsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(binding.flFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

//        val navHostFragment=supportFragmentManager
//            .findFragmentById(binding.flFragment.id) as NavHostFragment
//        val navController=navHostFragment?.navController
//        binding.bottomNavigationView.setupWithNavController(navController!!)
    }
}