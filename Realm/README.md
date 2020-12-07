# Realm with Android Architecture Component



## Realm과 LiveData 연동

```kotlin
class RealmLiveData<T: RealmModel>(
    private val realmResults: RealmResults<T>
) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> {
        value = it
    }

    override fun onActive() {
        realmResults.addChangeListener(listener)
    }

    override fun onInactive() {
        realmResults.removeChangeListener(listener)
    }
}
```

```kotlin
// RealmUtils.kt
fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)
```



## 샘플 메모앱

### Installation

#### build.gradle(project)

```groovy
buildscript {
    repositories {
        jcenter()
        google()
     	 	maven { url "https://jitpack.io" }
        maven {
            url 'http://oss.jfrog.org/artifactory/oss-snapshot-local'
        }
    }
    dependencies {
        classpath "io.realm:realm-gradle-plugin:7.0.0-beta-SNAPSHOT"
    }
}

allprojects {
    repositories {
        jcenter()
        google()
      	maven { url "https://jitpack.io" }
        maven {
            url 'http://oss.jfrog.org/artifactory/oss-snapshot-local'
        }
    }
}
```

#### build.gradle(app)

```groovy
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt'
apply plugin: 'realm-android'
```



### Model

- Realm 객체는 open class로 정의한다
- 프로퍼티는 var로 선언한다
- 인자가 없는 생성자가 반드시 있어야 하므로 프로퍼티에 기본값을 정의한다

```kotlin
open class Word (
    @PrimaryKey
    var word: String = ""
) : RealmObject()
```

### Dao

- Realm에서 데이터를 가져올 때 findAllAsync()를 사용해야만 LiveData와 연동이 된다

- Realm에 데이터를 삽입할 때 executeTransactionAsync()를 사용해야만 업데이트가 적용된다

- Realm은 인스턴스를 생성한 스레드에서 트랜잭션을 실행해야 한다

  executeTransactionAsync()의 경우, 백그라운드 스레드에서 실행되므로 인자로 넘어온 Realm 인스턴스 `it`로 트랜잭션을 실행해야 한다

- Realm에서 데이터를 삭제할 때는 executeTransaction(), executeTransactionAsync() 둘 다 가능하다

```kotlin
class WordDao(private val realm: Realm) {

    fun getAlphabetizedWords() = realm.where<Word>()
        .sort("word", Sort.ASCENDING)
        .findAllAsync()
        .asLiveData()

    fun insert(word: Word) {
        realm.executeTransactionAsync {
            it.insert(word)
        }
    }

    fun delete(content: String) {
        realm.executeTransaction {
            it.where<Word>().equalTo("word", content).findAll().deleteAllFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            it.where<Word>().findAll().deleteAllFromRealm()
        }
    }
}
```

### Repository

- 예제 코드에서는 로컬 DB만 사용하지만 상용 앱에서는 서버와의 데이터 통신이 종종 있다
- 로컬 저장소와 서버 저장소뿐만 아니라 다른 저장소에 접근하고 데이터를 가공한다

```kotlin
class WordRepository(
    private val wordDao: WordDao
) {

    val allWords = wordDao.getAlphabetizedWords()

    fun insert(word: Word) {
        wordDao.insert(word)
    }

    fun delete(content: String) {
        wordDao.delete(content)
    }

    fun deleteAll() {
        wordDao.deleteAll()
    }
}
```

### ViewModel

- UI와 관련된 데이터 로직을 담당한다
- 주로 LiveData를 이용하여 데이터의 변화에 따라 UI를 반응하게 한다

```kotlin
class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    private val realm: Realm = Realm.getDefaultInstance()

    val allWords: LiveData<List<Word>>

    init {
        repository = WordRepository(WordDao(realm))
        allWords = Transformations.map(repository.allWords) { it }
    }

    fun insert(word: Word) {
        repository.insert(word)
    }

    fun delete(content: String) {
        repository.delete(content)
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    override fun onCleared() {
        super.onCleared()
        realm.close()
    }
}
```

### UI Component

