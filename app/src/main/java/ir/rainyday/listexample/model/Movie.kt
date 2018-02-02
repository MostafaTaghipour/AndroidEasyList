package ir.rainyday.listexample.model

import com.google.gson.annotations.SerializedName
import ir.rainday.easylist.Diffable
import ir.rainyday.listexample.randFloat

/**
 * Created by mostafa-taghipour on 1/5/18.
 */

data class TopRatedMovies(@SerializedName("results") val results: List<Movie>)

data class PopularMovies(@SerializedName("results") val movies: List<Movie>) : Diffable {
    override val diffableIdentity: String
        get() {

            val first = movies.firstOrNull()
            val last = movies.lastOrNull()

            if (first!=null && last!=null){
               return first.diffableIdentity+last.diffableIdentity
            }

           return ""
        }

    override fun isEqualTo(other: Any): Boolean {
        return if (other is PopularMovies) return this == other else false
    }
}

data class Movie(
        @SerializedName("poster_path")
        val posterPath: String? = null,

        @SerializedName("overview")
        val overview: String? = null,

        @SerializedName("release_date")
        val releaseDate: String? = null,

        @SerializedName("original_language")
        val originalLanguage: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("backdrop_path")
        val backdropPath: String? = null,


        var ratio: Float = randFloat(0.5f, 1f)
)
: Diffable {
    override val diffableIdentity: String
        get() = title!!

    override fun isEqualTo(other: Any): Boolean {
        return if (other is Movie) return this == other else false
    }
}