package ir.rainyday.listexample

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ir.rainyday.listexample.api.MovieApi
import ir.rainyday.listexample.model.Movie
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by mostafa-taghipour on 12/22/17.
 */


object AppHelpers {



     fun formatYearLabel(result: Movie): String {
        return (result.releaseDate?.substring(0, 4)  // we want the year only

                + " | "
                + result.originalLanguage?.toUpperCase())
    }

     fun loadImage(context: Context,posterPath: String,width:Int=150): DrawableRequestBuilder<String> {
        return Glide
                .with(context)
                .load(MovieApi.BASE_IMAGE_URL + "w$width" + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
    }

}


object DateUtils {


    /**
     * Gets timestamp in millis and converts it to HH:mm (e.g. 16:44).
     */
    fun formatTime(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }

    fun formatTimeWithMarker(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }

    fun getHourOfDay(timeInMillis: Long): Int {
        val dateFormat = SimpleDateFormat("H", Locale.getDefault())
        return Integer.valueOf(dateFormat.format(timeInMillis))
    }

    fun getMinute(timeInMillis: Long): Int {
        val dateFormat = SimpleDateFormat("m", Locale.getDefault())
        return Integer.valueOf(dateFormat.format(timeInMillis))
    }

    /**
     * If the given time is of a different date, display the date.
     * If it is of the same date, display the time.
     * @param timeInMillis  The time to convert, in milliseconds.
     * @return  The time or date.
     */
    fun formatDateTime(timeInMillis: Long): String {
        return if (isToday(timeInMillis)) {
            formatTime(timeInMillis)
        } else {
            formatDate(timeInMillis)
        }
    }

    /**
     * Formats timestamp to 'date month' format (e.g. 'February 3').
     */
    fun formatDate(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }

    /**
     * Returns whether the given date is today, based on the user's current locale.
     */
    fun isToday(timeInMillis: Long): Boolean {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val date = dateFormat.format(timeInMillis)
        return date == dateFormat.format(System.currentTimeMillis())
    }

    /**
     * Checks if two dates are of the same day.
     * @param millisFirst   The time in milliseconds of the first date.
     * @param millisSecond  The time in milliseconds of the second date.
     * @return  Whether {@param millisFirst} and {@param millisSecond} are off the same day.
     */
    fun hasSameDate(millisFirst: Long, millisSecond: Long): Boolean {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(millisFirst).equals(dateFormat.format(millisSecond))
    }
}// This class should not be initialized



class NoEnterEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Just ignore the [Enter] key
            true
        } else super.onKeyDown(keyCode, event)
        // Handle all other keys in the default way
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val conn = super.onCreateInputConnection(outAttrs)
        outAttrs.imeOptions = outAttrs.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION.inv()
        return conn
    }
}




fun randFloat(min: Float, max: Float): Float {

    val rand = Random()

    return rand.nextFloat() * (max - min) + min

}

class DynamicHeightNetworkImageView : ImageView {
    private var mAspectRatio = 1.5f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    fun setAspectRatio(aspectRatio: Float) {
        mAspectRatio = aspectRatio
        requestLayout()
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = getMeasuredWidth()
        setMeasuredDimension(measuredWidth, (measuredWidth / mAspectRatio).toInt())
    }
}