- ViewModel의 LiveData를 관찰하여 데이터의 변화가 생기면 UI에 반영한다
- UI 이벤트가 발생하면 ViewModel의 메서드를 호출하여 데이터를 처리한다

```kotlin
// MainActivity.kt
wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

wordViewModel.allWords.observe(this, Observer { words ->
	words?.let { adapter.setWords(it) }
})

wordViewModel.delete(it)
wordViewModel.deleteAll()
wordViewModel.insert(word)
```



## Realm RecyclerView

- Realm RecyclerView를 사용하면 LiveData를 사용하지 않아도 데이터의 변화에 따라 RecyclerView가 갱신된다

### Adapter

- RealmRecyclerViewAdapter를 상속받아서 어댑터를 구현한다
- 생성자 파라미터로 OrderedRealmCollection(Realm 쿼리의 결과값)과 Boolean 값을 받는다
- OrderedRealmCollection은 리사이클러뷰의 데이터이고, autoUpdate는 데이터의 변화에 맞춰 리사이클러뷰를 자동으로 반영할 지를 결정한다

```kotlin
class WordListRealmAdapter(
    data: OrderedRealmCollection<Word>,
    autoUpdate: Boolean = true,
    private val click: (String) -> Unit
) : RealmRecyclerViewAdapter<Word, WordListRealmAdapter.WordViewHolder>(data, autoUpdate) {

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView = itemView.findViewById<TextView>(R.id.textView)

        fun onBind(item: Word) {
            wordItemView.text = item.word
            wordItemView.setOnClickListener {
                click(item.word)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }
}
```

### Dao

```kotlin
class WordDao(private val realm: Realm) {

    fun getAlphabetizedWords(): RealmResults<Word> = realm.where<Word>()
        .sort("word", Sort.ASCENDING)
        .findAll()

//    fun getAlphabetizedWords() = realm.where<Word>()
//        .sort("word", Sort.ASCENDING)
//        .findAllAsync()
//        .asLiveData()
  	...
}
```

### ViewModel

```kotlin
class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    private val realm: Realm = Realm.getDefaultInstance()

//    val allWords: LiveData<List<Word>>
    val allWords: RealmResults<Word>

    init {
        repository = WordRepository(WordDao(realm))
//        allWords = Transformations.map(repository.allWords) { it }
        allWords = repository.allWords
    }
  	...
}
```

### UI Component

```kotlin
class MainActivity : AppCompatActivity() {
  	...
  	override fun onCreate(savedInstanceState: Bundle?) {
      	...
//        val adapter = WordListAdapter(this) {
//            wordViewModel.delete(it)
//        }
        val adapter = WordListRealmAdapter(wordViewModel.allWords) {
            wordViewModel.delete(it)
        }
      	...
    }
}
```



## Realm & Coroutine

- Realm은 백그라운드 스레드에서 트랜잭션을 수행할 수 있는 함수를 제공한다
- executeTransactionAsync()
- 위 함수는 파라미터로 트랜잭션과 성공 시 콜백, 에러 시 콜백을 받는다
- 스레드 동기화 문제를 콜백을 이용하여 해결할 수 있지만, 코드가 깔끔해지지 않고 최근 안드로이드에서는 코루틴 사용을 권장한다
- 다음 함수로 Realm 트랜잭션을 코루틴으로 실행할 수 있다
- suspendCancellableCoroutine은 파라미터로 Continuation을 전달하고, Continuation.resume() 함수가 호출될 때까지 코루틴을 중지한다
- 콜백에 Continuation.resume()를 호출하여 트랜잭션이 완료되면 코루틴을 재개한다 -> 코루틴을 이용한 스레드 동기화 가능

```kotlin
suspend fun Realm.executeTransactionWithCoroutine(block: (Realm) -> Unit) {
    suspendCancellableCoroutine<Unit> { cont ->
        val execute = Realm.Transaction(block)
        val onSuccess = Realm.Transaction.OnSuccess { cont.resume(Unit) }
        val onError = Realm.Transaction.OnError { error -> cont.resumeWithException(error) }
        executeTransactionAsync(execute, onSuccess, onError)
    }
}
```



## Realm Thread Issue

