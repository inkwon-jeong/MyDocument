# 코틀린

## 코틀린의 주요 특성
 1. 자바 100% 호환
    - 코틀린으로 개발된 코드가 자바 클래스로 빌드되어 JVM(자바가상머신)에서 동작하기 때문에 호환이 가능하다

 2. 자바스크립트 지원
    - 코틀린은 자바스크립트로 컴파일할 수 있으며, 웹 애플리케이션 분야에도 사용될 수 있다

 3. 정적 타입 지정 언어
    - 정적 타입 지정 : 모든 프로그램의 구성 요소의 타입을 컴파일 시점에 알 수 있고, 컴파일러가 타입을 검증해준다
    - 장점
        - 성능 : 실행 시점에 어떤 메소드를 호출할지 알아내는 과정이 없어 메소드 호출이 더 빠르다
        - 신뢰성 : 컴파일러가 프로그램의 정확성을 검증한다(타입 검증)
        - 유지보수성 : 코드에서 다루는 객체가 어떤 타입인지 알 수 있어서 다루기 쉽다
        - 도구 지원 : 더 안전하게 리팩토링할 수 있고, 도구는 더 정확한 코드 완성 기능을 제공할 수 있다

## 자바 vs 코틀린
 1. Null 처리
    - 코틀린은 null이 될 수 있는 타입과 null이 될 수 없는 타입을 제공한다
    - 이로 인해 코틀린은 null-safe한 코드를 작성할 수 있고, NullPointerException을 줄여준다
```kotlin
fun main(args: Array<String>) {
    val str1: String = "Hello"
	val str2: String? = null

    print(str1) // result : "Hello"
    print(str2) // result : "Hi"
}

fun print(str: String?) {
    println(str ?: "Hi")
}
```

 2. 프로퍼티와 생성자 파라미터
  - 프로퍼티 : 필드와 접근자 메서드(getter, setter)를 통칭하는 것
  - 프로퍼티와 클래스 파라미터를 이용하면 주 생성자가 만들어진다
```kotlin
// 코틀린
class Car(
     var color: Int, // var 프로퍼티는 getter, setter를 다 갖고 있다
     val type : String // val 프로퍼티는 getter만 갖고 있다
)
```
```java
// 자바
public class Car {
    private int color;
    private String type;

    public Car(int color, String type) {
        this.color = color;
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }
}
```

 3. 확장함수
    - 코틀린은 상속이 아닌 함수 확장을 통해 기존 클래스의 기능을 쉽게 추가할 수 있다
```kotlin
fun main(args: Array<String>) {
    println(1.isOdd()) // result : true
    println(2.isOdd()) // result : false
}
fun Int.isOdd() : Boolean = this % 2 != 0
```

 4. 데이터 클래스
    - 코틀린은 데이터 클래스를 제공하여 POJO를 쉽게 생성할 수 있다
    - 특성
        - 인스턴스간 비교를 위한 equals() 자동생성
        - 해시 기반 자료구조에서 키로 사용할 수 있도록 hashCode() 자동 생성
        - 프로퍼티 순서대로 값을 반환해 주는 toString() 자동생성
        - 데이터 클래스를 깊은 복사해주는 copy() 자동생성
    - 제약사항
        - 기본 생성자에는 최소 하나의 파라미터가 있어야 한다
        - 기본 생성자의 파라미터는 val 또는 var이어야 한다
        - 데이터 클래스는 abstract, open, sealed, inner가 되면 안 된다
```kotlin
data class Person(
    val name: String,
    val age: Int,
    val birthday: Date?
)
```

 5. 함수형 프로그래밍
    - 일급 객체(First-Class Object) : 다른 객체들에 일반적으로 적용 가능한 연산을 모두 지원하는 객체
        - 변수에 할당할 수 있다
        - 함수의 인자로 전달할 수 있다
        - 함수의 반환값으로 함수를 반환할 수 있다
    - 특징
        - 일급 객체인 함수 : 함수를 일급 객체처럼 사용할 수 있다
        - 불변성 : 일단 만들어지고 나면 내부 상태가 절대로 바뀌지 않는 불변 객체를 사용한다
        - 부수효과 없음 : 입력이 같으면 항상 출력이 같고, 다른 객체의 상태를 변경하지 않으며, 외부와 상호작용하지 않는 순수함수를 사용한다
    - 장점
        - 간결성 : 함수를 값처럼 사용할 수 있으며, 강력한 추상화를 통해 코드 중복을 막을 수 있다
        - 안전한 다중 스레딩 : 불변 데이터 구조를 사용하여 순수함수를 적용하면 여러 스레드에서 데이터를 변경할 수 없다
        - 테스트 용이 : 순수함수는 준비코드 없이 독립적으로 테스트할 수 있다
    - 코틀린에서의 함수형 프로그래밍
        - 함수 타입을 지원하여 함수를 파라미터로 받거나 함수를 반환할 수 있다
        - 람다식을 지원하여 준비코드 작성 없이 코드 블록을 쉽게 정의하고 여기저기 전달할 수 있다
        - 데이터 클래스는 불변적인 값 객체를 간편하게 만들 수 있는 구문을 제공한다
        - 코틀린 표준 라이브러리는 객체와 컬렉션을 함수형 스타일로 다룰 수 있는 API를 제공한다
