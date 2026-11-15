package com.example.layoutlab

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

/** 实验主页：入口菜单，点击进入各布局实验界面 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_linear).setOnClickListener {
            startActivity(Intent(this, LinearLayoutActivity::class.java))
        }
        findViewById<Button>(R.id.btn_table).setOnClickListener {
            startActivity(Intent(this, TableLayoutActivity::class.java))
        }
        findViewById<Button>(R.id.btn_constraint1).setOnClickListener {
            startActivity(Intent(this, Constraint1Activity::class.java))
        }
        findViewById<Button>(R.id.btn_constraint2).setOnClickListener {
            startActivity(Intent(this, Constraint2Activity::class.java))
        }
        findViewById<Button>(R.id.btn_compose).setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }
    }
}
