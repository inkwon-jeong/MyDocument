

# 코루틴

## Part 1. Introduction

### 코틀린은 무엇인가?

- 코루틴은 비동기 프로그래밍을 간단하게 해주는 강력한 도구
- 순차적인 코드의 간단명료함 + 비동기 프로그래밍의 장점

### RxJava vs Coroutine

- RxJava는 스트림 처리에 좋지만, 스트림을 처리하듯이 모든 것을 작성해야 함으로 코드가 복잡해진다

- 코루틴은 순차적인 코드를 작성하기에 유연하며, 선호하는 방식으로 스트림을 처리할 수 있다

- RxJava는 결과를 처리하기 위한 콜백이 필요한 반면, 코루틴은 콜백이 필요없다

  

## Part 2. The basics

### CoroutineScope

- 기존 비동기 프로그래밍은 여러 독립적인 비동기 코드 블럭들로 구성된다
- CoroutineScope를 이용하여 코루틴들을 부모-자식 관계로 구성할 수 있다
- 독립적인 비동기 코드 블럭들은 일일이 취소해야 하는 반면, 코루틴은 취소하면 자식 코루틴도 모두 취소된다
- CoroutineScope는 대게 생명주기가 있는 객체(ViewModel, Presenter)에 의해 구현된다

```kotlin
class MyViewModel : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}
```

#### Dispatcher

- 코루틴이 어떤 스레드에서 실행될 지 결정하는 컴포넌트
- Main : UI 스레드
- IO : 백그라운드 스레드, 외부에서 디스크 또는 네트워크 I/O를 실행하도록 최적화되어 있는 스레드
- Default : 백그라운드 스레드, CPU를 많이 사용하는 작업을 실행하도록 최적화되어 있는 스레드
- Unconfined : 테스트용 스레드, 여러 스레드에 분산시키지 않고 같은 스레드에서 테스트하기 위한 스레드

#### Job

- 코루틴에 의해 처리되는 작업 단위
- Coroutine Builder에 의해 생성되고 코루틴의 상태를 추적하고 코루틴을 취소할 수 있다

### Coroutine Builders

- 새로운 코루틴을 생성하는 메서드
- 기본적으로 메서드를 호출한 CoroutineScope의 컨텍스트를 상속 받고, 인자로 받은 컨텍스트가 있다면 override한다

#### launch

- 새로운 코루틴을 생성하고 Job을 반환하는 메서드

```kotlin
val job: Job = launch(coroutineContext) {
(1) ... coroutine code ...
}
(2) ... other code ...
```

1. launch()가 즉시 Job을 반환하고, (2)의 코드가 실행된다
2. (1)과 (2)의 코드는 같은 시간에 병렬적으로 실행된다

#### async

- 새로운 코루틴을 생성하고 Deferred를 반환하는 메서드
- Deferred는 Job을 확장한 인터페이스, 코루틴의 결과를 기다리도록 하는 await()를 가지고 있다

```kotlin
val deferred: Deferred<T> = async(coroutineContext) {
(1) ... code that returns a result of type T...
(2) return@async result
}
(3) ... other code ...
(4) val resultValue: T = deferred.await()
(5) ... use resultValue ...
```

1. async()가 즉시 Deferred를 반환하고, (3)의 코드가 실행된다

2. (1)과 (3)의 코드가 같은 시간에 병렬적으로 실행된다

3. await()가 async()에 의해 시작된 코루틴의 끝나기를 기다리고 결과를 반환한다

4. (5)의 코드가 실행된다

#### withContext

- 부모의 컨텍스트를 인자로 받은 컨텍스트로 overriding하는 메서드
- 주로 스레드를 변경할 때 사용된다

```kotlin
// 기본 스레드 : Main
val resultValue: T = withContext(Dispatchers.IO) { // 블록 내에 있는 코드는 IO 스레드에서 실행된다
(1) ... code that returns a result of type T...
(2) return@withContext result
}
(3) ... other code that uses resultValue ... // Main 스레드에서 실행된다
```

1. (1)의 코드가 실행되고 결과가 반환된다
2. (3)의 코드가 실행된다

### *Suspend* Function

