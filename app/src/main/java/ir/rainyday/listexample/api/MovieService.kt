package ir.rainyday.listexample.api

/**
 * Created by mostafa-taghipour on 11/28/17.
 */


import ir.rainyday.listexample.model.PopularMovies
import ir.rainyday.listexample.model.TopRatedMovies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

interface MovieService {

    @GET("top_rated")
    fun getTopRatedMovies(
            @Query("api_key") apiKey: String=MovieApi.API_KEY,
            @Query("language") language: String="en_US",
            @Query("page") pageIndex: Int
    ): Call<TopRatedMovies>

    @GET("popular")
    fun getPopularMovies(
            @Query("api_key") apiKey: String=MovieApi.API_KEY,
            @Query("language") language: String="en_US",
            @Query("page") pageIndex: Int
    ): Call<PopularMovies>

}