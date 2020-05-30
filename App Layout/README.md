# 앱 레이아웃

## Constraint Layout
 - 제약조건을 이용하여 뷰의 위치를 정의하는 레이아웃
 - 다른 뷰, 상위 레이아웃, 가이드라인을 기준으로 정렬 또는 연결을 나타낸다  

### 레이아웃 동작
 1. 상위 요소 포지셔닝
  - 뷰의 측면을 레이아웃의 대응하는 가장자리로 제한한다

 2. 위치 순서 지정
  - 가로 또는 세로로 두 뷰가 표시되는 순서를 정의한다

 3. 정렬
  - 뷰의 가장자리를 다른 뷰의 가장자리에 맞게 정렬한다

 4. 기준선 정렬
  - 뷰의 텍스트 기준선을 다른 뷰의 텍스트 기준선에 맞춘다

 5. 안내선으로 제한
  - 뷰를 제한할 수 있는 세로 또는 가로 안내선을 추가할 수 있다
  - 안내선은 앱 사용자에게 표시되지 않는다

 6. 경계선으로 제한
  - 경계선은 뷰를 제한하는 데 사용할 수 있는 표시되지 않는 선이다
  - 경계선은 자체 위치를 정의하지 않으며, 뷰 위치를 기반으로 경계선 위치가 이동된다

### 체인
 1. Spread
  - 뷰가 균등하게 분산된다

 2. Spread inside
  - 첫 번째 뷰와 마지막 뷰는 체인의 각 끝에 있는 제약조건에 고정되고 나머지 뷰는 균등하게 분산된다

 3. Weighted
  - 체인이 Spread 또는 Spread inside로 설정되면 하나 이상의 뷰가 나머지 공간을 채운다
  - layout_constraintHorizontal_weight 및 layout_constraintVertical_weight 속성으로 사용하여 뷰의 가중치를 할당한다

 4. Packed
  - 뷰가 여백을 제외한 간격 없이 배치된다


## Linear Layout
 - 세로 또는 가로의 단일 방향으로 모든 하위 요소를 정렬하는 뷰 그룹
 - android:orientation 속성으로 레이아웃 방향을 지정한다
 - android:layout_weight 속성으로 개별 하위 요소에 가중치를 할당한다

## Relative Layout
 - 상대 위치에 하위 뷰를 표시하는 뷰 그룹
 - 각 뷰의 위치는 동위 요소나 상위 RelativeLayout 영역에 상대적인 위치로 지정된다

## Recycler View
 - 앱에서 대량의 데이터 세트 또는 자주 변경되는 데이터에 기반한 요소의 스크롤 목록을 표시하는 뷰
 - 사용자 인터페이스의 전체 컨테이너는 레이아웃에 추가하는 RecyclerView 객체이다
 - RecyclerView는 개발자가 제공한 Layout Manager에서 제공한 뷰로 채워진다(Linear, Grid)
 - 목록의 뷰는 뷰 홀더 객체로 표현된다(뷰 홀더는 뷰를 사용하여 단일 항목을 표시한다
 - RecyclerView는 동적 콘텐츠에서 화면에 나타나는 부분을 표시하는 데 필요한 뷰 홀더와 몇 개의 추가 뷰 홀더만큼만 만든다
 - 사용자가 목록을 스크롤하면 RecyclerView는 화면에 나타나지 않는 뷰를 가져와서 화면에 스크롤되는 데이터와 다시 바인딩한다(뷰 재활용)
 - 뷰 홀더 객체는 RecyclerView.Adapter를 확장하여 만든 어댑터에서 관리한다
 - 어댑터는 필요에 따라 뷰 홀더를 만들고(onCreateViewHolder()) 뷰 홀더를 데이터에 바인딩한다(onBindViewHolder())

### RecyclerView vs ListView
 - ListView는 ViewHolder 패턴을 구현하지 않아 각각의 뷰를 그릴 때마다 findViewById()를 호출하기 때문에 성능 저하 문제가 발생한다
 - ListView는 뷰를 세로로만 배치할 수 있지만 RecyclerView는 가로/세로/그리드 방향을 모두 지원한다

### RecyclerView.Adapter
```java
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        ...
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
```

### ListAdapter
 - ListAdapter는 내부적으로 리스트를 읽기만 가능한 불변 객체로 다룬다
 - 리스트의 항목에서 수정, 추가, 삭제, 이동이 발생한 경우 새로운 리스트를 ListAdapter로 전달해야 한다(submitList(List<T>))
 - DiffUtil이 이전 리스트와 새로운 리스트의 차이를 계산하여 효율적으로 RecyclerView를 갱신한다

```kotlin
private val diffCallback = object : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int) =
        oldItem == newItem // check uniqueness

    override fun areContentsTheSame(oldItem: Int, newItem: Int) =
        oldItem == newItem // check contents
}

private val adapter: ListAdapter<Int, NumberViewHolder> = object : ListAdapter<Int, NumberViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NumberViewHolder(parent)

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

private class NumberViewHolder(parentView: View) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context).inflate(R.layout.card_item, null, false)) {

    private val numberView = itemView.findViewById<TextView>(R.id.number)

    fun bindTo(position: Int) {
        numberView.text = "# $position"
    }
}
```

## 뷰 생성 과정
 - 안드로이드에서는 액티비티가 Focus를 얻게되면 자신의 Layout을 그리게 된다
 - 이때 그리고자 하는 Layout의 Root Node를 요청하게 되는데, setContentView()를 호출할 때 Root Node가 정해진다
 - Root Node를 따라 Child Node를 찾아가면서 차례대로 View를 그리게 된다

### Layout이 그려지는 과정
 - measure() -> onMeasure() -> layout() -> onLayout()

 - measure(widthMeasureSpec: Int, heightMeasureSpec: Int)

  - 뷰의 크기를 알아내기 위해 호출되며, 실제 크기 측정을 위해 onMeasure()를 호출한다
  - widthMeasureSpec : Parent가 부여한 필요한 가로 공간
  - heightMeasureSpec : Parent가 부여한 필요한 세로 공간

 - onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
  - 실제 뷰의 크기를 측정한다
  - widthMeasureSpec : Parent가 부여한 필요한 가로 공간
  - heightMeasureSpec : Parent가 부여한 필요한 세로 공간

 - layout(left: Int, top: Int, right: Int, bottom: Int)
  - 뷰의 위치를 할당하기 위해 호출되며, 실제 할당을 위해 onLayout()을 호출한다
  - left : Parent에 대한 왼쪽 포지션
  - top : Parent에 대한 위쪽 포지션
  - right : Parent에 대한 오른쪽 포지션
  - bottom : Parent에 대한 하단 포지션

 - onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)
  - 실제 뷰를 할당한다
  - changed : 새로운 사이즈나 위치인지 맞으면 True 아니면 False
  - left : Parent에 대한 왼쪽 포지션
  - top : Parent에 대한 위쪽 포지션
  - right : Parent에 대한 오른쪽 포지션
  - bottom : Parent에 대한 하단 포지션

 - onDraw(canvas: Canvas)
  - 뷰를 그리기 시작한다(Canvas는 모양(도형)을 그리고 Paint는 색, 스타일, 글꼴 등을 정의한다)
  - canvas : 뷰에서 스스로를 그리는 데 사용한다
