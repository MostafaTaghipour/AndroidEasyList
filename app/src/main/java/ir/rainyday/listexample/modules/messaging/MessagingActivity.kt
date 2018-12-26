package ir.rainyday.listexample.modules.messaging

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.listexample.R
import kotlinx.android.synthetic.main.content_messaging.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*


class MessagingActivity : AppCompatActivity() {

    val adapter = MessagingAdapter(this)

    private val recyclerView: RecyclerView by lazy {
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        reyclerview_message_list?.layoutManager = linearLayoutManager
        reyclerview_message_list.setHasFixedSize(true)
        reyclerview_message_list
    }


    private val viewModel: MessagingViewModel by lazy {
        ViewModelProviders.of(this).get(MessagingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        recyclerView.adapter=adapter

        viewModel.items.observe(this, Observer { messages ->
            adapter.items = messages
        })

        button_chatbox_send.setOnClickListener {
            sendMessage()
        }

        edittext_chatbox.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(text: Editable?) {
             button_chatbox_send.isEnabled = !(text.isNullOrEmpty())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })


        edittext_chatbox.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, i: Int, p2: KeyEvent?): Boolean {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    sendMessage()
                    return true;
                }
                return false;
            }
        })

    }

    private fun sendMessage() {
        val text = edittext_chatbox.text
        if (text.isNullOrEmpty())
            return

        viewModel.sendMessage(text!!.toString())
        recyclerView.scrollToPosition(0)
        edittext_chatbox.text = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}




