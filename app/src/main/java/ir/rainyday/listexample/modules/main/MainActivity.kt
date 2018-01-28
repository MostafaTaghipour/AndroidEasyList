package ir.rainyday.listexample.modules.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.layout_regular_appbar.*
import ir.rainyday.listexample.R
import ir.rainyday.listexample.modules.animation.AnimationActivity
import ir.rainyday.listexample.modules.endless.EndlessActivity
import ir.rainyday.listexample.modules.expandable.ExpandableActivity
import ir.rainyday.listexample.modules.filtring.FilteringActivity
import ir.rainyday.listexample.modules.layoumanager.LayoutManagerActivity
import ir.rainyday.listexample.modules.layoumanager.LayoutManagerAdapter
import ir.rainyday.listexample.modules.messaging.MessagingActivity
import ir.rainyday.listexample.modules.sectioned.SectionedActivity


class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        btnEndless.setOnClickListener {
            startActivity(Intent(this, EndlessActivity::class.java))
        }

        btnSectioned.setOnClickListener {
            startActivity(Intent(this, SectionedActivity::class.java))
        }

        btnFilter.setOnClickListener {
            startActivity(Intent(this, FilteringActivity::class.java))
        }

        btnAnimation.setOnClickListener {
            startActivity(Intent(this, AnimationActivity::class.java))
        }

        btnMessage.setOnClickListener {
            startActivity(Intent(this, MessagingActivity::class.java))
        }

        btnExpandable.setOnClickListener {
            startActivity(Intent(this, ExpandableActivity::class.java))
        }

        btnLayoutManager.setOnClickListener {
            startActivity(Intent(this, LayoutManagerActivity::class.java))
        }
    }



}
