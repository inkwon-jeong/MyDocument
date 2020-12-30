# FCM

## Firebase Cloud Messaging

https://firebase.google.com/docs/cloud-messaging?authuser=0

- 무료로 메시지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션
- 주요 기능
  - 알림 메시지 또는 데이터 메시지 전송 : 알림 메시지 또는 데이터 메시지를 전송한다
  - 다양한 메시지 타겟팅 : 단일 기기, 기기 그룹, 주제를 구독한 기기 등 3가지 방식으로 메시지를 전송한다
  - 클라이언트 앱에서 메시지 전송: 기기에서 다시 서버로 확인, 채팅, 기타 메시지를 보낼 수 있다
- 주요 구성요소
  - Firebase용 Cloud Functions 또는 앱 서버와 같이 메시지를 작성, 타겟팅, 전송할 수 있는 신뢰할 수 있는 환경
  - 해당 플랫폼별 전송 서비스를 통해 메시지를 수신하는 iOS, Android 또는 웹(자바스크립트) 클라이언트 앱
- 구현 방법
  1. FCM SDK 설정 : 플랫폼에 맞는 설정 안내에 따라 앱에서 Firebase 및 FCM을 설정한다
  2. 클라이언트 앱 개발 : 클라이언트 앱에 메시지 처리, 주제 구독 로직 또는 기타 선택사항 기능을 추가한다
  3. 앱 서버 개발 : Firebase Admin SDK 또는 서버 프로토콜 중 하나를 결정한 후 로직을 구축한다



## FCM 아키텍처

https://firebase.google.com/docs/cloud-messaging/fcm-architecture?authuser=0

![fcm_architecture](./image/fcm_architecture.png)

1. 메시지 요청을 작성하거나 구현하는 도구(알림 작성기, Firebase Admin SDK 또는 FCM 서버 프로토콜)
2. FCM 백엔드는 메시지 요청을 수락하고, 주제를 통해 메시지 팬아웃을 수행하고, 메시지 ID와 같은 메시지 메타데이터를 생성한다
3. 기기로 타겟팅된 메시지를 라우팅하고, 메시지 전송을 처리하고, 필요한 경우 플랫폼별 구성을 적용하는 플랫폼 수준의 전송 레이어
4. 알림이 표시되거나 앱의 포그라운드/백그라운드 상태 및 관련 애플리케이션 로직에 따라 메시지가 처리되는 사용자 기기의 FCM SDK



## FCM 수명주기 흐름

1. 클라이언트 앱의 인스턴스가 메시지를 받을 수 있게 기기를 등록하여 앱 인스턴스를 고유하게 식별하는 등록 토큰을 받는다
2. 토큰 또는 주제로 메시지를 받을 기기를 타켓팅하고 메시지를 작성한다
3. 앱 서버가 클라이언트 앱에 메시지를 전송한다



## FCM 메시지

https://firebase.google.com/docs/cloud-messaging/concept-options?authuser=0

### 알림 메시지

- FCM이 클라이언트 앱을 대신하여 기기에 자동으로 메시지를 표시한다
- 사용자에게 표시되는 사전 정의된 키 모음 및 커스텀 키-값 쌍의 데이터 페이로드(선택사항)가 포함

```json
{
  "message":{
    "token":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
    "notification":{
      "title":"Portugal vs. Denmark",
      "body":"great match!"
    }
    "data":{
      "Nick" : "Mario",
      "body" : "great match!",
      "Room" : "PortugalVSDenmark"
    }
  }
}
```

### 데이터 메시지

- 클라이언트 앱이 데이터 메시지 처리를 담당한다
- 데이터 메시지에는 예약 키 이름 없이 커스텀 키-값 쌍만 있다

```json
{
  "message":{
    "token":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
    "data":{
      "Nick" : "Mario",
      "body" : "great match!",
      "Room" : "PortugalVSDenmark"
    }
  }
}
```



## FCM 클라이언트 앱 설정(Android)

https://firebase.google.com/docs/cloud-messaging/android/client?authuser=0

### SDK 설정

- Firebase 프로젝트 만들기
- Firebase에 앱 등록
- Firebase 구성 파일 추가
- 앱에 Firebase SDK 추가

### 앱 매니페스트 설정

```xml
<service
    android:name=".java.MyFirebaseMessagingService" <--FirebaseMessagingService를 구현한 클래스 패키지 경로-->
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

### 기기 등록 토큰

- FCM SDK는 앱을 처음 시작할 때 클라이언트 앱 인스턴스용 등록 토큰을 생성한다
- 등록 토큰이 변경되는 경우
  - 새 기기에서 앱 복원
  - 사용자가 앱 삭제/재설치
  - 사용자가 앱 데이터 삭제

#### 현재 등록 토큰 가져오기

```kotlin
FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
    if (!task.isSuccessful) {
        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
        return@OnCompleteListener
    }

    // Get new FCM registration token
    val token = task.result

    // Log and toast
    val msg = getString(R.string.msg_token_fmt, token)
    Log.d(TAG, msg)
    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
})
```

#### 토큰 생성 모니터링

```kotlin
/**
 * Called if the FCM registration token is updated. This may occur if the security of
 * the previous token had been compromised. Note that this is called when the
 * FCM registration token is initially generated so this is where you would retrieve the token.
 */
