package ir.rainyday.easylist

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator


internal fun runAsync(action: () -> Unit) = Thread(Runnable(action)).start()

internal fun runOnUiThread(action: () -> Unit) {
    if (isMainLooperAlive()) {
        action()
    } else {
        Handler(Looper.getMainLooper()).post(Runnable(action))
    }
}

private fun isMainLooperAlive() = Looper.myLooper() == Looper.getMainLooper()


public var RecyclerView.disableChangeAnimation: Boolean
    set(value) {
        (this.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = !value
    }
    get() {
        return !((this.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations ?: true)
    }