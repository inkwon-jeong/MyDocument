# 자료구조

## List
 - 순서가 있는 데이터의 집합
 - 데이터의 중복을 허용한다

### ArrayList vs LinkedList
 - 순차적으로 추가/삭제하는 경우에는 ArrayList가 더 빠르다
 - 중간 데이터를 추가/삭제하는 경우에는 LinkedList가 더 빠르다
 - 데이터 접근시간은 ArrayList가 더 빠르다
 - 메모리 효율성은 LinkedList가 더 좋다

### Interface
```java
import java.util.Iterator;

public interface ListIterator<T> extends List<T>, Iterable<T> {
    Iterator<T> getIterator();
}

public interface List<T> {
    void add(T newEntry);                       // ArrayList : O(1), LinkedList : O(n)
    void add(int newPosition, T newEntry);      // ArrayList : O(n), LinkedList : O(n)
    T remove(int givenPosition);                // ArrayList : O(n), LinkedList : O(n)
    void clear();                               // ArrayList : O(1), LinkedList : O(1)
    T replace(int givenPosition, T newEntry);   // ArrayList : O(1), LinkedList : O(n)
    T get(int givenPosition);                   // ArrayList : O(1), LinkedList : O(n)
    boolean contains(T anEntry);                // ArrayList : O(n), LinkedList : O(n)
    int size();                                 // ArrayList : O(1), LinkedList : O(1)
    boolean isEmpty();                          // ArrayList : O(1), LinkedList : O(1)
    T[] toArray();                              // ArrayList : O(n), LinkedList : O(n)
}
```
### ArrayList
```java
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<T> implements ListIterator<T> {

    private T[] list;
    private int numberOfEntries;
    private int initialCapacity;
    private boolean initialized = false;
    private static final int DEFAULT_INITIAL_CAPACITY = 25;
    private static final int MAX_CAPACITY = 10000;

    public ArrayList() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayList(int initialCapacity) {
        if(initialCapacity < DEFAULT_INITIAL_CAPACITY)
            initialCapacity = DEFAULT_INITIAL_CAPACITY;
        else
            checkCapacity(initialCapacity);

        initializeDataFields(initialCapacity);
    }

    private void initializeDataFields(int initialCapacity) {
        @SuppressWarnings("unchecked")
        T[] tempList = (T[]) new Object[initialCapacity];
        list = tempList;
        numberOfEntries = 0;
        this.initialCapacity = initialCapacity;
        initialized = true;
    }

    @Override
    public void add(T newEntry) {
        checkInitialization();
        list[numberOfEntries] = newEntry;
        numberOfEntries++;
        ensureCapacity();
    }

    @Override
    public void add(int newPosition, T newEntry) {
        checkInitialization();
        if(newPosition >= 0 && newPosition <= numberOfEntries) {
            if(newPosition < numberOfEntries)
                makeRoom(newPosition);
            list[newPosition] = newEntry;
            numberOfEntries++;
            ensureCapacity();
        }
    }

    @Override
    public T remove(int givenPosition) {
        checkInitialization();
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            T result = list[givenPosition];

            if(givenPosition < numberOfEntries - 1)
                removeGap(givenPosition);
            numberOfEntries--;
            return result;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to remove operation.");
    }

    @Override
    public void clear() {
        initializeDataFields(initialCapacity);
    }

    @Override
    public T replace(int givenPosition, T newEntry) {
        checkInitialization();
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            T originalEntry = list[givenPosition];
            list[givenPosition] = newEntry;
            return originalEntry;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to replace operation.");
    }

    @Override
    public T get(int givenPosition) {
        checkInitialization();
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            return list[givenPosition];
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to getEntry operation.");
    }

    @Override
    public boolean contains(T anEntry) {
        checkInitialization();
        boolean found = false;
        int index = 0;
        while(!found && index < numberOfEntries) {
            if(anEntry.equals(list[index]))
                found = true;
            index++;
        }
        return found;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public T[] toArray() {
        checkInitialization();

        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];
        for(int index = 0; index < numberOfEntries; index++)
            result[index] = list[index];

        return result;
    }

    private void checkInitialization() {
        if(!initialized)
            throw new SecurityException(
                    "ArrayList object is not initialized properly");
    }

    private void checkCapacity(int capacity) {
        if(capacity > MAX_CAPACITY)
            throw new IllegalStateException(
                    "Attempt to create a list whose capacity exceeds allowed maximum of "
                            + MAX_CAPACITY);
    }

    private void ensureCapacity() {
        int capacity = list.length;
        if(numberOfEntries >= capacity) {
            int newCapacity = 2 * capacity;
            checkCapacity(newCapacity);
            list = Arrays.copyOf(list, 2 * list.length);
        }
    }

    private void makeRoom(int newPosition) {
        assert newPosition >= 0 && newPosition < numberOfEntries;
        int newIndex = newPosition;
        int lastIndex = numberOfEntries - 1;

        for(int index = lastIndex; index >= newIndex; index--) {
            list[index + 1] = list[index];
        }
    }

    private void removeGap(int givenPosition) {
        assert givenPosition >= 0 && givenPosition < numberOfEntries - 1;
        int removedIndex = givenPosition;
        int lastIndex = numberOfEntries - 1;
        for(int index = removedIndex; index < lastIndex; index++)
            list[index] = list[index + 1];
    }

    @Override
    public Iterator<T> getIterator() {
        return iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T> {

        private int nextIndex;

        private ArrayListIterator() {
            nextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return nextIndex <= numberOfEntries;
        }

        @Override
        public T next() {
            if(hasNext()) {
                T entry = list[nextIndex];
                nextIndex++;
                return entry;
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of list.");
        }
    }
}
```
### LinkedList
```java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements ListIterator<T> {

    private Node firstNode;
    private Node lastNode;
    private int numberOfEntries;

    public LinkedList() {
        initializeDataFields();
    }

    private void initializeDataFields() {
        firstNode = null;
        lastNode = null;
        numberOfEntries = 0;
    }

    @Override
    public void add(T newEntry) {
        Node newNode = new Node(newEntry);
        if(isEmpty()) {
            firstNode = newNode;
        } else {
            lastNode.setNextNode(newNode);
        }
        lastNode = newNode;
        numberOfEntries++;
    }

    @Override
    public void add(int newPosition, T newEntry) {
        if(newPosition >= 0 && newPosition <= numberOfEntries) {
            Node newNode = new Node(newEntry);
            if(isEmpty()) {
                firstNode = newNode;
                lastNode = newNode;
            } else if(newPosition == 0) {
                newNode.setNextNode(firstNode);
                firstNode = newNode;
            } else if(newPosition == numberOfEntries) {
                lastNode.setNextNode(newNode);
                lastNode = newNode;
            } else {
                Node nodeBefore = getNodeAt(newPosition - 1);
                Node nodeAfter = nodeBefore.getNextNode();
                newNode.setNextNode(nodeAfter);
                nodeBefore.setNextNode(newNode);
            }
            numberOfEntries++;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to add operation");
    }

    @Override
    public T remove(int givenPosition) {
        T result = null;
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            if(givenPosition == 0) {
                result = firstNode.getData();
                firstNode = firstNode.getNextNode();
                if(numberOfEntries == 1)
                    lastNode = null;
            } else if(givenPosition == numberOfEntries - 1) {
                Node nodeBefore = getNodeAt(givenPosition - 1);
                nodeBefore.setNextNode(null);
                result = lastNode.getData();
                lastNode = nodeBefore;
            } else {
                Node nodeBefore = getNodeAt(givenPosition - 1);
                Node nodeToRemove = nodeBefore.getNextNode();
                Node nodeAfter = nodeToRemove.getNextNode();
                nodeBefore.setNextNode(nodeAfter);
                result = nodeToRemove.getData();
            }
            numberOfEntries--;

            return result;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to remove operation");
    }

    @Override
    public void clear() {
        initializeDataFields();
    }

    @Override
    public T replace(int givenPosition, T newEntry) {
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            Node desiredNode = getNodeAt(givenPosition);
            T originalEntry = desiredNode.getData();
            desiredNode.setData(newEntry);
            return originalEntry;
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to replace operation");
    }

    @Override
    public T get(int givenPosition) {
        if(givenPosition >= 0 && givenPosition < numberOfEntries) {
            assert !isEmpty();
            return getNodeAt(givenPosition).getData();
        } else
            throw new IndexOutOfBoundsException(
                    "Illegal position given to getEntry operation");
    }

    @Override
    public boolean contains(T anEntry) {
        boolean found = false;
        Node currentNode = firstNode;
        while(!found && currentNode != null) {
            if(currentNode.getData().equals(anEntry))
                found = true;
            else
                currentNode = currentNode.getNextNode();
        }
        return found;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        boolean result;
        if(numberOfEntries == 0) {
            assert firstNode == null;
            result = true;
        } else {
            assert firstNode != null;
            result = false;
        }
        return result;
    }

    @Override
    public T[] toArray() {
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[numberOfEntries];

        int index = 0;
        Node currentNode = firstNode;
        while(index < numberOfEntries && currentNode != null) {
            result[index] = currentNode.getData();
            currentNode = currentNode.getNextNode();
            index++;
        }

        return result;
    }

    private Node getNodeAt(int givenPosition) {
        assert firstNode != null && givenPosition >= 0 && givenPosition < numberOfEntries;
        Node currentNode = firstNode;

        for(int counter = 0; counter < givenPosition; counter++)
            currentNode = currentNode.getNextNode();

        assert currentNode != null;
        return currentNode;
    }

    @Override
    public Iterator<T> getIterator() {
        return iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {

        private Node nextNode;

        private LinkedListIterator() {
            nextNode = firstNode;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public T next() {
            if(hasNext()) {
                Node node = nextNode;
                nextNode = nextNode.getNextNode();
                return node.getData();
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of list.");
        }
    }

    private class Node {
        private T data;
        private Node next;

        private Node(T dataPortion) {
            this(dataPortion, null);
        }

        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }

        T getData() {
            return data;
        }

        void setData(T newData) {
            data = newData;
        }

        Node getNextNode() {
            return next;
        }

        void setNextNode(Node nextNode) {
            next = nextNode;
        }
    }
}
```

