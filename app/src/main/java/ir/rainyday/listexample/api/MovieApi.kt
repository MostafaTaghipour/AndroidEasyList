package ir.rainyday.listexample.api

import ir.rainyday.listexample.model.Movie
import ir.rainyday.listexample.model.PopularMovies
import ir.rainyday.listexample.model.TopRatedMovies
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by mostafa-taghipour on 11/28/17.
 */

object MovieApi {


    private var retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .client(buildClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build()
            }
            return retrofit!!
        }

    private fun buildClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.readTimeout(60, TimeUnit.SECONDS)
        client.connectTimeout(60, TimeUnit.SECONDS)
        client.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        return client.build()
    }

    val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
    val BASE_URL = "https://api.themoviedb.org/3/movie/"
    val API_KEY = "ec01f8c2eb6ac402f2ca026dc2d9b8fd"
    private val movieService: MovieService = MovieApi.client.create(MovieService::class.java)

    fun getTopRatedMovies(page: Int, success: (List<Movie>) -> Unit, failure: (Throwable) -> Unit) {
        movieService.getTopRatedMovies(pageIndex = page).enqueue(object : Callback<TopRatedMovies> {
            override fun onResponse(call: Call<TopRatedMovies>, response: Response<TopRatedMovies>) {
               response.body()?.results?.let {
                   success.invoke(it)
               }
            }

            override fun onFailure(call: Call<TopRatedMovies>, t: Throwable) {
                t.printStackTrace()
                failure.invoke(t)
            }
        })
    }


    fun getPopularMovies(page: Int, success: (PopularMovies) -> Unit, failure: (Throwable) -> Unit) {
        movieService.getPopularMovies(pageIndex = page).enqueue(object : Callback<PopularMovies> {
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                response.body()?.let {
                    success.invoke(it)
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                t.printStackTrace()
                failure.invoke(t)
            }
        })
    }
}