# Dependency Injection



## Dependency가 무엇인가?

- B 클래스를 사용하는 A 클래스에 대해, A 클래스가 B 클래스를 의존한다고 말한다
- A 클래스를 dependent, B 클래스를 dependency라고 한다

```kotlin
class Car { // Dependant
    private val engine = Engine() // Dependency

    fun start() {
        engine.start()
    }
}
```



## Dependency Injection이 무엇인가?

- Dependency Injection(DI)는 의존성 문제를 해결하기 위한 방법이다
- DI는 내부가 아니라 외부에서 객체를 생성해서 넣어주는 것을 의미한다
- DI는 Inversion of Control 개념에 기반하여, dependant는 dependency의 구현에 독립적이다

```kotlin
class Car(private val engine: Engine) {
    fun start() {
        engine.start()
    }
}

fun main(args: Array) {
    val engine = Engine()
    val car = Car(engine) // Dependency Injection
    car.start()
}
```



## Dependency Injection의 장점

- 구성요소 간의 결합도를 낮춘다 
- 유연성과 확장성을 향상시킨다
- 재사용성을 높여준다
- 테스트에 용이하다
- 리팩토링에 용이하다



## Manual Dependency Injection

### Application Graph

![application_graph](./image/application_graph.png)

```kotlin
class UserRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) { ... }

class UserLocalDataSource { ... }
class UserRemoteDataSource(
    private val loginService: LoginRetrofitService
) { ... }
```

```kotlin
class LoginActivity: Activity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      
        val retrofit = Retrofit.Builder()
            .baseUrl("https://example.com")
            .build()
            .create(LoginService::class.java)

        val remoteDataSource = UserRemoteDataSource(retrofit)
        val localDataSource = UserLocalDataSource()

        val userRepository = UserRepository(localDataSource, remoteDataSource)

        loginViewModel = LoginViewModel(userRepository)
    }
}
```



### 컨테이너로 Dependency 관리

```kotlin
interface Factory {
    fun create(): T
}

class LoginViewModelFactory(private val userRepository: UserRepository) : Factory {
    override fun create(): LoginViewModel {
        return LoginViewModel(userRepository)
    }
}
```

```kotlin
class AppContainer {
    private val retrofit = Retrofit.Builder()
                            .baseUrl("https://example.com")
                            .build()
                            .create(LoginService::class.java)

    private val remoteDataSource = UserRemoteDataSource(retrofit)
    private val localDataSource = UserLocalDataSource()

    val userRepository = UserRepository(localDataSource, remoteDataSource)
    val loginViewModelFactory = LoginViewModelFactory(userRepository)
}
```

```kotlin
class MyApplication : Application() {
    val appContainer = AppContainer()
}    
```

```kotlin
class LoginActivity : Activity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as MyApplication).appContainer
        loginViewModel = appContainer.loginViewModelFactory.create()
    }
}
```



### 애플리케이션 흐름에서 Dependency 관리

```kotlin
class LoginContainer(val userRepository: UserRepository) {
    val loginData = LoginUserData()
    val loginViewModelFactory = LoginViewModelFactory(userRepository)
}

class AppContainer {
    ...
    val userRepository = UserRepository(localDataSource, remoteDataSource)
    var loginContainer: LoginContainer? = null
}
```

```kotlin
class LoginActivity: Activity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginData: LoginUserData
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = (application as MyApplication).appContainer
        appContainer.loginContainer = LoginContainer(appContainer.userRepository)

        loginViewModel = appContainer.loginContainer.loginViewModelFactory.create()
        loginData = appContainer.loginContainer.loginData
    }

    override fun onDestroy() {
        appContainer.loginContainer = null
        super.onDestroy()
    }
}
```