## Stack
 - 마지막에 저장한 데이터를 가장 먼저 꺼내게 되는 LIFO(Last In First Out) 구조
 - 활용 예시
    - JVM의 호출 스택
    - 웹 브라우저 방문기록
    - 실행취소(undo)
    - 역순 문자열 만들기
    - 수식의 괄호 검사
    - 후위표기법(postfix) 계산

### Interface
```java
public interface Stack<T> {
    void push(T newEntry); // O(1)
    T pop();               // O(1)
    T peek();              // O(1)
    boolean isEmpty();     // O(1)
    void clear();          // O(1)
    int size();            // O(1)
}
```

### VectorStack
```java
import java.util.EmptyStackException;
import java.util.Vector;

public class VectorStack<T> implements Stack<T> {

    private final Vector<T> stack;
    private final boolean initialized;
    private static final int DEFAULT_INITIAL_CAPACITY = 50;
    private static final int MAX_CAPACITY = 10000;

    public VectorStack() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public VectorStack(int initialCapacity) {
        checkCapacity(initialCapacity);
        stack = new Vector<>(initialCapacity);
        initialized = true;
    }

    @Override
    public void push(T newEntry) {
        checkInitialization();
        stack.add(newEntry);
    }

    @Override
    public T pop() {
        checkInitialization();
        if(isEmpty())
            throw new EmptyStackException();
        else
            return stack.remove(stack.size() - 1);
    }

    @Override
    public T peek() {
        checkInitialization();
        if(isEmpty())
            throw new EmptyStackException();
        else
            return stack.lastElement();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public int size() {
        return stack.size();
    }

    private void checkInitialization() {
        if(!initialized)
            throw new SecurityException(
                    "VectorStack object is not initialized properly");
    }

    private void checkCapacity(int capacity) {
        if(capacity > MAX_CAPACITY)
            throw new IllegalStateException(
                    "Attempt to create a stack whose capacity exceeds allowed maximum of "
                            + MAX_CAPACITY);
    }
}
```

