package com.example.componentlab

import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：PopupMenu 弹出菜单
 *
 * PopupMenu 是锚定到某个 View 的模态菜单，适合为特定内容提供溢出样式菜单。
 */
class DemoPopupMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_popupmenu)

        val tvResult = findViewById<TextView>(R.id.tv_result)
        val button = findViewById<Button>(R.id.btn_show_popup)

        button.setOnClickListener { anchor ->
            val popup = PopupMenu(this, anchor)
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                val message = "选择了：${item.title}"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                tvResult.text = message
                true
            }
            popup.show()
        }
    }
}
