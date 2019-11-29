# AndroidEasyList
[![](https://jitpack.io/v/mostafataghipour/androideasylist.svg)](https://jitpack.io/#mostafataghipour/androideasylist)

## [iOS version is here](https://github.com/MostafaTaghipour/iOSEasyList)

Framework to simplify the setup and configuration of Recyclerview adapter. It allows a type-safe setup of RecyclerView adapter.  also provides out-of-the-box diffing and animated deletions, inserts, moves and changes.


Everything you need to implement your own lists:
- Easy to use RecyclerView
- Diffable
- Header and footer
- Pagination
- Collapsiple
- Loading footer
- Empty View
- Filterable
- Multiple data type

<img width="290" alt="animation" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/animation.gif"> <img width="290" alt="expandable" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/expandable.gif"> <img width="290" alt="filtering" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/filtering.gif"> <img width="290" alt="message" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/message.gif"> <img width="290" alt="layout" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/layout.gif"> <img width="290" alt="pagination" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/pagination.gif"> <img width="290" alt="sectioned" src="https://raw.githubusercontent.com/MostafaTaghipour/AndroidEasyList/master/screenshots/sectioned.gif">


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
    implementation 'com.github.mostafataghipour:androideasylist:1.0.8'
}
```



## Usage
1. Define your model
```kotlin
data class Movie(
    val id : String,
    val title : String
)

//optional: If you want to use DiffUtil capabilities (e.g. automatic animations like delete, insert, move , reload)
//          inherit 'Diffable' ptotocol
: Diffable {
    override val diffableIdentity: String
        get() = title!!

    //optional: this function need for automatic reload
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
            mMovieTitle?.text = item.title
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

3. Set Data
```kotlin
    adapter.items = yourItems
```

4. That's it, for more samples please see example project

## Thanks for

- [Android-Pagination-with-RecyclerView](https://github.com/Suleiman19/Android-Pagination-with-RecyclerView)
- [Pinned-section-item-decoration](https://github.com/takahr/pinned-section-item-decoration)
- [Generic ViewHolder](https://medium.com/@AlexeyBuzdin/generic-viewholder-for-android-63bf9e0db06a)

## Author

Mostafa Taghipour, mostafa@taghipour.me

## License

AndroidEasyList is available under the MIT license. See the LICENSE file for more info.