## Queue
 - 처음에 저장한 데이터를 가장 먼저 꺼내게 되는 FIFO(First In First Out) 구조
 - 활용 예시
    - 프로세스 관리
    - 우선순위가 같은 작업 예약
    - 은행 업무
    - 너비 우선 탐색(BFS)

### Interface
```java
public interface Queue<T> {
    void enqueue(T newEntry);  // O(1)
    T dequeue();               // O(1)
    T getFront();              // O(1)
    boolean isEmpty();         // O(1)
    void clear();              // O(1)
    int size();                // O(1)
}
```

### LinkedQueue
```java
import java.util.NoSuchElementException;

public class LinkedQueue<T> implements Queue<T> {

    private Node firstNode;
    private Node lastNode;
    private int size;

    public LinkedQueue() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    public void enqueue(T newEntry) {
        Node newNode = new Node(newEntry, null);
        if(isEmpty())
            firstNode = newNode;
        else
            lastNode.setNextNode(newNode);
        lastNode = newNode;
        size++;
    }

    @Override
    public T dequeue() {
        T front = getFront();
        assert firstNode != null;
        firstNode = firstNode.getNextNode();

        if(firstNode == null)
            lastNode = null;

        size--;
        return front;
    }

    @Override
    public T getFront() {
        if(isEmpty())
            throw new NoSuchElementException();
        else
            return firstNode.getData();
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null && lastNode == null;
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    private class Node {
        private T data;
        private Node next;

        private Node(T dataPortion) {
            this(dataPortion, null);
        }

        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }

        T getData() {
            return data;
        }

        void setData(T newData) {
            data = newData;
        }

        Node getNextNode() {
            return next;
        }

        void setNextNode(Node nextNode) {
            next = nextNode;
        }
    }
}
```

