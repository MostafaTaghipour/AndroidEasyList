package ir.rainyday.easylist

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class DisableChangeItemAnimator : DefaultItemAnimator() {
    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchRemoveFinished(holder)
        return false
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchAddFinished(holder)
        return false
    }

    override fun animateMove(holder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        dispatchMoveFinished(holder)
        return false
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder?, newHolder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        dispatchChangeFinished(oldHolder, true)
        dispatchChangeFinished(newHolder, false)
        return false
    }

    override fun runPendingAnimations() { // stub
    }


    override fun endAnimations() { // stub
    }

    override fun isRunning(): Boolean {
        return false
    }
}


public var RecyclerView.disableChangeAnimation: Boolean
    set(value) {
        if(value) {
            val animator = DisableChangeItemAnimator()
            this.itemAnimator = animator
        }
        else{
            val animator = DefaultItemAnimator()
            animator.supportsChangeAnimations = true
            this.itemAnimator = animator
        }
    }
    get() {
        if (this.itemAnimator is DisableChangeItemAnimator)
            return false

        return (this.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations ?: false
    }