# 안드로이드 4대 구성요소


## 액티비티
 - 사용자와 상호작용하기 위한 진입점
 - 사용자 인터페이스를 포함한 화면

### Manifest(Activity & Intent Filter)
 ```xml
 <activity android:name=".ExampleActivity" android:icon="@drawable/app_icon">
    <intent-filter>
        <action android:name="android.intent.action.SEND" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:mimeType="text/plain" />
    </intent-filter>
 </activity>
 ```

### 액티비티 수명주기
 1. onCreate()
  - 시스템이 액티비티를 생성될 때 실행(Created 상태)
  - 액티비티의 필수 구성요소 초기화(뷰 생성 및 데이터 바인딩)
  - setContentView() 호출

 2. onStart()
  - 액티비티가 Started 상태일 때 실행
  - 액티비티가 사용자에게 보이는 상태
  - 액티비티가 화면에 보일 때 실행할 기능 활성화

 3. onResume()
  - 액티비티가 Resumed 상태일 때 실행
  - 액티비티가 포커스를 얻은 상태
  - 앱이 사용자와 상호작용 가능(액티비티가 포그라운드에 있는 동안 실행해야 하는 기능 활성화)

 4. onPause()
  - 액티비티가 Paused 상태일 때 실행
  - 액티비티가 포커스를 잃은 상태
  - 액티비티가 포그라운드에 있는 동안 실행해야 하는 기능 정지(onResume()과 반대)
  - 사용자 데이터 저장, 네트워크 호출, 데이터베이스 트랜잭션 실행 X

 5. onStop()
  - 액티비티가 Stoped 상태일 때 실행
  - 액티비티가 화면에 보이지 않는 상태
  - 액티비티가 화면에 보이지 않을 때 실행할 필요가 없는 기능을 모두 정지(onStart()와 반대)
  - 사용자 데이터 저장, 네트워크 호출, 데이터베이스 트랜잭션 실행

 6. onDestroy()
  - 액티비티가 소멸되기 전에 실행
   - (사용자가 Activity를 완전히 닫거나 Activity에서 finish()가 호출되어) Activity가 종료되는 경우
   - 구성 변경(예: 기기 회전 또는 멀티 윈도우 모드)으로 인해 시스템이 일시적으로 Activity를 소멸시키는 경우
  - 이전의 콜백에서 아직 해제되지 않은 모든 리소스를 해제

 7. onSaveInstanceState()
  - 시스템이 구성변경 또는 메모리 확보를 위해 액티비티를 실행하는 프로세스를 종료할 때 실행
  - 기본적으로 레이아웃의 상태를 저장
  - 액티비티에서 복원해야하는 데이터(인스턴스)를 번들에 저장

 8. onRestoreInstanceState()
  - 액티비티가 이전에 소멸된 후 재생성될 때 실행(복원할 인스턴스가 있는 경우)
  - 시스템이 액티비티에 전달하는 번들로부터 인스턴스 상태 복원

### 프래그먼트
 - 어떤 동작 또는 사용자 인터페이스의 일부
 - 여러 개의 프래그먼트를 하나의 액티비티에 결합 또는 하나의 프래그먼트를 여러 액티비티에서 재사용
 - 자체 수명주기가 있고 다른 액티비티에서 재사용할 수 있는 하위 액티비티
  1. onAttach()
   - 프래그먼트가 액티비티와 연결될 때 실행
   - 컨텍스트(액티비티)가 전달됨

  2. onCreate()
   - 액티비티의 onCreate()와 유사
   - 하지만 UI 초기화는 다음 콜백에서 실행

  3. onCreateView()
   - 프래그먼트와 연결된 뷰 계층을 생성하기 위해 실행
   - 레이아웃을 인플레이트하여 뷰 객체 반환(인플레이트 : xml에 정의된 뷰를 실제 뷰 객체로 만든다)

  4. onActivityCreated()
   - 액티비티에서 onCreate()가 실행된 후 실행
   - 액티비티와 프래그먼트의 뷰 모두 생성된 상태

  5. onStart()
   - 액티비티의 onStart()와 유사

  6. onResume()
   - 액티비티의 onResume()와 유사

  7. onPause()
   - 액티비티의 onPause()와 유사

  8. onStop()
   - 액티비티의 onStop()와 유사

  9. onDestroyView()
   - 프래그먼트와 연결된 뷰 계층이 제거될 때 실행

  10. onDestroy()
   - 액티비티의 onDestroy()와 유사

  11. onDetach()
   - 프래그먼트가 액티비티와 연결이 끊어지는 중일 때 실행