## Deque
 - Queue의 변형으로, 양쪽 끝에 추가/삭제가 가능하다

### Interface
```java
public interface Deque<T> {
    void addToFront(T newEntry);  // O(1)
    T removeFront();              // O(1)
    void addToBack(T newEntry);   // O(1)
    T removeBack();               // O(1)
    T getFront();                 // O(1)
    T getBack();                  // O(1)
    boolean isEmpty();            // O(1)
    void clear();                 // O(1)
    int size();                   // O(1)
}
```

### LinkedDeque
```java
import java.util.NoSuchElementException;

public class LinkedDeque<T> implements Deque<T> {

    private Node firstNode;
    private Node lastNode;
    private int size;

    public LinkedDeque() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    public void addToFront(T newEntry) {
        Node newNode = new Node(newEntry, firstNode, null);
        if(isEmpty())
            lastNode = newNode;
        else
            firstNode.setPreviousNode(newNode);

        firstNode = newNode;
        size++;
    }

    @Override
    public T removeFront() {
        T front = getFront();
        assert firstNode != null;
        firstNode = firstNode.getNextNode();

        if(firstNode == null)
            lastNode = null;
        else
            firstNode.setPreviousNode(null);

        size--;
        return front;
    }

    @Override
    public void addToBack(T newEntry) {
        Node newNode = new Node(newEntry, null, lastNode);
        if(isEmpty())
            firstNode = newNode;
        else
            lastNode.setNextNode(newNode);

        lastNode = newNode;
        size++;
    }

    @Override
    public T removeBack() {
        T back = getBack();
        assert lastNode != null;
        lastNode = lastNode.getPreviousNode();

        if(lastNode == null)
            firstNode = null;
        else
            lastNode.setNextNode(null);

        size--;
        return back;
    }

    @Override
    public T getFront() {
        if(isEmpty())
            throw new NoSuchElementException();
        else
            return firstNode.getData();
    }

    @Override
    public T getBack() {
        if(isEmpty())
            throw new NoSuchElementException();
        else
            return lastNode.getData();
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null && lastNode == null;
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    private class Node {
        private T data;
        private Node next;
        private Node previous;

        private Node(T dataPortion) {
            this(dataPortion, null, null);
        }

        private Node(T dataPortion, Node nextNode, Node previousNode) {
            data = dataPortion;
            next = nextNode;
            previous = previousNode;
        }

        T getData() {
            return data;
        }

        void setData(T newData) {
            data = newData;
        }

        Node getNextNode() {
            return next;
        }

        void setNextNode(Node nextNode) {
            next = nextNode;
        }

        Node getPreviousNode() {
            return previous;
        }

        void setPreviousNode(Node previous) {
            this.previous = previous;
        }
    }
}
```

