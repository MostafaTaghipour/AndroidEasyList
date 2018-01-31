# AndroidEasyList
[![](https://jitpack.io/v/mostafataghipour/androideasylist.svg)](https://jitpack.io/#mostafataghipour/androideasylist)

## [iOS version is here](https://github.com/MostafaTaghipour/iOSEasyList)

Framework to simplify the setup and configuration of Recyclerview adapter. It allows a type-safe setup of RecyclerView adapter.  also provides out-of-the-box diffing and animated deletions, inserts, moves and changes.


Everything you need to implement your own lists:
- Easy to use UITableView and UICollectionView
- Diffable
- Header and footer
- Pagination
- Expandable
- Loading footer
- Empty View
- Filterable
- Multiple data type

<img width="290" alt="animation" src="/screenshots/animation.gif"> <img width="290" alt="expandable" src="/screenshots/expandable.gif"> <img width="290" alt="filtering" src="/screenshots/filtering.gif"> <img width="290" alt="message" src="/screenshots/message.gif"> <img width="290" alt="layout" src="/screenshots/layout.gif"> <img width="290" alt="pagination" src="/screenshots/pagination.gif"> <img width="290" alt="sectioned" src="/screenshots/sectioned.gif">


## Requirements

- Api 14+

## Installation

Add JitPack to repositories in your project's root `build.gradle` file:

```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your module's `build.gradle` file:

```Gradle
dependencies {
    ...
    compile 'com.github.mostafataghipour:androideasylist:1.0.3'
}
```



## Usage
1. Define `RecyclerViewAdapter`
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
}

```

2. Set Data
```kotlin
    adapter.items = yourItems
```

3. That's it, for more samples please see example project

## Thanks for

- [Android-Pagination-with-RecyclerView](https://github.com/Suleiman19/Android-Pagination-with-RecyclerView)
- [Apinned-section-item-decoration](https://github.com/takahr/pinned-section-item-decoration)
- [Generic ViewHolder](https://medium.com/@AlexeyBuzdin/generic-viewholder-for-android-63bf9e0db06a)

## Author

Mostafa Taghipour, mostafa.taghipour@ymail.com

## License

EasyList is available under the MIT license. See the LICENSE file for more info.

