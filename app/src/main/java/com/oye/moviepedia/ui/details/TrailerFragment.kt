package com.oye.moviepedia.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.oye.moviepedia.databinding.FragmentTrailerBinding
import com.oye.moviepedia.ui.BaseFragment

class TrailerFragment: BaseFragment() {
    private lateinit var binding: FragmentTrailerBinding
    private val args: TrailerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUI() {
        val url = "https://www.youtube.com/embed/${args.trailerKey}?autoplay=1"

        binding.webView.webChromeClient = object : WebChromeClient() {}
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.mediaPlaybackRequiresUserGesture = false
        binding.webView.loadUrl(url)

        setupSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.setNavigationOnClickListener {
            onSupportNavigateUp()
        }
    }

}