package ir.rainyday.listexample.modules.filtring

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import ir.rainyday.listexample.api.MovieApi
import ir.rainyday.listexample.api.MovieService
import ir.rainyday.listexample.model.Movie
import ir.rainyday.listexample.model.TopRatedMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by mostafa-taghipour on 11/29/17.
 */
class FilteringViewModel() : ViewModel() {

    private var originalItems = ArrayList<Movie>()
    private var query: String? = null
    private var sortFactor: SortFactor? = null

    val items = MutableLiveData<MutableList<Movie>>()

    init {
        items.value = mutableListOf<Movie>()
        loadPage()
    }


    private fun loadPage() {
        MovieApi.getTopRatedMovies(1,
                success = { movies ->
                    originalItems = ArrayList(movies)
                    updateItems()
                },
                failure = {
                })
    }

    private fun updateItems() {
        var list  = originalItems

        if (!query.isNullOrEmpty())
            list = list.filter { it.title!!.toLowerCase().contains(query!!.toLowerCase()) } as ArrayList<Movie>


        if (sortFactor != null)
            list.sortBy {

                when (sortFactor!!) {
                    SortFactor.Title -> it.title
                    SortFactor.Language -> it.originalLanguage
                    SortFactor.Year -> it.releaseDate
                }

            }

        items.value = list
    }

    fun setFilter(newText: String?) {
        query = newText
        updateItems()
    }

    fun sort(by: SortFactor) {
        sortFactor = by
        updateItems()
    }

}

enum class SortFactor {
    Title,
    Year,
    Language
}