override fun onNewToken(token: String) {
    Log.d(TAG, "Refreshed token: $token")

    // If you want to send messages to this application instance or
    // manage this apps subscriptions on the server side, send the
    // FCM registration token to your app server.
    sendRegistrationToServer(token)
}
```



## 앱에서 메시지 수신(Android)

https://firebase.google.com/docs/cloud-messaging/android/receive?authuser=0

### 메시지 처리

| 앱 상태    | 알림메시지(알림만) | 데이터메시지      | 알림메시지(알림 & 데이터                              |
| ---------- | ------------------ | ----------------- | ----------------------------------------------------- |
| 포그라운드 | OnMessageReceived  | OnMessageReceived | OnMessageReceived                                     |
| 백그라운드 | 작업 표시줄        | OnMessageReceived | 알림: 작업 표시줄<br />데이터: 런처 액티비티의 인텐트 |

### onMessageReceived 재정의

- 모든 메시지는 수신된 지 20초(마쉬멜로우의 경우 10초) 이내에 처리되어야 한다
- 긴 작업이 요구될 때는 Worker를 이용해야 한다
- 알림 페이로드는 RemoteMessage의 notification(객체), 데이터 페이로드는 RemoteMessage의 data(맵)이다

```kotlin
override fun onMessageReceived(remoteMessage: RemoteMessage) {
    // ...

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: ${remoteMessage.from}")

    // Check if message contains a data payload.
    if (remoteMessage.data.isNotEmpty()) {
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")

        if (/* Check if data needs to be processed by long running job */ true) {
            // For long-running tasks (10 seconds or more) use WorkManager.
            scheduleJob()
        } else {
            // Handle message within 10 seconds
            handleNow()
        }
    }

    // Check if message contains a notification payload.
    remoteMessage.notification?.let {
        Log.d(TAG, "Message Notification Body: ${it.body}")
    }

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
}
```



## 앱에서 주제 구독(Android)

- 클라이언트 앱에서 기존 주제를 구독하거나 새 주제를 만들 수 있다

```kotlin
Firebase.messaging.subscribeToTopic("weather")
        .addOnCompleteListener { task ->
            var msg = getString(R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = getString(R.string.msg_subscribe_failed)
            }
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }
```



## FCM 서버 환경 설정(Node.js)

### SDK 추가

```bash
$ npm init # package.json 파일이 없을 때
$ npm install firebase-admin --save
```

```javascript
var admin = require('firebase-admin');
```

### SDK 초기화

- 서비스 계정의 비공개 키 파일 생성

  1. Firebase Console에서 **설정 > 서비스 계정**을 연다
  2. **새 비공개 키 생성**을 클릭한 다음 **키 생성**을 클릭하여 확인한다
  3. 키가 들어 있는 JSON 파일을 안전하게 저장한다

- 애플리케이션에 사용자 인증 정보 제공

  - GOOGLE_APPLICATION_CREDENTIALS 환경 변수를 설정한다

  ```shell
  export GOOGLE_APPLICATION_CREDENTIALS="비공개 키 파일 경로"
  ```

  ```javascript
  admin.initializeApp({
    credential: admin.credential.applicationDefault()
  });
  ```

  - 또는 코드에서 서비스 계정 키 경로 명시적으로 전달한다

  ```javascript
  let serAccount = require("비공개 키 파일 경로")
  admin.initializeApp({
    credential: admin.credential.cert(serAccount),
  })
  ```

  

  ## FCM 서버 메시지 전송(Node.js)

  ### 특정 기기에 메시지 전송

  ```javascript
  // This registration token comes from the client FCM SDKs.
  var registrationToken = 'YOUR_REGISTRATION_TOKEN';
  
  var message = {
    data: {score: '850', time: '2:45'},
    token: registrationToken
  };
  
  // Send a message to the device corresponding to the provided
  // registration token.
  admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
  ```

  ### 여러 기기에 메시지 전송

  ```javascript
  // Create a list containing up to 500 registration tokens.
  // These registration tokens come from the client FCM SDKs.
  const registrationTokens = [
    'YOUR_REGISTRATION_TOKEN_1',
    // …
    'YOUR_REGISTRATION_TOKEN_N',
  ];
  
  var message = {
    data: {score: '850', time: '2:45'},
    tokens: registrationTokens,
  };
  
  admin.messaging().sendMulticast(message)
    .then((response) => {
      console.log(response.successCount + ' messages were sent successfully');
  	};
  ```

  ### 주제로 메시지 전송

  ```javascript
  // The topic name can be optionally prefixed with "/topics/".
  var topic = 'highScores';
  
  var message = {
    data: {score: '850', time: '2:45'},
    topic: topic
  };
  
  // Send a message to devices subscribed to the provided topic.
  admin.messaging().send(message)
    .then((response) => {
      // Response is a message ID string.
      console.log('Successfully sent message:', response);
    })
    .catch((error) => {
      console.log('Error sending message:', error);
    });
  ```

  

  ## FCM 서버 주제 관리

  - 등록 토큰을 알고 있으면 서버 로직을 사용하여 클라이언트 앱 인스턴스를 일괄 구독하거나 구독 취소할 수 있다

  ```kotlin
  // These registration tokens come from the client FCM SDKs.
  var registrationTokens = [
    'YOUR_REGISTRATION_TOKEN_1',
    // ...
    'YOUR_REGISTRATION_TOKEN_n'
  ];
  
  // Subscribe the devices corresponding to the registration tokens to the
  // topic.
  admin.messaging().subscribeToTopic(registrationTokens, topic)
    .then(function(response) {
      // See the MessagingTopicManagementResponse reference documentation
      // for the contents of response.
      console.log('Successfully subscribed to topic:', response);
    })
    .catch(function(error) {
      console.log('Error subscribing to topic:', error);
    });
  
  // Unsubscribe the devices corresponding to the registration tokens from
  // the topic.
  admin.messaging().unsubscribeFromTopic(registrationTokens, topic)
    .then(function(response) {
      // See the MessagingTopicManagementResponse reference documentation
      // for the contents of response.
      console.log('Successfully unsubscribed from topic:', response);
    })
    .catch(function(error) {
      console.log('Error unsubscribing from topic:', error);
    });
  ```

  

  ## FCM 예제

  - Node.js로 로컬 서버를 구축한다
  - 앱에서 토큰을 서버로 전송한다
  - 서버에서 토큰을 이용하여 특정 기기에 메시지를 전송한다

  

  ### 서버

  ```javascript
  // FCM
  const admin = require('firebase-admin')
  
  const serverKeyPath = './fcmtest-4071d-firebase-adminsdk-yfu5b-e27b312dc1.json'
  let serAccount = require(serverKeyPath)
  
  admin.initializeApp({
    credential: admin.credential.cert(serAccount),
  })
  
  // Server
  const express = require("express")
  const server = express()
  
  // 앱에서 전송한 토큰을 받아 파일에 기록한다
  server.post('/', (req, res) => {
    let token = req.query.token;
    writeFile(token);
    res.end();
  })
  
  // 파일에 기록된 토큰을 가지고 메시지를 전송한다
  server.get('/push', (req, res) => {
    readFile();
    res.end();
  })
  
  server.listen(3000, () => {
    console.log("The server is running on Port 3000")
  });
  
  function pushMessage(token) {
    let fcm_message = {
      // 알림 페이로드
      notification: {
        title: '테스트 메시지 전송',
        body: '테스트 메시지 전송 성공'
      },
      // 데이터 페이로드
      data: {
        id: '1223',
        contents: 'SNP Lab'
      },
      // 토큰
      token: token
    };
  
    // 토큰을 가지고 특정 기기에 메시지를 전송한다
    admin.messaging().send(fcm_message)
      .then(function (response) {
        console.log('보내기 성공 메시지:' + response);
      })
      .catch(function (error) {
        console.log('보내기 실패 메시지:' + error);
      });
  }
  
  // File
  const fs = require('fs')
  const fileName = 'FCM_TEST/token.txt'
  
  function openFile(flag, block) {
    console.log('open file');
    fs.open(fileName, flag, function (err, fd) {
      if (err) throw err;
      console.log('file open complete');
      block();
      fs.close(fd, function () {
        console.log('close file');
      });
    });
  }
  
  function readFile() {
    openFile('a+', function () {
      fs.readFile(fileName, 'utf8', function (err, data) {
        console.log('read token: ' + data);
        pushMessage(data);
      });
    });
  }
  
  function writeFile(token) {
    openFile('w+', function () {
      fs.writeFile(fileName, token, 'utf8', function (err) {
        if (err) throw err;
        console.log('write token: ' + token);
      })
    });
  }
  ```

  

  ### 클라이언트 앱

  ```kotlin
  class MyFirebaseMessagingService : FirebaseMessagingService() {
    ...
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
      // 메시지를 수신하여 작업을 처리한다
      ...
    }
    
    override fun onNewToken(token: String) {
    	Log.d(TAG, "Refreshed token: $token")
  
      // 앱에서 토큰을 서버로 전송한다
      sendRegistrationToServer(token)
    }
    
    private fun sendRegistrationToServer(token: String?) {
    	Log.d(TAG, "sendRegistrationTokenToServer($token)")
  
    	var connection: HttpURLConnection? = null
  
    	try {
      	val url = URL("$BASE_URL?token=$token")
  
      	connection = url.openConnection() as HttpURLConnection
       	connection.requestMethod = "POST" // URL 요청에 대한 메소드 설정 : POST.
       	connection.setRequestProperty("Accept-Charset", "UTF-8") // Accept-Charset 설정.
      	connection.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=UTF-8")
  
      	Log.d(TAG, "responseCode: ${connection.responseCode}")
      } catch (e: MalformedURLException) { // for URL.
      	e.printStackTrace()
      } catch (e: IOException) { // for openConnection().
      	e.printStackTrace()
      } finally {
      	connection?.disconnect()
      }
    }
    
    ...
  }
  ```

  

  

  