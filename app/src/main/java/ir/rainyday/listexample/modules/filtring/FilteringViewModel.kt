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

    val items = MutableLiveData<MutableList<Movie>>()

    init {
        items.value = mutableListOf<Movie>()
        loadPage()
    }


    private fun loadPage() {
        MovieApi.getTopRatedMovies(1,
                success = { movies ->
                    items.value = movies.toMutableList()
                },
                failure = {
                })
    }

    fun sort(by: SortFactor) {
        val list = items.value

        list?.sortBy {

            when (by) {
                SortFactor.Title -> it.title
                SortFactor.Language -> it.originalLanguage
                SortFactor.Year -> it.releaseDate
            }

        }

        items.value = list
    }

}

enum class SortFactor {
    Title,
    Year,
    Language
}