- suspend 함수는 코루틴이나 다른 suspend 함수 내에서만 사용된다
- suspend 함수의 결과가 반환될 때까지 해당 함수를 호출한 코루틴을 중지시킨다
- 코루틴을 중지시키는 것은 코루틴이 실행되는 스레드를 차단하는 것이 아니다(다른 스레드들이 해당 스레드를 사용할 수 있게 한다)



## Part 3. Coroutines in Android Studio

![coroutine_builder](/Users/jeong-inkwon/Documents/SNP Lap/tutorials/MyDocument/Coroutine/image/coroutine_builder.png)

- (1)의 아이콘은 suspend 함수 호출이 있는 라인을 나타낸다
- (2)의 코드 블럭은 CoroutineScope 내에서 실행된다

### What happens to the coroutine scope in this example?

- 클래스는 CoroutineScope 인터페이스를 구현하여, 클래스 내에 모든 것은 coroutine scope를 이용할 수 있다
- launch(), async(), withContext()는 새로운 context를 가진 scope를 가진다
- 모든 scope들은 부모-자식관계를 가진다(context의 Job을 기준으로)
- launch()
  - CoroutineContext : coroutineContext + Dispatchers.Main
  - 부모 context : coroutineContext
- withContext()
  - CoroutineContext : launch()의 context + Dispatchers.Default
  - 부모 context : launch()의 context
- async()
  - CoroutineContext : launch()의 context + Dispatchers.Default
  - 부모 context : launch()의 context

### Sequence of steps in the main Android UI thread

1. launch()는 메인 스레드에서 새 코루틴을 시작한다
2. withContext()는 launch() 코루틴을 중지시키고, 코드 블럭을 백그라운드 스레드에서 실행시킨다
   결과를 반환하고 중지한 코루틴을 재개한다
3. async()는 즉시 결과를 반환하고, 백그라운드 스레드에서 코드 블럭을 실행한다(launch()와 async() 병렬 실행)
4. await()는 launch() 코루틴을 중지시키고, async()가 완료되기를 기다린다
5. async()가 반환한 결과를 변수에 할당한고, launch() 코루틴을 재개한다



## Part 4. Running coroutines sequentially or in parallel

### Sequentially

- 순차 처리는 다른 작업을 실행하기 전에 실행중인 작업의 완료를 기다리는 것이다
- 주로 다른 작업의 결과를 필요로 할 때 사용된다 

```kotlin
val result1: Int = withContext(Dispatchers.Default) {
(1) // ... do something ...
    return@withContext value1
}
val result2: Int = withContext(Dispatchers.Default) {
(2) // ... do something with result1 ...
    return@withContext value2
}
... other code in the parent coroutine ...
```

- withContext()를 사용하면 순차적으로 작업을 처리하는데 용이하다
- withContext()는 suspend 함수여서, 결과를 반환하기까지 withContext()를 호출한 코루틴을 중지시킨다
- (1)의 코드가 실행되어 반환한 값을 result1에 할당하고, (2)의 코드가 실행되어 반환한 값을 result2에 할당한다

### Parallel

- 병렬 처리는 여러 CPU 코어를 사용하여 스레드들을 동시에 실행하는 것이다
- 다른 작업의 결과를 기다릴 필요가 없어서 더 빠른 처리능력을 가진다

```kotlin
val result1Deferred: Deferred<Int> = async(Dispatchers.Default) {
(1) // ... do something ...
    return@async value1
}
val result2Deferred: Deferred<Int> = async(Dispatchers.Default) {
(2) // ... do something ...
    return@async value2
}
val result1: Int = result1Deferred.await()
val result2: Int = result2Deferred.await()
... other code in the parent coroutine ...
```

- async()를 사용하면 병렬적으로 작업을 처리하는데 용이하다
- (1)의 코드와 (2)의 코드가 병렬적으로 실행된다
- await()는 async()의 결과를 기다리는 suspend 함수여서 순차적으로 처리된다 



## Part 5. Coroutine cancellation

- launch()는 Job, async()는 Deferred를 반환한다(Deferred는 Job을 확장한 객체)
- Job은 코루틴을 취소하는 cancel() 함수를 가진다

```kotlin
val job: Job = launch(...) { ... }
...
job.cancel()
val deferred: Deferred = async(...) { ... }
...
deferred.cancel()
```

### Cancellation Exception

