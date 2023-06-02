package com.app.citycare.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.app.citycare.R
import com.app.citycare.db.news.ArticleDatabase
import com.app.citycare.repository.news.NewsRepository
import com.app.citycare.ui.Event.EventFragment
import com.app.citycare.ui.News.NewsViewModel
import com.app.citycare.ui.News.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        val navController = Navigation.findNavController(this, R.id.frameLayout)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

    }
}