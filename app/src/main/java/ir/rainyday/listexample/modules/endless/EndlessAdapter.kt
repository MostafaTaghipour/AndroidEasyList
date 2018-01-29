package ir.rainyday.listexample.modules.endless

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import ir.rainday.easylist.RecyclerViewAdapter
import ir.rainday.easylist.GenericViewHolder
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie
import ir.rainyday.listexample.model.PopularMovies
import java.lang.ref.WeakReference
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import ir.rainday.easylist.GravitySnapHelper
import ir.rainday.easylist.LoadingFooterAdapter


/**
 * Created by mostafa-taghipour on 12/22/17.
 */


class EndlessAdapter(context: Context) :
        RecyclerViewAdapter<Any>(context),
        LoadingFooterAdapter, PopularsViewHolder.Contract {

    companion object {
        // View Types
        private val ITEM = 0
        private val HERO = 1
        private val POPULAR = 2
    }

    var error: String? = null
    private var listener: WeakReference<CallbackListener>? = null


    override var innerListPositions: HashMap<Int, Int> = HashMap<Int, Int>()

    fun setListener(listener: CallbackListener) {
        this.listener = WeakReference<CallbackListener>(listener)
    }

    override fun getLayout(viewType: Int): Int {
        return 0
    }


    override fun generateViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder {
        return when(viewType){
            POPULAR->{
                val view = inflater.inflate( R.layout.item_popular_list, viewGroup,false)
                PopularsViewHolder(context,view,this)
            }
            HERO->{
                val view = inflater.inflate( R.layout.item_hero, viewGroup,false)
                HeroViewHolder(context,view)
            }
            else ->{
                val view = inflater.inflate( R.layout.item_list, viewGroup,false)
                ItemViewHolder(context,view)
            }
        }
    }


    override fun bindView(item: Any, position: Int, viewHolder: RecyclerView.ViewHolder) {

        when
        {
            (viewHolder is ItemViewHolder)-> viewHolder.bindView(item as Movie)
            (viewHolder is HeroViewHolder)-> viewHolder.bindView(item as Movie)
            (viewHolder is PopularsViewHolder)-> viewHolder.bindView(item as PopularMovies,position)
        }

    }

    override fun getItemType(position: Int): Int {
        return if (items!![position] is PopularMovies) POPULAR else if (position % 10 == 0) HERO else ITEM
    }

    override val adapter: RecyclerViewAdapter<*> = this

    override fun getLoadingLayout(): Int {
        return R.layout.item_progress
    }

    override fun getRetryLayout(): Int? {
        return R.layout.item_retry
    }

    override fun bindRetryView(viewHolder: RecyclerView.ViewHolder) {
       viewHolder as GenericViewHolder

        val errorText = viewHolder.getView<TextView>(R.id.loadmore_errortxt)
        val view = viewHolder.getView<View>(R.id.loadmore_errorlayout)!!
        val loadMoreButton = viewHolder.getView<ImageButton>(R.id.loadmore_retry)!!
        errorText?.text = error


        if (!loadMoreButton.hasOnClickListeners()) {
            loadMoreButton.setOnClickListener {
                listener?.get()?.onRetryClicked()
            }
        }


        if (!view.hasOnClickListeners()) {
            view.setOnClickListener {
                listener?.get()?.onRetryClicked()
            }
        }
    }


    interface CallbackListener {
        fun onRetryClicked()
    }
}



class ItemViewHolder(var context: Context,itemView:View):RecyclerView.ViewHolder(itemView){
    private val mMovieTitle: TextView = itemView.findViewById<TextView>(R.id.movie_title)
    private val mMovieDesc: TextView? = itemView.findViewById<TextView>(R.id.movie_desc)
    private val mYear: TextView? = itemView.findViewById<TextView>(R.id.movie_year)
    private val mPosterImg: ImageView? = itemView.findViewById<ImageView>(R.id.movie_poster)

    fun bindView(item: Movie){

        mMovieTitle.text = item.title
        mYear?.text = AppHelpers.formatYearLabel(item)
        mMovieDesc?.text = item.overview

        // load movie thumbnail
        AppHelpers.loadImage(context, item.posterPath!!)
                .into(mPosterImg)
    }

}

class HeroViewHolder(var context: Context,itemView:View):RecyclerView.ViewHolder(itemView){
    private val mMovieTitle: TextView = itemView.findViewById<TextView>(R.id.movie_title)
    private val mMovieDesc: TextView? = itemView.findViewById<TextView>(R.id.movie_desc)
    private val mYear: TextView? = itemView.findViewById<TextView>(R.id.movie_year)
    private val mPosterImg: ImageView? = itemView.findViewById<ImageView>(R.id.movie_poster)

    fun bindView(item: Movie){

        mMovieTitle.text = item.title
        mYear?.text = AppHelpers.formatYearLabel(item)
        mMovieDesc?.text = item.overview

        item.backdropPath?.let {
            AppHelpers.loadImage(context, it,500)
                    .into(mPosterImg)
        }
    }

}

class PopularsViewHolder(var context: Context,itemView:View,var contract: Contract):RecyclerView.ViewHolder(itemView){
    private var recyclerView : RecyclerView
    private var popularAdapter: RecyclerViewAdapter<Movie>
    private var currentPosition:Int=0


    init {
        recyclerView=itemView.findViewById<RecyclerView>(R.id.horizontal_recyclerview)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        popularAdapter = object : RecyclerViewAdapter<Movie>(context){
            override fun getLayout(viewType: Int): Int {
                return R.layout.item_popular
            }

            override fun bindView(item: Movie, position: Int, viewHolder: RecyclerView.ViewHolder) {
                viewHolder as GenericViewHolder
                val imageView = viewHolder.getView<ImageView>(R.id.image_view)
                AppHelpers.loadImage(context, item.posterPath!!)
                        .into(imageView)
            }
        }
        recyclerView.adapter = popularAdapter
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
        //keep inner list position
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemPosition = (recyclerView!!.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                contract.innerListPositions[currentPosition] = itemPosition
            }
        })
    }

    fun bindView(item: PopularMovies, position: Int){
        currentPosition=position
        popularAdapter.items = item.movies
        contract.innerListPositions[position]?.let {
            recyclerView.post {
                recyclerView.scrollToPosition(it)
            }
        }
    }

    interface Contract {
        var innerListPositions : HashMap<Int, Int>
    }

}