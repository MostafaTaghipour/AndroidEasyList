package ir.rainyday.listexample.modules.expandable

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
class ExpandableViewModel() : ViewModel() {

    val items = MutableLiveData<MutableList<Any>>()

    init {
        //init service and load data
        items.value = mutableListOf<Any>()
        loadPage()
    }


    private fun loadPage() {


        MovieApi.getTopRatedMovies(1,
                success = { movies ->
                    val temp = movies.toMutableList()
                    temp.sortByDescending { it.releaseDate }
                    val grouped = temp.groupBy { it.releaseDate!!.substring(0,4)}

                    val list:MutableList<Any> = ArrayList<Any>()

                    grouped.forEach {
                        list.add(it.key)

                        it.value.forEach {
                            list.add(it)
                        }
                    }

                    items.value = list
                },
                failure = {
                })
    }


}