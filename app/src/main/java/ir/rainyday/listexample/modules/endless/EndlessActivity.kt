package ir.rainyday.listexample.modules.endless

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.LoadingFooterAdapter
import ir.rainyday.easylist.PaginatedRecyclerOnScrollListener
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.activity_endless.*
import kotlinx.android.synthetic.main.content_endless.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*
import kotlinx.android.synthetic.main.progress_layout.*


class EndlessActivity : AppCompatActivity(), EndlessAdapter.CallbackListener {


    private var scrollListener: PaginatedRecyclerOnScrollListener? = null

    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        main_recycler?.layoutManager = linearLayoutManager
        scrollListener = object : PaginatedRecyclerOnScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int) {
                viewModel.loadPage(page)
            }

            override fun onPageChanged(currentPage: Int) {
                Log.d("EndlessActivity", "currentPage: $currentPage")
                //Toast.makeText(this@EndlessActivity,"currentPage: $currentPage",Toast.LENGTH_SHORT).show()
            }
        }
        scrollListener?.pageSize = 21
        main_recycler.addOnScrollListener(scrollListener!!)
        main_recycler
    }

    private val adapter: EndlessAdapter by lazy {

        val adapter = EndlessAdapter(this)
        adapter.setListener(this)
        adapter

    }

    private val viewModel: EndlessViewModel by lazy {
        ViewModelProviders.of(this).get(EndlessViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endless)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);


        recyclerView.adapter = adapter
        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
        })

        swipe_refresh_layout.setOnRefreshListener {
            scrollListener?.reset()
            viewModel.loadPage(0, true)
        }

        viewModel.loading.observe(this, Observer { visible ->

            if (visible == null)
                return@Observer


            if (adapter.itemCount == 0) {
                main_progress.visibility = if (visible) View.VISIBLE else View.GONE
            } else if (swipe_refresh_layout.isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            } else {
                (adapter as LoadingFooterAdapter).loading = visible
            }
        })

        viewModel.error.observe(this, Observer { error ->

            val hasError = error != null

            if (adapter.itemCount == 0) {
                error_layout.visibility = if (hasError) View.VISIBLE else View.GONE
                error_txt_cause.text = error
            } else {
                adapter.error = error
                (adapter as LoadingFooterAdapter).retry = hasError

            }
        })

        fab.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }

        error_btn_retry.setOnClickListener {
            loadNextPage()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRetryClicked() {
        loadNextPage()
    }

    private fun loadNextPage() {
        viewModel.loadPage(scrollListener!!.lastLoadedPage)
    }

}


