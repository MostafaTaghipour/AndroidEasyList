package ir.rainyday.listexample.modules.sectioned

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
class SectionedViewModel() : ViewModel() {

    val items = MutableLiveData<MutableList<Any>>()

    init {
        //init service and load data
        items.value = mutableListOf<Any>()
        loadPage()
    }


    fun loadPage() {


        MovieApi.getTopRatedMovies(1,
                success = { movies ->


                    val temp = movies.toMutableList()
                    temp.sortBy { it.title }
                    val grouped = temp.groupBy { it.title!![0] }

                    val list:MutableList<Any> = ArrayList<Any>()

                    grouped.forEach {
                        list.add(it.key)

                        it.value.forEach {
                            list.add(it)
                        }

                        list.add(it.value.size)
                    }

                    items.value = list

                },
                failure = {
                })

    }


}