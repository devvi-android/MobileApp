package com.app.citycare.ui.News

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.app.citycare.R
import com.app.citycare.ui.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class NewsDetailFragment : Fragment() {

    private val args: NewsDetailFragmentArgs by navArgs()

    private lateinit var viewModel: NewsViewModel
    private lateinit var webView: WebView
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val article = args.article
        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.floating_action_button)

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl((article.url))
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully !", Snackbar.LENGTH_SHORT).show()
        }
    }
}