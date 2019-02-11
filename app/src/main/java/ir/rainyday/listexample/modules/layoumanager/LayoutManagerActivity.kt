package ir.rainyday.listexample.modules.layoumanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import ir.rainyday.easylist.SpacingItemDecoration
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.content_layout_manager.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*


class LayoutManagerActivity : AppCompatActivity() {

    private lateinit var adapter: LayoutManagerAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var spacingItemDecoration: SpacingItemDecoration? = null
    private var sixteen: Int = 0
    private var eight: Int = 0

    private val viewModel: LayoutManagerViewModel by lazy {
        ViewModelProviders.of(this).get(LayoutManagerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_manager)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sixteen = resources.getDimension(R.dimen.activity_margin).toInt()
        eight = resources.getDimension(R.dimen.activity_margin_half).toInt()

        adapter = LayoutManagerAdapter(this)
        main_recycler.adapter = adapter
        setRecyclerViewLayoutManager(LayoutType.Linear)

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

        if (spacingItemDecoration != null) {
            main_recycler.removeItemDecoration(spacingItemDecoration!!)
            spacingItemDecoration = null
        }

        layoutManager = when (layoutManagerType) {
            LayoutType.Linear -> {
                val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

                spacingItemDecoration = SpacingItemDecoration(sixteen)
                main_recycler.addItemDecoration(spacingItemDecoration!!)
                main_recycler.setPadding(sixteen, sixteen, sixteen, sixteen)

                linearLayoutManager
            }
            LayoutType.Grid -> {
                val gridLayoutManager = GridLayoutManager(this, 2)

                spacingItemDecoration = SpacingItemDecoration(sixteen)
                main_recycler.addItemDecoration(spacingItemDecoration!!)
                main_recycler.setPadding(sixteen, sixteen, sixteen, sixteen)

                gridLayoutManager
            }
            LayoutType.Spanned -> {
                val gridLayoutManager = GridLayoutManager(this, 4)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (position < 4)
                            return 2

                        return 1
                    }
                }

                main_recycler.setPadding(eight, eight, eight, eight)

                gridLayoutManager

            }
            LayoutType.Staggered -> {
                val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

                spacingItemDecoration = SpacingItemDecoration(sixteen)
                main_recycler.addItemDecoration(spacingItemDecoration!!)
                main_recycler.setPadding(sixteen, sixteen, sixteen, sixteen)

                staggeredGridLayoutManager
            }
            LayoutType.Flow -> {
                val flowLayoutManager = FlowLayoutManager()
                flowLayoutManager.setAlignment(Alignment.LEFT)
                flowLayoutManager.isAutoMeasureEnabled = true
                flowLayoutManager
            }
        }
        main_recycler.layoutManager = layoutManager
        adapter.layoutType = layoutManagerType
        main_recycler.scrollToPosition(scrollPosition)
    }
}


enum class LayoutType(val value: Int) {
    Linear(1),
    Grid(2),
    Spanned(3),
    Staggered(4),
    Flow(5)
}


