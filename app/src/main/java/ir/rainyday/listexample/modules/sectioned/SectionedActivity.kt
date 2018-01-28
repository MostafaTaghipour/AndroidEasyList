package ir.rainyday.listexample.modules.sectioned

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.content_sectioned.*

import kotlinx.android.synthetic.main.layout_regular_appbar.*
import ir.rainday.easylist.PinnedHeaderItemDecoration


class SectionedActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_recycler?.layoutManager = linearLayoutManager

        // add PinnedHeader as recyclerview ItemDecoration
        main_recycler.addItemDecoration(PinnedHeaderItemDecoration())

        //handle Indexer
        fastScroll.attachRecyclerView(main_recycler)
        fastScroll.setSectionIndexer(adapter)
        fastScroll.setBubbleColor(ContextCompat.getColor(this, R.color.colorAccent))
        fastScroll.setBubbleTextColor(Color.WHITE)
        fastScroll.setHandleColor(ContextCompat.getColor(this, R.color.colorAccent))

        main_recycler
    }

    private val adapter: SectionedAdapter by lazy {

        val adapter = SectionedAdapter(this)
        adapter

    }

    private val viewModel: SectionedViewModel by lazy {
        ViewModelProviders.of(this).get(SectionedViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sectioned)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);


        recyclerView.adapter = adapter
        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
