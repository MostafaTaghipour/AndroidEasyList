package ir.rainyday.listexample.model

import ir.rainday.easylist.Diffable
import java.util.*

/**
 * Created by mostafa-taghipour on 1/16/18.
 */
data class DateModel (
        val date:Date
): Diffable {
    override val diffableIdentity: String
        get() = date.toString()

    override fun isEqualTo(other: Any): Boolean {
        return if (other is DateModel) this==other else false
    }
}