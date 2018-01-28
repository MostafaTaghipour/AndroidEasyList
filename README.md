# EasyList

Framework to simplify the setup and configuration of Recyclerview adapter. It allows a type-safe setup of RecyclerView adapter.  also provides out-of-the-box diffing and animated deletions, inserts, moves and changes.


Everything you need to implement your own lists:
- Easy to use UITableView and UICollectionView
- Diffable
- Sectioned
- Pagination
- Expandable
- Loading footer
- Empty View
- Multiple data type

<img width="290" alt="animation" src="/screenshots/animation.gif"> <img width="290" alt="expandable" src="/screenshots/expandable_ios.gif"> <img width="290" alt="filtering" src="/screenshots/filtering.gif"> <img width="290" alt="message" src="/screenshots/message.gif"> <img width="290" alt="layout" src="/screenshots/layout.gif"> <img width="290" alt="pagination" src="/screenshots/pagination.gif"> <img width="290" alt="sectioned" src="/screenshots/sectioned.gif">


## Requirements

- Api 14+

## Installation

Add JitPack to repositories in your project's root `build.gradle` file:

```Gradle
allprojects {
repositories {
...
maven { url "https://jitpack.io" }
}
}
```

Add the dependency to your module's `build.gradle` file:

```Gradle
dependencies {
...

}
```



## Usage
1. Define your model
```kotlin
data class Movie(
    val id: String,
    val title: String
) : Diffable {
    override val diffableIdentity: String
        get() = id

    override fun isEqualTo(other: Any): Boolean {
        return if (other is Movie) return this == other else false
    }
}
```

2. Define `RecyclerViewAdapter`
```kotlin
private val adapter: RecyclerViewAdapter<Movie> by lazy {

    val adapter = object : RecyclerViewAdapter<Movie>(this){
        override fun getLayout(viewType: Int): Int {
            return  R.layout.item_list
        }

        override fun bindView(item: Movie, position: Int, viewHolder: RecyclerView.ViewHolder) {
            viewHolder as GenericViewHolder
            val mMovieTitle: TextView? = viewHolder.getView<TextView>(R.id.movie_title)
            val mMovieDesc: TextView? = viewHolder.getView<TextView>(R.id.movie_desc)
            val mYear: TextView? = viewHolder.getView<TextView>(R.id.movie_year)
            val mPosterImg: ImageView? = viewHolder.getView<ImageView>(R.id.movie_poster)


            mMovieTitle?.text = item.title
            mYear?.text = AppHelpers.formatYearLabel(item)
            mMovieDesc?.text = item.overview

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                .into(mPosterImg)

        }
    }
    
    return adapter
}


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_filtering)

    recyclerView.adapter = adapter
})

}
```

3. Set Data
```kotlin
    adapter.items = yourItems
```

4. That's it, for more samples please see example project



## Author

Mostafa Taghipour, mostafa.taghipour@ymail.com

## License

EasyList is available under the MIT license. See the LICENSE file for more info.

