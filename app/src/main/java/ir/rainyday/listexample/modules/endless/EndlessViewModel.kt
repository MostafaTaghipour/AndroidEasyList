package ir.rainyday.listexample.modules.endless

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.graphics.pdf.PdfDocument
import ir.rainyday.listexample.api.MovieApi
import ir.rainyday.listexample.api.MovieService
import ir.rainyday.listexample.model.Movie
import ir.rainyday.listexample.model.PopularMovies
import ir.rainyday.listexample.model.TopRatedMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Handler

/**
 * Created by mostafa-taghipour on 11/29/17.
 */
class EndlessViewModel() : ViewModel() {

    val items = MutableLiveData<MutableList<Any>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()
    private var popularLoading: Boolean = false

    init {
        //init service and load data
        items.value = ArrayList()
        loadPage(0)
    }


    fun loadPage(page: Int, clearOld: Boolean = false) {
        error.value = null
        loading.value = true




            MovieApi.getTopRatedMovies(page + 1,
                    success = { movies ->
                        loading.value = false

                        val list = items.value

                        if (clearOld)
                            list?.clear()

                        list?.addAll(movies)
                        items.value = list


                        loadPopulars(page )
                    },
                    failure = {
                        loading.value = false
                        error.value = "Some thing wrong"
                    })




    }


    fun loadPopulars(page: Int) {

        if (popularLoading)
            return

        popularLoading = true


        MovieApi.getPopularMovies(page + 1,
                success = { popularMovies ->
                    popularLoading = false

                    val list = items.value

                    list?.add(popularMovies)
                    items.value = list
                },
                failure = {
                    popularLoading = false
                })

    }


}