### 태스크 및 백 스택
 - 프로세스 : 시스템은 앱의 컴포넌트를 실행하는 시점에서 컴포넌트가 메모리로 로드되고 컴포넌트가 속한 앱의 프로세스가 구동된다
 - 태스크 : 각 앱마다 현재 사용하고 있는 컴포넌트들을 그룹화(스택)하여 관리한다(컴포넌트가 해당 앱에 속할 수도 다른 앱에 속할 수도 있다)

 - <activity> taskAffinity
  - 액티비티가 어느 태스크에 속할지 결정한다
  - 기본적으로 동일한 앱의 모든 액티비티는 같은 어피니티를 가지고 있다

 - <activity> launchMode 속성
  1. standard(기본 모드)
   - 태스크에 액티비티의 새 인스턴스를 생성하고 인텐트를 새 인스턴스로 라우팅한다
   - 액티비티는 여러번 인스턴스화될 수 있고 각 인스턴스는 서로 다른 작업에 속할 수 있으며 한 작업에 여러 인스턴스가 있을 수 있다

  2. singleTop
   - 액티비티의 인스턴스가 현재 태스크의 맨 위에 있으면 시스템은 onNewIntent()를 호출하여 인텐트를 기존 인스턴스로 라우팅한다
   - 액티비티는 여러번 인스턴스화될 수 있고 각 인스턴스는 서로 다른 작업에 속할 수 있으며 한 작업에 여러 인스턴스가 있을 수 있다

  3. singleTask
   - 시스템이 새 태스크를 생성하고 루트에 액티비티를 인스턴스화한다
   - 액티비티의 인스턴스가 다른 태스크에 있다면 시스템은 onNewIntent()를 호출하여 인텐트를 기존 인스턴스로 라우팅한다

  4. singleInstance
   - 시스템이 인스턴스를 보유한 태스크로는 다른 액티비티를 실행하지 않는다
   - 액티비티는 해당 태스크의 유일한 멤버이다

 - 인텐트 플래그 속성
  1. FLAG_ACTIVITY_NEW_TASK
   - singleTask와 동일한 동작

  2. FLAG_ACTIVITY_SINGLE_TOP
   - singleTop과 동일한 동작

  3. FLAG_ACTIVITY_CLEAR_TOP
   - 인스턴스가 현재 태스크에 있다면 해당 인스턴스 위에 있는 다른 인스턴스들이 제거되고 인텐트가 onNewIntent()를 호출하여 기존 인스턴스로 라우팅한다

### 프로세스 중요도
 - 모든 Android 앱은 자체 Linux 프로세스에서 실행된다
 - 애플리케이션 프로세스의 수명 주기가 앱 자체에 의해 직접 제어되지 않는다
 - 시스템이 실행중인 앱 요소, 요소들의 중요도 및 시스템의 전체 메모리 양을 고려하여 시스템에 의해 결정한다

 - 중요도 계층 구도
  1. 포그라운드 프로세스(Foreground Process)
   - 사용자가 현재 하고 있는 태스크에 필요한 프로세스
   - ex) 프로세스가 사용자가 상호작용하고 있는 화면 상단에서 액티비티를 실행한다(onResume() 호출)
   - ex) 프로세스에 현재 실행 중인 브로드캐스트 리시버가 있다(BroadcastReceiver.onReceive() 메서드가 실행 중임)
   - ex) 프로세스에 콜백(Service.onCreate(), Service.onStart() 또는 Service.onDestroy()) 중 하나에서 현재 코드를 실행 중인 서비스가 있다

  2. 가시적 프로세스(Visible Process)
   - 사용자가 현재 알고 있는 태스크에 필요한 프로세스
   - ex) 프로세스가 화면상으로는 사용자에게 표시되지만 포그라운드에 있지 않은 액티비티를 실행한다(onPause() 호출)
   - ex) 프로세스에 Service.startForeground()를 통해 포그라운드 서비스로 실행 중인 서비스가 있다

  3. 서비스 프로세스(Service Process)
   - startService()로 시작된 서비스를 유지하는 프로세스

  4. 캐시된 프로세스(Cached Process)
   - 현재 필요하지 않은 프로세스


