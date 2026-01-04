package com.slimmy.portoapps.ui.portofolio

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.slimmy.portoapps.databinding.FragmentWebViewBinding

class AppWebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!
    private var originalUrl: String = "https://google.com"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        originalUrl = arguments?.getString("url") ?: "https://google.com"

        setupWebView(originalUrl)
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.btnCloseWebview.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRefreshWebview.setOnClickListener {
            // Logic diubah: Kembali ke URL asli yang ada di database
            binding.webview.loadUrl(originalUrl)
        }

        binding.btnOpenBrowser.setOnClickListener {
            val currentUrl = binding.webview.url ?: originalUrl
            currentUrl?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    private fun setupWebView(url: String) {
        binding.webview.apply {
            // Optimasi Settings
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                cacheMode = WebSettings.LOAD_DEFAULT
                databaseEnabled = true
                
                // Mencegah akses file yang tidak perlu untuk keamanan dan kecepatan
                allowFileAccess = false
                allowContentAccess = false
            }
            
            // Tambahkan Hardware Acceleration
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    if (_binding == null) return
                    super.onPageStarted(view, url, favicon)
                    binding.pbWebview.visibility = View.VISIBLE
                    binding.tvWebviewUrl.text = url
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (_binding == null) return
                    super.onPageFinished(view, url)
                    binding.pbWebview.visibility = View.GONE
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // Mencegah link eksternal (seperti playstore/wa) membuat crash jika tidak ada appsnya
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        return false
                    }
                    return try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    } catch (e: Exception) {
                        true
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    if (_binding == null) return
                    super.onReceivedTitle(view, title)
                    binding.tvWebviewTitle.text = title
                }

                override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                    if (_binding == null) return
                    super.onReceivedIcon(view, icon)
                    binding.ivFavicon.setImageBitmap(icon)
                    binding.ivFavicon.visibility = View.VISIBLE
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (_binding == null) return
                    super.onProgressChanged(view, newProgress)
                    binding.pbWebview.progress = newProgress
                }
            }

            loadUrl(url)
        }
    }

    override fun onPause() {
        super.onPause()
        binding.webview.onPause()
        binding.webview.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        binding.webview.onResume()
        binding.webview.resumeTimers()
    }

    override fun onDestroyView() {
        // Penting: Bersihkan WebView untuk mencegah Memory Leak
        binding.webview.apply {
            stopLoading()
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }
        super.onDestroyView()
        _binding = null
    }
}