## 해싱(Hashing)
 - 원래 데이터의 값 : 키(key) -> 데이터의 값 : 해시값(hash value) 매핑하는 것
 - 인덱스에 해시값을 사용함으로써 모든 데이터를 살피지 않아도 검색, 삽입, 삭제를 빠르게 수행할 수 있다
 - 시간복잡도(검색, 삽입, 삭제) : O(1)

## Set
 - 순서를 유지하지 않는 데이터의 집합
 - 데이터의 중복을 허용하지 않는다

### Interface
```java
import java.util.Iterator;

public interface Set<T> {
    void add(T value);
    T remove(T value);
    boolean contains(T value);
    Iterator<T> iterator();
    boolean isEmpty();
    int size();
    void clear();
}
```

### HashSet
```java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashSet<T> implements Set<T> {

    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5;
    private static final int MAX_CAPACITY = 10000;

    private T[] hashTable;
    private int tableSize;
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean initialized = false;
    private static final double MAX_LOAD_FACTOR = 0.5;

    public HashSet() {
        this(DEFAULT_CAPACITY);
    }

    public HashSet(int initialCapacity) {
        checkCapacity(initialCapacity);

        int tableSize = getNextPrime(initialCapacity);
        checkSize(tableSize);

        initializeDataFields(tableSize);
    }

    private int getNextPrime(int number) {
        int prime = number;
        while (true) {
            boolean finish = true;
            for(int i = 2; i * i <= prime; i++) {
                if(prime % i == 0) {
                    finish = false;
                    break;
                }
            }

            if(finish)
                break;
            else
                prime++;
        }
        return prime;
    }

    private void initializeDataFields(int tableSize) {
        @SuppressWarnings("unchecked")
        T[] tempTable = (T[]) new Object[tableSize];
        hashTable = tempTable;
        numberOfEntries = 0;
        this.tableSize = tableSize;
        initialized = true;
    }

    private boolean isHashSetTooFull() {
        return ((double) numberOfEntries / hashTable.length) >= MAX_LOAD_FACTOR;
    }

    private void enlargeHashSet() {
        T[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize * 2);

        @SuppressWarnings("unchecked")
        T[] tempTable = (T[]) new Object[newSize];
        hashTable = tempTable;
        numberOfEntries = 0;

        for(int index = 0; index < oldSize; index++) {
            if(oldTable[index] != null) {
                add(oldTable[index]);
            }
        }
    }

    private int getHashIndex(T value) {
        return Math.abs(value.hashCode() % hashTable.length);
    }

    private int locate(int index, T value) {
        boolean found = false;
        while (!found && (hashTable[index] != null)) {
            if(value.equals(hashTable[index]))
                found = true;
            else
                index = (index + 1) % hashTable.length;
        }

        return found ? index : -1;
    }


    @Override
    public void add(T value) {
        checkInitialization();
        if((value == null))
            throw new IllegalArgumentException();
        else {
            int index = locate(getHashIndex(value), value);

            assert (index >= 0) && (index < hashTable.length);
            if(hashTable[index] == null) {
                hashTable[index] = value;
                numberOfEntries++;
            }
        }

        if(isHashSetTooFull())
            enlargeHashSet();
    }

    @Override
    public T remove(T value) {
        checkInitialization();
        T result = null;
        int index = locate(getHashIndex(value), value);

        if(index != -1) {
            result = hashTable[index];
            hashTable[index] = null;
            numberOfEntries--;
        }
        return result;
    }

    @Override
    public boolean contains(T value) {
        checkInitialization();
        int index = locate(getHashIndex(value), value);
        return index != -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new SetIterator();
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        initializeDataFields(tableSize);
    }

    private void checkInitialization() {
        if(!initialized)
            throw new SecurityException(
                    "HashMap object is not initialized properly");
    }

    private void checkCapacity(int capacity) {
        if(capacity > MAX_CAPACITY)
            throw new IllegalStateException(
                    "Attempt to create a map whose capacity exceeds allowed maximum of "
                            + MAX_CAPACITY);
    }

    private void checkSize(int size) {
        if(size > MAX_SIZE)
            throw new IllegalStateException(
                    "Attempt to create a map whose size exceeds allowed maximum of "
                            + MAX_SIZE);
    }

    private class SetIterator implements Iterator<T> {

        private int currentIndex;
        private int numberLeft;

        private SetIterator() {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberLeft > 0;
        }

        @Override
        public T next() {
            if(hasNext()) {
                while(hashTable[currentIndex] == null)
                    currentIndex++;

                T result = hashTable[currentIndex];
                currentIndex++;
                numberLeft--;
                return result;
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of set.");
        }
    }
}
```

