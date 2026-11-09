package com.example.componentlab

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 实验3-3：使用 XML 定义菜单
 *
 * - 字体大小：小(10sp) / 中(16sp) / 大(20sp) —— 点击后设置测试文本字号
 * - 普通菜单项 —— 点击后弹出 Toast 提示
 * - 字体颜色：红色 / 黑色 —— 点击后设置测试文本颜色
 */
class Exp3MenuActivity : AppCompatActivity() {

    private lateinit var tvContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp3_menu)
        tvContent = findViewById(R.id.tv_content)
    }

    /** 用 MenuInflater 把 XML 菜单资源转换成可编程对象 */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_exp3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 字体大小
            R.id.size_small -> tvContent.textSize = 10f
            R.id.size_medium -> tvContent.textSize = 16f
            R.id.size_large -> tvContent.textSize = 20f

            // 普通菜单项
            R.id.menu_normal -> {
                Toast.makeText(this, R.string.normal_item_toast, Toast.LENGTH_SHORT).show()
            }

            // 字体颜色
            R.id.color_red -> tvContent.setTextColor(Color.RED)
            R.id.color_black -> tvContent.setTextColor(Color.BLACK)

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
