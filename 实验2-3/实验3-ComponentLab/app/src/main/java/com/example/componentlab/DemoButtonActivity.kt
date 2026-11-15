package com.example.componentlab

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：Android 基本界面组件 —— Button / ImageButton / ToggleButton 的事件处理
 *
 * 演示两种写法：
 * 1. 匿名内部类形式：setOnClickListener(object : View.OnClickListener { ... })
 * 2. Lambda 表达式形式：setOnClickListener { ... }
 */
class DemoButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_button)

        val tvResult = findViewById<TextView>(R.id.tv_result)

        // 1) 普通 Button：Lambda 表达式形式
        findViewById<Button>(R.id.btn_normal).setOnClickListener {
            show(tvResult, getString(R.string.btn_normal))
        }

        // 2) ImageButton：匿名内部类形式
        findViewById<ImageButton>(R.id.btn_image).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                show(tvResult, getString(R.string.btn_image))
            }
        })

        // 3) ToggleButton：监听选中状态变化
        findViewById<ToggleButton>(R.id.btn_toggle).setOnCheckedChangeListener { _, isChecked ->
            show(tvResult, "ToggleButton " + if (isChecked) "开" else "关")
        }
    }

    private fun show(tv: TextView, text: String) {
        val message = getString(R.string.clicked, text)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        tv.text = message
    }
}
