# WebView

## 앱에 WebView 추가

- WebView를 사용하려면 앱이 인터넷에 접속할 수 있어야 한다

```xml
<manifest ... >
  <uses-permission android:name="android.permission.INTERNET" />
  ...
</manifest>
```

### 액티비티 레이아웃에서 추가

```xml
<WebView
  android:id="@+id/webview"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
/>
```

```kotlin
val myWebView: WebView = findViewById(R.id.webview)
myWebView.loadUrl("http://www.example.com")
```

### onCreate()에서 추가

#### loadUrl()

```kotlin
val myWebView = WebView(activityContext)
setContentView(myWebView)
myWebView.loadUrl("http://www.example.com")
```

#### loadData()

```kotlin
// Create an unencoded HTML string
// then convert the unencoded HTML string into bytes, encode
// it with Base64, and load the data.
val unencodedHtml =
	"&lt;html&gt;&lt;body&gt;'%23' is the percent code for ‘#‘ &lt;/body&gt;&lt;/html&gt;"
val encodedHtml = Base64.encodeToString(unencodedHtml.toByteArray(), Base64.NO_PADDING)
myWebView.loadData(encodedHtml, "text/html", "base64")
```



## WebView에서 자바스크립트 사용

### 자바스크립트 사용 설정

- WebView에 로드하려는 웹페이지가 자바스크립트를 사용하는 경우,  자바스크립트를 사용하도록 설정해야 한다

```kotlin
val myWebView: WebView = findViewById(R.id.webview)
myWebView.settings.javaScriptEnabled = true
```

### 자바스크립트 코드를 Android에 결합

#### 인터페이스 정의

- 자바스크립트 사용이 설정되면 앱 코드와 자바스크립트 코드 간에 인터페이스를 만들 수 있다

```kotlin
/** Instantiate the interface and set the context  */
class WebAppInterface(private val mContext: Context) {

  /** Show a toast from the web page  */
  @JavascriptInterface
  fun showToast(toast: String) {
    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
  }
}
```

#### 인터페이스 결합

- addJavascriptInterface()를 호출하여 자바스크립트와 Android 간의 인터페이스를 결합한다
- 자바스크립트에 결합할 클래스 인스턴스와 자바스크립트가 클래스 액세스를 위해 호출할 수 있는 인터페이스 이름을 전달한다

```kotlin
val webView: WebView = findViewById(R.id.webview)
webView.addJavascriptInterface(WebAppInterface(this), "Android")
```

#### 자바스크립트

```html
<input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />

<script type="text/javascript">
  function showAndroidToast(toast) {
    Android.showToast(toast);
  }
</script>
```

### 앱 예시 코드

#### MainActivity.kt

```kotlin
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(WebAppInterface(this),  "Android")
        webView.loadUrl("file:///android_asset/index.html")
    }
}
```

#### WebAppInterface.kt

```kotlin
import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast

class WebAppInterface(
    private val context: Context
) {

    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
```

#### activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### index.html

```html
<html>
  <script type="text/javascript">
    function showAndroidToast(toast) {
      Android.showToast(toast);
    }
  </script>
  <body>
    <input type="button" value="자바스크립트" onclick="showAndroidToast('Hello Android!')">
  </body>
</html>
```



## 페이지 탐색 처리

- 기본적으로 사용자가 WebView에서 웹페이지의 링크를 클릭하면 URL을 처리하는 앱이 Android에서 실행된다

### WebViewClient

- WebViewClient를 이용하여 링크가 WebView에서 열리도록 재정의할 수 있다(웹페이지 탐색 가능)

#### WebViewClient 정의

- shouldOverrideUrlLoading()을 사용하여 WebView에서 로드할지 URL을 처리하는 다른 앱에서 로드할지 결정할 수 있다

```kotlin
private class MyWebViewClient : WebViewClient() {

  override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
    if (Uri.parse(url).host == "www.example.com") {
      // This is my web site, so do not override; let my WebView load the page
      return false
    }
    // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
      startActivity(this)
    }
    return true
  }
}
```

#### WebViewClient 설정

```kotlin
val myWebView: WebView = findViewById(R.id.webview)
myWebView.webViewClient = MyWebViewClient()
```

### 웹페이지 방문 기록 탐색

- WebViewClient를 설정하여 WebView에서 웹페이지 탐색이 가능하게 되면 웹페이지 방문 기록이 누적된다
- goForward(), goBack()를 이용하여 웹페이지를 앞뒤로 탐색한다
- canGoForward(), canGoBack()를 이용하여 웹페이지 방문 기록이 있는지 확인한다

```kotlin
override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
  // Check if the key event was the Back button and if there's history
  if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
    myWebView.goBack()
    return true
  }
  // If it wasn't the Back key or there's no web page history, bubble up to the default
  // system behavior (probably exit the activity)
  return super.onKeyDown(keyCode, event)
}
```

