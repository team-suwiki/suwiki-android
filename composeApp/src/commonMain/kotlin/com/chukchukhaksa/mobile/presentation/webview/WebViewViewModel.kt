package com.chukchukhaksa.mobile.presentation.webview

import androidx.lifecycle.ViewModel
import com.chukchukhaksa.mobile.common.ui.mviStore

class WebViewViewModel : ViewModel() {
  val mviStore = mviStore<WebViewState, WebViewSideEffect>(WebViewState())

  fun updateTitle(title: String) {
    mviStore.setState { 
      copy(title = title)
    }
  }

  fun updateUrl(url: String) {
    mviStore.setState { 
      copy(url = url)
    }
  }

  fun setLoading(isLoading: Boolean) {
    mviStore.setState { 
      copy(isLoading = isLoading)
    }
  }
}