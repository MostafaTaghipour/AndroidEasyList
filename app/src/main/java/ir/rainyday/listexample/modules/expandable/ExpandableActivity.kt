package ir.rainyday.listexample.modules.expandable

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import ir.rainyday.listexample.R

import kotlinx.android.synthetic.main.layout_regular_appbar.*
import kotlinx.android.synthetic.main.content_expandable.*


class ExpandableActivity : AppCompatActivity() {

    private val viewModel: ExpandableViewModel by lazy {
        ViewModelProviders.of(this).get(ExpandableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_recycler.layoutManager = linearLayoutManager
        val adapter = ExpandableAdapter(this, linearLayoutManager)
        main_recycler.adapter = adapter

        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