## 서비스
 - 백그라운드에서 오래 실행되는 작업을 수행할 수 있는 구성 요소이며 사용자 인터페이스를 제공하지 않는다
 - 다른 앱의 구성 요소가 서비스를 시작할 수 있으며, 이는 사용자가 다른 앱으로 전환하더라도 백그라운드에서 계속해서 실행된다
 - 구성 요소를 서비스에 바인딩하여 서비스와 상호작용할 수 있으며, 심지어는 프로세스 간 통신(IPC)도 수행할 수 있다

 - 서비스의 세 가지 유형
  1. 포그라운드
   - 사용자에게 잘 보이는 작업 수행(알림을 표시)

  2. 백그라운드
   - 사용자에게 직접 보이지 않는 작업 수행

  3. 바인드
   - 앱 구성 요소가 bindService()를 호출하여 서비스와 바인딩한다
   - 클라이언트-서버 인터페이스를 제공하여 구성 요소가 서비스와 상호작용하게 하며, 결과를 받을 수 있게 한다
   - 위와 같은 작업을 여러 프로세스에 걸쳐 프로세스 간 통신(IPC)으로 수행할 수 있다

### Manifest(Service)
 ```xml
 <manifest ... >
  ...
  <application ... >
      <service android:name=".ExampleService" />
      ...
  </application>
 </manifest>
 ```

### 서비스의 생명주기
 1. onCreate()
  - 시스템은 서비스가 처음 생성되었을 때 호출
  - 서비스가 이미 실행중일 때 호출 X

 2. onStartCommand()
  - 시스템은 다른 구성요소가 서비스를 시작하도록 요청할 때(startService()) 호출한다
  - 서비스를 중단하려면 stopSelf() 또는 stopService()를 호출해야 한다

 3. onBind()
  - 시스템은 다른 구성 요소가 해당 서비스에 바인딩할 때(bindService()) 호출한다
  - 클라이언트가 서비스와 통신을 주고받기 위해 사용할 인터페이스(IBinder) 반환
  - 바인딩을 허용하지 않을 때 null 반환

 4. onUnbind()
  - 시스템은 다른 구성 요소가 해당 서비스에 바인딩을 해제할 때(unbindService()) 호출한다

 5. onRebind()
  - onUnbind() 호출된 이후에 다시 바인딩할 때 호출한다

 6. onDestroy()
  - 시스템은 서비스를 더 이상 사용하지 않고 소멸시킬 때 호출한다
  - 서비스에서 사용한 리소스를 정리한다

### Intent Service
 - Service의 하위 클래스로 Worker 스레드를 사용하여 모든 시작 요청을 처리하되 한 번에 하나씩 처리한다
 - onHandleIntent() 구현 : 각 시작 요청에 대해 인텐트를 수신해서 백그라운드 작업을 완료한다
 - 시작 요청이 모두 처리된 후 서비스를 중단하므로 개발자가 stopSelf()를 호출할 필요가 전혀 없다
 - onBind()의 기본 구현을 제공하여 null을 반환하도록 한다
 - onStartCommand()의 기본 구현을 제공하여 인텐트를 작업 큐로 보내고 onHandleIntent()를 호출한다

```java
public class HelloIntentService extends IntentService {

  /**
   * A constructor is required, and must call the super <code><a href="/reference/android/app/IntentService.html#IntentService(java.lang.String)">IntentService(String)</a></code>
   * constructor with a name for the worker thread.
   */
  public HelloIntentService() {
      super("HelloIntentService");
  }

  /**
   * The IntentService calls this method from the default worker thread with
   * the intent that started the service. When this method returns, IntentService
   * stops the service, as appropriate.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
      // Normally we would do some work here, like download a file.
      // For our sample, we just sleep for 5 seconds.
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e) {
          // Restore interrupt status.
          Thread.currentThread().interrupt();
      }
  }
}
```

### Service
 - 서비스가 멀티스레딩을 수행해야 하는 경우 Service 클래스를 확장하여 각 인텐트를 처리하게 할 수 있다

 - onStartCommand() 반환값
  1. START_NOT_STICKY
   - 시스템이 서비스를 onStartCommand() 반환 후에 중단시키면 서비스를 재생성하면 안 된다
   - 전달할 Pending Intent가 있는 경우에 예외
   - 앱이 완료되지 않은 모든 작업을 단순히 다시 시작할 수 있을 때 유용하다

  2. START_STICKY
   - 시스템이 onStartCommand() 반환 후에 서비스를 중단하면 서비스를 다시 생성하고 onStartCommand()를 호출하되 마지막 인텐트(null)는 전달하지 않는다
   - 서비스를 시작하기 위한 Pending Intent가 있는 경우에 그러한 인텐트를 전달한다

  3. START_REDELIVER_INTENT
   - 시스템이 onStartCommand() 반환 후에 서비스를 중단하는 경우 서비스를 다시 생성하고 이 서비스에 전달된 마지막 인텐트로 onStartCommand()를 호출한다

