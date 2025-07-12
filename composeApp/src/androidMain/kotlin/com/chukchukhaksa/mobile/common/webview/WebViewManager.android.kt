package com.chukchukhaksa.mobile.common.webview

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView as AndroidWebView
import android.webkit.WebViewClient

actual object WebViewManager {
  @Volatile
  private var singletonWebView: AndroidWebView? = null
  private var isInitialized = false
  private var hasLoadedInitialUrl = false

  @SuppressLint("SetJavaScriptEnabled")
  actual fun getSingletonWebView(): Any? {
    return singletonWebView
  }

  @SuppressLint("SetJavaScriptEnabled")
  fun initializeSingletonWebView(context: Context) {
    if (singletonWebView == null) {
      synchronized(this) {
        if (singletonWebView == null) {
          singletonWebView = AndroidWebView(context).apply {
            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              setSupportZoom(true)
              builtInZoomControls = true
              displayZoomControls = false
              loadWithOverviewMode = true
              useWideViewPort = true
              cacheMode = WebSettings.LOAD_DEFAULT
            }
            webViewClient = object : WebViewClient() {
              override fun onPageStarted(
                view: AndroidWebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
              ) {
                super.onPageStarted(view, url, favicon)
                // 글로벌 로딩 상태 업데이트 로직 필요시 추가
              }

              override fun onPageFinished(view: AndroidWebView?, url: String?) {
                super.onPageFinished(view, url)
                // 글로벌 상태 업데이트 로직 필요시 추가
              }
            }
          }
          isInitialized = true
        }
      }
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  actual fun createNewWebView(): Any {
    // Context는 Compose에서 받아오므로 여기서는 임시로 null 반환
    throw IllegalStateException("Use createNewWebView(context) instead")
  }

  @SuppressLint("SetJavaScriptEnabled")
  fun createNewWebView(context: Context): AndroidWebView {
    return AndroidWebView(context).apply {
      settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = true
        setSupportZoom(true)
        builtInZoomControls = true
        displayZoomControls = false
        loadWithOverviewMode = true
        useWideViewPort = true
        cacheMode = WebSettings.LOAD_DEFAULT
      }
    }
  }

  actual fun isSingletonInitialized(): Boolean {
    return isInitialized && singletonWebView != null
  }

  actual fun loadUrlIfNeeded(webView: Any, url: String) {
    if (webView is AndroidWebView) {
      if (webView == singletonWebView) {
        // 싱글톤 웹뷰인 경우
        if (!hasLoadedInitialUrl) {
          webView.loadUrl(url)
          hasLoadedInitialUrl = true
        }
        return
      }
      // 새로운 웹뷰인 경우 항상 로드
      webView.loadUrl(url)
    }
  }
}