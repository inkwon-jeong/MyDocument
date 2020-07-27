# Dependency Injection



## What is Dependency?

- B 클래스를 사용하는 A 클래스에 대해, A 클래스가 B 클래스를 의존한다고 말한다
- A 클래스를 dependent, B 클래스를 dependency라고 한다

```java
class Targaryens { // Dependant
  
        public Targaryens() {
                Dragons dragons = new Dragons(); // Dependency
                dragons.callForWar();
        }
}
```



## Why dependencies are bad?

- 재사용이 어렵다
- 테스트를 하기 어렵다
- 프로젝트가 커질 때 코드의 유지보수가 어렵다



## Types of Dependencies

### Class Dependency

- 생성자 메서드에서 dependency를 사용한다

### Interface Dependency

- 인터페이스의 dependency

```java
public Result executePlan(WarStrategy strategy) {
  //WarStrategy is implemented by all the houses
}
```

### Method/Field Dependency

- 객체의 메서드나 필드에 대한 dependency

```java
public String extractName(HouseClass house) {
  return house.getKing();
  //getting the king name of any house
}
```

### Direct and Indirect Dependency

- A 클래스가 B 클래스를 의존하고, B 클래스가 C 클래스를 의존한다
- 이 때, A 클래스는 직접적으로 B 클래스를 의존하고, 간접적으로 C 클래스를 의존한다



## What is dependency injection?

- Dependency Injection(DI)는 dependency 문제를 해결하기 위한 방법이다
- DI는 한 객체가 다른 객체의 dependency를 제공해주는 기술이다
- DI는 Inversion of Control 개념에 기반하여, 어떤 클래스도 dependency를 인스턴스화할 수 없고 다른 클래스로부터 받아야 한다



