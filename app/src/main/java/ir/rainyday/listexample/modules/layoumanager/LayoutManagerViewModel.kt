package ir.rainyday.listexample.modules.layoumanager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
class LayoutManagerViewModel() : ViewModel() {

    val items = MutableLiveData<MutableList<Movie>>()

    init {
        //init service and load data
        items.value = mutableListOf<Movie>()
        loadPage()
    }


    private fun loadPage() {


        MovieApi.getTopRatedMovies(1,
                success = { movies ->
                    items.value=movies.toMutableList()
                },
                failure = {
                })


    }


}