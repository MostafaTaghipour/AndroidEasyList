package ir.rainyday.listexample.modules.animation

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ir.rainday.easylist.DragAndSwipeItemTouchHelperCallback
import ir.rainday.easylist.GenericViewHolder
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.DateModel
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.android.synthetic.main.content_filtering.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*
import java.text.SimpleDateFormat
import java.util.*


class AnimationActivity : AppCompatActivity(),
        GenericViewHolder.OnItemClicked<DateModel>,
        DragAndSwipeItemTouchHelperCallback.Callback,
        AnimationAdapterCallback {

    val adapter = AnimationAdapter(this, this)


    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        main_recycler?.layoutManager = linearLayoutManager


        //load animation
        val animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        main_recycler.layoutAnimation = animation
        main_recycler.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {

                //add divider
                val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this@AnimationActivity,
                        linearLayoutManager.orientation)
                main_recycler.addItemDecoration(dividerItemDecoration)
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        }


        //change item animator
        main_recycler.itemAnimator = SlideInLeftAnimator()

        //set adapter
        adapter.listener = this
        main_recycler.adapter = adapter


        main_recycler.post {
            adapter.lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
        }

        main_recycler
    }


    private val viewModel: AnimationViewModel by lazy {
        ViewModelProviders.of(this).get(AnimationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtering)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);


        //populate adapter
        viewModel.items.observe(this, Observer { list ->
            adapter.items = list
        })

        // implement swipe and drag
        val callback = DragAndSwipeItemTouchHelperCallback(this)
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_animation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.add -> {
                viewModel.addNewItem()
                recyclerView.scrollToPosition(0)
            }
            R.id.edit -> {
                enableActionMode()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //drag and swipe
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.swapItems(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        viewModel.removeItem(position)
        val snackbar = Snackbar.make(this.findViewById<View>(android.R.id.content), "Item deleted successful", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.undoDelete()
        }
        snackbar.show()
    }

    override fun getSwipeDirections(): Int {
        return ItemTouchHelper.START
    }

    override fun isLongPressDragEnabled(): Boolean {
        return actionMode != null
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return actionMode != null
    }


    override fun onRecyclerViewItemClicked(adapter: RecyclerView.Adapter<*>, view: View, position: Int, item: DateModel) {
        if (actionMode == null)
            return

        this.adapter.toggleSelection(position)

        val count = this.adapter.getSelectedItemCount()
        setActionModeTitle(if (count > 0) "$count item selected" else null)
    }
    override fun update(item: DateModel, position: Int) {

        if (actionMode == null)
            return

        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)

        val alert = AlertDialog.Builder(this)
        val editText = EditText(this)
        alert.setMessage("Edit item:")
        alert.setTitle(null)
        alert.setView(editText)

        editText.setText(dateFormat.format(item.date))

        alert.setPositiveButton("OK") { _, _ ->
            //What ever you want to do with the value
            val str = editText.text.toString()
            val date = dateFormat.parse(str)
            viewModel.update(date, position)
        }

        alert.setNegativeButton("Cancel") { _, _ ->
            // what ever you want to do with No option.
        }

        alert.show()
    }

    //Actionbar
    private var actionModeCallback = ActionModeCallback()
    private var actionMode: ActionMode? = null

    private inner class ActionModeCallback : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.menu_action_mode, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_delete -> {
                    viewModel.removeItems(adapter.getSelectedItems())

                    Handler().postDelayed({
                        adapter.clearSelections()
                        setActionModeTitle()
                    }, 400)

                    true
                }

                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            disableActionMode()
        }
    }

    private fun setActionModeTitle(title: CharSequence? = null) {
        actionMode?.title = title ?: "Modify"
        actionMode?.invalidate()
    }

    private fun enableActionMode() {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback)
            setActionModeTitle("Modify")
            adapter.actionMode = true
            adapter.notifyDataSetChanged()
        }
    }

    private fun disableActionMode() {
        adapter.clearSelections()
        actionMode = null
        adapter.actionMode = false
        adapter.notifyDataSetChanged()
    }


}


