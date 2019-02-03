package ir.rainyday.listexample.modules.sectioned

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.PinnedHeaderItemDecoration
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.content_sectioned.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*


class SectionedActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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
