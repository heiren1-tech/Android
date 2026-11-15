package com.example.layoutlab

import android.app.Activity
import android.os.Bundle

/** 实验1：线性布局 —— 4x4 按钮网格，见 res/layout/activity_linear.xml */
class LinearLayoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear)
    }
}
