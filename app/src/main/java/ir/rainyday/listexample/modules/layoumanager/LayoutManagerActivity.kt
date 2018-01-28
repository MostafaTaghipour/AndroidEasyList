package ir.rainyday.listexample.modules.layoumanager

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.content_layout_manager.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*


class LayoutManagerActivity : AppCompatActivity() {

    lateinit var adapter: LayoutManagerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager

    private val viewModel: LayoutManagerViewModel by lazy {
        ViewModelProviders.of(this).get(LayoutManagerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_recycler.layoutManager = layoutManager
        adapter = LayoutManagerAdapter(this)
        main_recycler.adapter = adapter

        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_layout_manager, menu)
        menu?.findItem(R.id.linear)?.isChecked = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var type = LayoutType.Linear
        when (item?.itemId) {
            R.id.linear -> {
                item.isChecked = true
                type = LayoutType.Linear
            }
            R.id.grid -> {
                item.isChecked = true
                type = LayoutType.Grid
            }
            R.id.staggered -> {
                item.isChecked = true
                type = LayoutType.Staggered
            }

            R.id.spanned -> {
                item.isChecked = true
                type = LayoutType.Spanned
            }

            R.id.flow -> {
                item.isChecked = true
                type = LayoutType.Flow
            }
        }
        setRecyclerViewLayoutManager(type)
        return super.onOptionsItemSelected(item)
    }


    private fun setRecyclerViewLayoutManager(layoutManagerType: LayoutType) {
        var scrollPosition = 0

        // If a layout manager has already been set, get current scroll position.
        if (main_recycler.layoutManager != null) {
            scrollPosition = (main_recycler.layoutManager as? LinearLayoutManager)
                    ?.findFirstCompletelyVisibleItemPosition() ?: 0
        }

        layoutManager = when (layoutManagerType) {
            LayoutType.Linear -> {
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }
            LayoutType.Grid -> {
                GridLayoutManager(this, 2)
            }
            LayoutType.Spanned -> {
                val gridLayoutManager = GridLayoutManager(this, 4)
                gridLayoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                       if (position<4)
                            return 2

                        return 1
                    }
                }
                gridLayoutManager

            }
            LayoutType.Staggered  -> {
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
            }
            LayoutType.Flow -> {
                val flowLayoutManager = FlowLayoutManager()
                flowLayoutManager.setAlignment(Alignment.LEFT)
                flowLayoutManager.isAutoMeasureEnabled = true
                flowLayoutManager
            }
        }
        main_recycler.layoutManager = layoutManager
        adapter.layoutType=layoutManagerType
        main_recycler.scrollToPosition(scrollPosition)
    }
}


enum class LayoutType(val value : Int) {
    Linear(1),
    Grid(2),
    Spanned(3),
    Staggered(4),
    Flow(5)
}


