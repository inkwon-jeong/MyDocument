# 안드로이드 아키텍처 컴포넌트

[!architecture](.image/final-architecture.png)

## ViewModel
 - UI 컨트롤러(액티비티, 프래그먼트)에 관한 데이터를 제공하고 모델과 커뮤니케이션하기 위한 데이터 처리 비즈니스 로직을 포함한다
 - UI 컨트롤러의 구성변경에도 데이터를 유지한다

```kotlin
// ViewModel
class MyViewModel : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}

// UI Controller
class MyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.

        val model = ViewModelProviders.of(this)[MyViewModel::class.java]
        model.getUsers().observe(this, Observer<List<User>>{ users ->
            // update UI
        })
    }
}
```

### ViewModel 생명주기
 - ViewModel 개체의 범위는 ViewModel을 가져올 때 ViewModelProvider에 전달되는 Lifecycle로 지정된다
 - ViewModel은 범위가 지정된 Lifecycle이 영구적으로 끝날 때까지 메모리에 남아있는다
 - Lifecycle이 영구적으로 끝나면 onCleared() 호출한다


## Room
 - SQLite에 대한 추상화 레이어를 제공하여 원활한 데이터베이스 액세스를 지원하는 동시에 SQLite를 완벽히 활용한다

### Room 구성요소
 1. Database
  - 데이터베이스 홀더를 포함하며 앱의 지속적인 관계형 데이터에 대한 기본 연결을 위한 기본 액세스 포인트 역할을 한다

```kotlin
// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

   abstract fun wordDao(): WordDao

   companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
   }
}
```

 2. Entity
  - 데이터베이스 내의 테이블을 나타낸다

```kotlin
@Entity(tableName = "word_table")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
```

 3. DAO(Data Access Object)
  - 데이터베이스에 액세스하는 데 사용되는 메서드가 포함되어 있다

```kotlin
@Dao
interface WordDao {
    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}
```

## WorkManager
 - 지연 가능하고 안정적으로 실행되어야 하는 작업을 대상으로 설계되었다

### 주요 기능
 - 네트워크 가용성 또는 충전 상태와 같은 작업 제약 조건 추가
 - 비동기 일회성 작업 또는 주기적으로 실행되는 작업 예약
 - 예약된 작업 모니터링 및 관리
 - 작업 체이닝
 - 앱 또는 기기가 다시 시작되는 경우에도 작업 실행을 보장
 - 잠자기 모드와 같은 절전 기능 지원

### 백그라운드 작업 만들기
 - Worker 클래스를 확장하고 doWork() 메서드를 재정의한다
 - doWork()에서 반환된 Result는 WorkManager에 작업의 다음 상태를 알린다
  + Result.success() : 성공
  + Result.failure() : 실패
  + Result.retry() : 나중에 다시 시도

```kotlin
class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Do the work here--in this case, upload the images.

        uploadImages()

        // Indicate whether the task finished successfully with the Result
        return Result.success()
    }
}
```

### 작업 실행 방법 및 시기 구성
 - WorkRequest는 작업이 언제 어떻게 실행되어야 하는지 정의한다
 - 일회성 작업에는 OneTimeWorkRequest를 사용하고 주기적 작업에는 PeriodicWorkRequest를 사용한다

```kotlin
val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
        .build()
```

### 시스템에 작업 전달
 - WorkManager가 enqueue()를 호출하여 시스템에 작업을 전달한다

```kotlin
WorkManager.getInstance(myContext).enqueue(uploadWorkRequest)
```