```java
public class HelloService extends Service {
  private Looper serviceLooper;
  private ServiceHandler serviceHandler;

  // Handler that receives messages from the thread
  private final class ServiceHandler extends Handler {
      public ServiceHandler(Looper looper) {
          super(looper);
      }
      @Override
      public void handleMessage(Message msg) {
          // Normally we would do some work here, like download a file.
          // For our sample, we just sleep for 5 seconds.
          try {
              Thread.sleep(5000);
          } catch (InterruptedException e) {
              // Restore interrupt status.
              Thread.currentThread().interrupt();
          }
          // Stop the service using the startId, so that we don't stop
          // the service in the middle of handling another job
          stopSelf(msg.arg1);
      }
  }

  @Override
  public void onCreate() {
    // Start up the thread running the service. Note that we create a
    // separate thread because the service normally runs in the process's
    // main thread, which we don't want to block. We also make it
    // background priority so CPU-intensive work doesn't disrupt our UI.
    HandlerThread thread = new HandlerThread("ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND);
    thread.start();

    // Get the HandlerThread's Looper and use it for our Handler
    serviceLooper = thread.getLooper();
    serviceHandler = new ServiceHandler(serviceLooper);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

      // For each start request, send a message to start a job and deliver the
      // start ID so we know which request we're stopping when we finish the job
      Message msg = serviceHandler.obtainMessage();
      msg.arg1 = startId;
      serviceHandler.sendMessage(msg);

      // If we get killed, after returning from here, restart
      return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
      // We don't provide binding, so return null
      return null;
  }

  @Override
  public void onDestroy() {
    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
  }
}
```

### Binding
 1. Binder Class 확장
  - 서비스가 앱 전용이고 클라이언트와 같은 과정으로 실행되는 경우
  - Binder 클래스를 확장하고 그 인스턴스를 onBind()에서 반환하는 방식

  - Service
```java
public class LocalService extends Service {
   // Binder given to clients
   private final IBinder binder = new LocalBinder();
   // Random number generator
   private final Random mGenerator = new Random();

   /**
    * Class used for the client Binder.  Because we know this service always
    * runs in the same process as its clients, we don't need to deal with IPC.
    */
   public class LocalBinder extends Binder {
       LocalService getService() {
           // Return this instance of LocalService so clients can call public methods
           return LocalService.this;
       }
   }

   @Override
   public IBinder onBind(Intent intent) {
       return binder;
   }

   /** method for clients */
   public int getRandomNumber() {
     return mGenerator.nextInt(100);
   }
}
```

  - Client
```java
public class BindingActivity extends Activity {
   LocalService mService;
   boolean mBound = false;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
   }

   @Override
   protected void onStart() {
       super.onStart();
       // Bind to LocalService
       Intent intent = new Intent(this, LocalService.class);
       bindService(intent, connection, Context.BIND_AUTO_CREATE);
   }

   @Override
   protected void onStop() {
       super.onStop();
       unbindService(connection);
       mBound = false;
   }

   /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
   public void onButtonClick(View v) {
       if (mBound) {
           // Call a method from the LocalService.
           // However, if this call were something that might hang, then this request should
           // occur in a separate thread to avoid slowing down the activity performance.
           int num = mService.getRandomNumber();
           Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
       }
   }

   /** Defines callbacks for service binding, passed to bindService() */
   private ServiceConnection connection = new ServiceConnection() {

       @Override
       public void onServiceConnected(ComponentName className,
               IBinder service) {
           // We've bound to LocalService, cast the IBinder and get LocalService instance
           LocalBinder binder = (LocalBinder) service;
           mService = binder.getService();
           mBound = true;
       }

       @Override
       public void onServiceDisconnected(ComponentName arg0) {
           mBound = false;
       }
   };
}
```

 2. Messenger 사용
  - 인터페이스가 여러 프로세스에서 작동해야 하는 경우
  - Messenger로 서비스에 대한 인터페이스를 생성
  - 서비스가 여러 가지 유형의 Message 객체에 응답하는 Handler를 정의
  - Handler가 Messenger의 기초가 되어 클라이언트와 IBinder를 공유하고 클라이언트는 Message 객체를 사용해 서비스에 명령을 보낼 수 있다
  - Messenger가 모든 요청을 단일 스레드로 큐에 저장(스레드 세이프)

  - Service