- Realm은 동시에 다수의 스레드에서 접근할 수 있지만, 스레드 간에  Realm, RealmObject, query, results를 전달 할 수 없다
- 기본적으로, 쿼리를 이용하여 가져온 Realm 객체는 managed 상태이다 -> 따라서, Realm 객체를 수정하면 Realm DB에 반영된다
- Realm 객체를 unmanaged 상태로 사용하고 싶으면, Realm.copyFromRealm() 함수로 객체를 복사하면 된다
- 다음 함수들은 Realm 객체를 복사하고, 람다를 이용하여 다른 객체로 변환할 수 있다

```kotlin
fun <T : RealmObject, R> T.copy(block: (T) -> R) =
    block(realm.copyFromRealm(this))

fun <T : RealmObject, R> RealmResults<T>.copy(block: (List<T>) -> R) =
    block(realm.copyFromRealm(this))

fun <T : RealmObject, R> RealmLiveData<T>.copy(block: (List<T>) -> R) =
    Transformations.map(this) { block(realm.copyFromRealm(it)) }
```



- Realm 인스턴스는 Realm 데이터 커넥션을 열고 닫는 추가 비용을 줄이기 위해 [레퍼런스 카운트 캐시](https://en.wikipedia.org/wiki/Reference_counting)를 가진다
- 레퍼런스 카운트: 해당 객체가 처음 참조되면 메모리를 할당하고, 참조할 때마다 기존 메모리에 저장된 객체를 반환한다
  객체를 참조하는 곳이 없으면(참조값이 0이 되면) 객체를 메모리에서 제거한다
- Realm 인스턴스는 스레드 별로 레퍼런스 카운트를 가진다 -> 같은 스레드에서 Realm 인스턴스를 호출하는 것은 비용이 들지 않고 
  모든 인스턴스를 닫으면 메모리에서 제거된다
- Realm.getInstance()를 호출할 때마다 레퍼런스 카운트가 증가되고, Realm.close()를 호출할 때마다 레퍼런스 카운트가 감소된다
  -> Realm.getInstance()를 호출한 만큼 Realm.close()를 호출해야 해당 인스턴스가 메모리에서 제거된다
- Realm은 전체 레퍼런스 개수와 스레드별 레퍼런스 개수를 제공하지만, 스레드에 맞는 인스턴스를 제공해주는 기능이 없다
- 다음 함수는 해시맵을 이용하여 어떤 스레드에서 처음 Realm 인스턴스를 호출하면 인스턴스를 생성하여 맵에 저장하고,
  처음이 아니라면 기존에 있던 인스턴스를 반환한다

```kotlin
val hashMapOfRealm = hashMapOf<Pair<Long, String>, Realm>()

fun getRealmInstance(realmConfiguration: RealmConfiguration) =
    run {
        val localCount = Realm.getLocalInstanceCount(realmConfiguration)
        val globalCount = Realm.getGlobalInstanceCount(realmConfiguration)

        val threadId = Thread.currentThread().id
        val realmFileName = realmConfiguration.realmFileName

        val key = threadId to realmFileName

        Logger.d(
            "localCount: $localCount, " +
            "globalCount: $globalCount, " +
            "thread: $threadId, " +
            "realmFileName: ${realmFileName}, " +
            "realm: ${hashMapOfRealm[key]}"
        )

        hashMapOfRealm.getOrPut(
            key,
            { Realm.getInstance(realmConfiguration) }
        )
    }
```

- Realm을 사용하는 곳에 다음 코드를 작성하면 어떤 스레드에서도 Realm을 사용할 수가 있다

```kotlin
private val realm
        get() = getRealmInstance(realmConfiguration)
```

- Realm 인스턴스를 더 이상 사용하지 않으면 다음 코드를 통해 메모리에서 제거할 수 있다

```kotlin
fun closeRealmInstance(realmConfiguration: RealmConfiguration) {
    val threadId = Thread.currentThread().id
    val realmFileName = realmConfiguration.realmFileName

    val key = threadId to realmFileName

    hashMapOfRealm.remove(key)?.close()
}
```



