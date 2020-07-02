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

- 새로운 코루틴을 생성하고 Defered를 반환하는 메서드
- Defered는 Job을 확장한 인터페이스, 코루틴의 결과를 기다리도록 하는 await()를 가지고 있다

```kotlin
val deferred: Deferred<T> = async(coroutineContext) {
(1) ... code that returns a result of type T...
(2) return@async result
}
(3) ... other code ...
(4) val resultValue: T = deferred.await()
(5) ... use resultValue ...
```

1. async()가 즉시 Defered를 반환하고, (3)의 코드가 실행된다

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



## Part 4. Running coroutines sequentially or in parallel



## Part 5. Coroutine cancellation



## Part 6: Exception propagation



## Part 7: A small DSL for Android apps development



## Part 8: MVP and MVVM with Clean Architecture



## Part 9: Combining multiple tasks with the operators on collections



## Part 10: Handling callbacks



## Part 11: Channels



## Part 12: Testing



