package ir.rainyday.listexample.modules.filtring

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.FilterableAdapter
import ir.rainyday.easylist.GenericViewHolder
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.easylist.setEmptyView
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie
import kotlinx.android.synthetic.main.content_filtering.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*


class FilteringActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var searchView: SearchView

    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        main_recycler?.layoutManager = linearLayoutManager

        main_recycler
    }

    private val adapter: RecyclerViewAdapter<Movie> by lazy {

        val adapter = object : RecyclerViewAdapter<Movie>(), FilterableAdapter {
            override fun getLayout(viewType: Int): Int {
                return  R.layout.item_movie
            }

            override fun bindView(item: Movie, position: Int, viewHolder: RecyclerView.ViewHolder) {
                viewHolder as GenericViewHolder
                val mMovieTitle: TextView? = viewHolder.getView(R.id.movie_title)
                val mMovieDesc: TextView? = viewHolder.getView(R.id.movie_desc)
                val mYear: TextView? = viewHolder.getView(R.id.movie_year)
                val mPosterImg: ImageView? = viewHolder.getView(R.id.movie_poster)


                mMovieTitle?.text = item.title
                mYear?.text = AppHelpers.formatYearLabel(item)
                mMovieDesc?.text = item.overview

                // load movie thumbnail
                AppHelpers.loadImage(viewHolder.itemView.context, item.posterPath!!)
                        .into(mPosterImg)

            }


            override fun filterItem(constraint: CharSequence, item: Any): Boolean {
                return (item as Movie).title!!.toLowerCase().contains(constraint.toString().toLowerCase())
            }
        }
        adapter

    }

    private val viewModel: FilteringViewModel by lazy {
        ViewModelProviders.of(this).get(FilteringViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtering)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);


        recyclerView.adapter = adapter

        recyclerView.setEmptyView(R.layout.layout_no_item)

        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
            recyclerView.scrollToPosition(0)
        })

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_filtering, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu.findItem(R.id.search)
        searchView = searchMenuItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.sort_by_lang->viewModel.sort(SortFactor.Language)
            R.id.sort_by_year->viewModel.sort(SortFactor.Year)
            R.id.sort_by_title->viewModel.sort(SortFactor.Title)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        (adapter as FilterableAdapter).setFilterConstraint(newText)
        return true
    }

}