## Map
 - 키(key)와 값(value)의 쌍으로 이루어진 데이터의 집합
 - 순서를 유지하지 않는 데이터의 집합
 - 키는 중복을 허용하지 않고, 값은 중복을 허용한다

### Interface
```java
import java.util.Iterator;

public interface Map<K, V> {
    void put(K key, V value);
    V remove(K key);
    V get(K key);
    boolean contains(K key);
    Iterator<K> keyIterator();
    Iterator<V> valueIterator();
    boolean isEmpty();
    int size();
    void clear();
}
```

### HashMap
```java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMap<K, V> implements Map<K, V> {

    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5;
    private static final int MAX_CAPACITY = 10000;

    private Entry<K, V>[] hashTable;
    private int tableSize;
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean initialized = false;
    private static final double MAX_LOAD_FACTOR = 0.5;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int initialCapacity) {
        checkCapacity(initialCapacity);

        int tableSize = getNextPrime(initialCapacity);
        checkSize(tableSize);

        initializeDataFields(tableSize);
    }

    private int getNextPrime(int number) {
        int prime = number;
        while (true) {
            boolean finish = true;
            for(int i = 2; i * i <= prime; i++) {
                if(prime % i == 0) {
                    finish = false;
                    break;
                }
            }

            if(finish)
                break;
            else
                prime++;
        }
        return prime;
    }

    private void initializeDataFields(int tableSize) {
        @SuppressWarnings("unchecked")
        Entry<K, V>[] tempTable = (Entry<K, V>[]) new Entry[tableSize];
        hashTable = tempTable;
        numberOfEntries = 0;
        this.tableSize = tableSize;
        initialized = true;
    }

    private boolean isHashTableTooFull() {
        return ((double) numberOfEntries / hashTable.length) >= MAX_LOAD_FACTOR;
    }

    private void enlargeHashTable() {
        Entry<K, V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize * 2);

        @SuppressWarnings("unchecked")
        Entry<K, V>[] tempTable = (Entry<K, V>[]) new Entry[newSize];
        hashTable = tempTable;
        numberOfEntries = 0;

        for(int index = 0; index < oldSize; index++) {
            if((oldTable[index] != null) && oldTable[index].isIn()) {
                put(oldTable[index].getKey(), oldTable[index].getValue());
            }
        }
    }

    @Override
    public void put(K key, V value) {
        checkInitialization();
        if((key == null) || (value == null))
            throw new IllegalArgumentException();
        else {
            int index = probe(getHashIndex(key), key);

            assert (index >= 0) && (index < hashTable.length);
            if((hashTable[index] == null) || hashTable[index].isRemoved()) {
                hashTable[index] = new Entry<>(key, value);
                numberOfEntries++;
            } else {
                hashTable[index].setValue(value);
            }
        }

        if(isHashTableTooFull())
            enlargeHashTable();
    }

    @Override
    public V remove(K key) {
        checkInitialization();
        V result = null;
        int index = locate(getHashIndex(key), key);

        if(index != -1) {
            result = hashTable[index].getValue();
            hashTable[index].setToRemoved();
            numberOfEntries--;
        }

        return result;
    }

    private int getHashIndex(K key) {
        return Math.abs(key.hashCode() % hashTable.length);
    }

    private int locate(int index, K key) {
        boolean found = false;
        while (!found && (hashTable[index] != null)) {
            if(hashTable[index].isIn() &&
                    key.equals(hashTable[index].getKey()))
                found = true;
            else
                index = (index + 1) % hashTable.length;
        }

        return found ? index : -1;
    }

    private int probe(int index, K key) {
        boolean found = false;
        int removedStateIndex = -1;
        while (!found && (hashTable[index] != null)) {
            if(hashTable[index].isIn()) {
                if(key.equals(hashTable[index].getKey())) {
                    found = true;
                } else {
                    index = (index + 1) % hashTable.length;
                }
            } else {
                if(removedStateIndex == -1)
                    removedStateIndex = index;

                index = (index + 1) % hashTable.length;
            }
        }

        if(found || (removedStateIndex == -1))
            return index;
        else
            return removedStateIndex;
    }

    @Override
    public V get(K key) {
        checkInitialization();
        V result = null;
        int index = locate(getHashIndex(key), key);

        if(index != -1)
            result = hashTable[index].getValue();

        return result;
    }

    @Override
    public boolean contains(K key) {
        checkInitialization();
        int index = locate(getHashIndex(key), key);
        return index != -1;
    }

    @Override
    public Iterator<K> keyIterator() {
        return new KeyIterator();
    }

    @Override
    public Iterator<V> valueIterator() {
        return new ValueIterator();
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int size() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        initializeDataFields(tableSize);
    }

    private void checkInitialization() {
        if(!initialized)
            throw new SecurityException(
                    "HashMap object is not initialized properly");
    }

    private void checkCapacity(int capacity) {
        if(capacity > MAX_CAPACITY)
            throw new IllegalStateException(
                    "Attempt to create a map whose capacity exceeds allowed maximum of "
                            + MAX_CAPACITY);
    }

    private void checkSize(int size) {
        if(size > MAX_SIZE)
            throw new IllegalStateException(
                    "Attempt to create a map whose size exceeds allowed maximum of "
                            + MAX_SIZE);
    }

    private static class Entry<S, T> {
        private S key;
        private T value;
        private States state;
        private enum States {CURRENT, REMOVED}

        private Entry(S key, T value) {
            this.key = key;
            this.value = value;
            state = States.CURRENT;
        }

        private S getKey() {
            return key;
        }

        private T getValue() {
            return value;
        }

        private void setValue(T newValue) {
            value = newValue;
        }

        private boolean isIn() {
            return state == States.CURRENT;
        }

        private boolean isRemoved() {
            return state == States.REMOVED;
        }

        private void setToIn() {
            state = States.CURRENT;
        }

        private void setToRemoved() {
            state = States.REMOVED;
        }
    }

    private class KeyIterator implements Iterator<K> {

        private int currentIndex;
        private int numberLeft;

        private KeyIterator() {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberLeft > 0;
        }

        @Override
        public K next() {
            if(hasNext()) {
                while((hashTable[currentIndex] == null) ||
                        hashTable[currentIndex].isRemoved())
                    currentIndex++;

                K result = hashTable[currentIndex].getKey();
                numberLeft--;
                currentIndex++;
                return result;
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of map.");
        }
    }

    private class ValueIterator implements Iterator<V> {

        private int currentIndex;
        private int numberLeft;

        private ValueIterator() {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberLeft > 0;
        }

        @Override
        public V next() {
            if(hasNext()) {
                while((hashTable[currentIndex] == null) ||
                        hashTable[currentIndex].isRemoved())
                    currentIndex++;

                V result = hashTable[currentIndex].getValue();
                numberLeft--;
                currentIndex++;
                return result;
            } else
                throw new NoSuchElementException("Illegal call to next(); " +
                        "iterator is after end of map.");
        }
    }
}
```

## Tree

## Sorting

## Searching
