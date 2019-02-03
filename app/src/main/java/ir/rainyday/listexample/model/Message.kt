package ir.rainyday.listexample.model

import ir.rainyday.easylist.Diffable
import ir.rainyday.listexample.R

/**
 * Created by mostafa-taghipour on 12/26/17.
 */

data class Message(
        val message: String,
        val sender: User,
        val createdAt: Long
) : Diffable {
    override val diffableIdentity: String
        get() = createdAt.toString()

    override fun isEqualTo(other: Any): Boolean {

        if (other is Message)
            return this == other

        return false
    }
}

data class User(val nickname: String,
                val avatar: Int)

var me: User = User("me", R.drawable.me)
var sarah: User = User("sarah", R.drawable.sarah)