```java
public class MessengerService extends Service {
    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;

    /**
     * Handler of incoming messages from clients.
     */
    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }
}
```

  - Client
```java
public class MessengerService extends Service {
    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;

    /**
     * Handler of incoming messages from clients.
     */
    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler(this));
        return mMessenger.getBinder();
    }
}
```

 3. AIDL 사용
  - 객체를 운영체제가 이해할 수 있는 원시 유형으로 해체한 다음, 여러 프로세스에서 마샬링하여 IPC를 수행
  - 서비스는 스레드로부터 안전해야 하고 다중 스레딩 처리가 가능해야 한다


## 브로드캐스트 리시버
 - Android 앱은 Android 시스템 및 기타 Android 앱에서 브로드캐스트 메시지를 받거나 보낼 수 있다
 - 관심 있는 이벤트가 발생할 때 이러한 브로드캐스트가 전송된다

 - Manifest
```xml
<receiver android:name=".MyBroadcastReceiver"  android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.INPUT_METHOD_CHANGED" />
    </intent-filter>
</receiver>
```

```java
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
    }
}
```

 - Context
```java
BroadcastReceiver br = new MyBroadcastReceiver();

IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
this.registerReceiver(br, filter);
this.unregisterReceiver(br);
```

 - BroadcastReceiver의 상태(실행 중인지 아닌지 여부)는 포함된 프로세스의 상태에 영향을 준다
 - 현재 onReceive() 메서드의 코드를 실행 중일 때 포그라운드 프로세스로 간주된다(활성상태)
 - onReceive()에서 반환되면 BroadcastReceiver는 더 이상 활성 상태가 아니다
 - 활성 상태가 아닐시 시스템은 프로세스를 종료하여 메모리를 회수할 수 있으므로 백그라운드 스레드를 시작해서는 안 된다

 - 브로드캐스트 리시버에서 백그라운드 스레드 시작하려면 goAsync()를 호출하거나 JobScheduler를 사용하여 수신자의 JobService를 예약해야 한다
```java
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();
        Task asyncTask = new Task(pendingResult, intent);
        asyncTask.execute();
    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action: " + intent.getAction() + "\n");
            sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
            String log = sb.toString();
            Log.d(TAG, log);
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}
```

 - 브로드캐스트 전송 방법
  1. sendOrderedBroadcast(Intent, String)
   - 한 번에 하나의 수신자에 브로드캐스트를 전송한다
   - 결과를 다음 수신자로 전파하거나, 브로드캐스트를 완전히 중단하여 브로드캐스트가 다른 수신자로 전달되지 않게 할 수 있다
   - 수신자가 실행되는 순서는 일치하는 인텐트 필터의 android:priority 속성으로 제어한다

  2. sendBroadcast(Intent)
   - 모든 수신자에 브로드캐스트를 전송한다
   - 수신자가 다른 수신자의 결과를 읽거나, 브로드캐스트로부터 수신한 데이터를 전파하거나 브로드캐스트를 중단할 수 없다

  3. LocalBroadcastManager.sendBroadcast(Intent)
   - 발신자와 동일한 앱에 있는 수신자에 브로드캐스트를 전송한다

  - 권한으로 브로드캐스트 제한
   1. 권한을 사용하여 전송
    - 발신자
```java
sendBroadcast(new Intent("com.example.NOTIFY"), Manifest.permission.SEND_SMS);
```
    - 수신자
```xml
<uses-permission android:name="android.permission.SEND_SMS"/>
```
   2. 권한을 사용하여 수신
    - 수신자
```xml
<!-- Manifest -->
<receiver android:name=".MyBroadcastReceiver"
              android:permission="android.permission.SEND_SMS">
    <intent-filter>
        <action android:name="android.intent.action.AIRPLANE_MODE"/>
    </intent-filter>
</receiver>
```

```java
// Context
IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
registerReceiver(receiver, filter, Manifest.permission.SEND_SMS, null );
```

    - 발신자
```xml
<uses-permission android:name="android.permission.SEND_SMS"/>
```