- Canellation Exception은 cancel()을 호출했을 때 해당 코루틴뿐 아니라 모든 자식 코루틴을 취소시킬 수 있게 해준다
- Canellation Exception을 발생시키면, 다른 예외발생처럼 코드 실행에 인터럽트가 생긴다
- 다른 예외와 차이점은 개발자가 예외를 잡지 않으면 앱은 충돌하지 않는다

### Cancellation through CoroutineContext

```ko
coroutineContext.cancel()
coroutineContext.cancelChildren()
```

- cancel() 
  - 함수를 호출한 컨텍스트를 포함하여 모든 자식 코루틴의 Job을 취소시킨다
  - 더 이상 컨텍스트를 사용하여 코루틴을 실행할 수 없다
- cancelChildren() 
  - 함수를 호출한 컨텍스트를 제외하고 모든 자식 코루틴의 Job을 취소시킨다
  - 계속해서 컨텍스트를 사용하여 코루틴을 실행할 수 있다

### Cancellation points

- 코루틴의 중지시점이 Cancellation Exception을 발생시키는 시점이다
- 모든 suspend 함수는 Cancellation Exception을 발생시킬 후보자이다

```kotlin
val deferred: Deferred = async(...) { ... }
...
deferred.await() // <--- CancellationException can be thrown here
```

### Catching CancellationException

- Cancellation Exception을 처리하고 다시 예외를 발생시키지 않으면, 부모 코루틴의 취소가 중지된다

```kotlin
// 코루틴 취소 시 코드 블럭을 실행하고, 다시 예외를 발생시켜 부모 코루틴 취소를 진행한다
try {
    ...
} catch (e: CancellationException) {
    ... do something to handle the cancellation...
    throw e
}
```

```kotlin
// 다시 예외를 발생시키지 않아서 부모 코루틴 취소가 중지된다
try {
    ...
} catch (e: CancellationException) {
    ... do something to handle the cancellation...
}
```

```kotlin
// Cancellation Exception이라면 다시 예외를 발생시켜 부모 코루틴 취소를 진행한다
try {
    ...
} catch (e: Exception) {
    if (e is CancellationException) {
        throw e
    }
    ... handle other exceptions ...
}
```

### Cancellation is cooperative

- 기본적으로 코루틴 라이브러리에 있는 suspend 함수들은 cancellable이다
- 개발자가 직접 작성한 suspend 함수는 cancellable로 만들어야 한다

```kotlin
suspend fun execute(...) {
    ...
(1) aSuspendFunction()
    ...
(2) anotherSuspendFunction()
    ...
}
```

- (1), (2)의 함수가 모두 cancellable이므로 execute()도 cancellable이 된다

```kotlin
suspend fun execute(...) {
    while (isActive && ...) {
        ... long computation steps ...
    }
    
    if (!isActive) {
        throw CancellationException()
    }
    ... other code after the long computation ...
}
```

- isActive 프로퍼티를 이용하여 cancellable로 만든다

### Cancellation in Android

- Presenter와 ViewModel은 CoroutineScope 인터페이스를 구현한다
- Presenter와 ViewModel의 CoroutineScope를 취소하면 그들의 생명주기 안에서 실행된 모든 코루틴을 취소할 수 있다

#### Presenter in MVP

- Presenter와 연결된 액티비티가 onStop()을 호출할 때마다 모든 코루틴을 취소한다
- Presenter 인스턴스가 destroy되지 않을 경우 CoroutineScope를 재사용할 수 있게 cancelChildren()을 사용한다

``` kotlin
abstract class BasePresenter : CoroutineScope {
    ...
    fun cancelCoroutines() {
        coroutineContext.cancelChildren()
    }
}
```

#### ViewModel in MVVM

- ViewModel이 onCleared()를 호출할 때 모든 코루틴을 취소한다
- ViewModel은 콜백 메서드 호출 후 destroy되기 때문에 cancel()도 사용할 수 있다

```kotlin
abstract class BaseViewModel : ViewModel(), CoroutineScope {
    ...
    override fun onCleared() {
        coroutineContext.cancelChildren()
      	coroutineContext.cancel()
        super.onCleared()
    }
}
```



## Part 6: Exception propagation



## Part 7: A small DSL for Android apps development



## Part 8: MVP and MVVM with Clean Architecture



## Part 9: Combining multiple tasks with the operators on collections



## Part 10: Handling callbacks



## Part 11: Channels



## Part 12: Testing



