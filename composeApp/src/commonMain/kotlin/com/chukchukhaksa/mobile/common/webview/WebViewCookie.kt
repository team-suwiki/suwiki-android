package com.chukchukhaksa.mobile.common.webview

data class WebViewCookie(
  val name: String,
  val value: String,
  val domain: String,
  val path: String = "/",
  val isSecure: Boolean = false,
  val isHttpOnly: Boolean = false,
  val maxAge: Long? = null, // null이면 세션 쿠키
)