package com.example.componentlab

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：OptionsMenu 选项菜单
 *
 * - 通过 Activity 的 onCreateOptionsMenu 生成菜单（只在菜单第一次生成时调用）
 * - 使用 MenuInflater 扩充 XML 菜单资源
 * - onOptionsItemSelected 处理选中的菜单项
 */
class DemoOptionMenuActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_optionmenu)
        tvResult = findViewById(R.id.tv_result)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val message = "选择了菜单项：${item.title}"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        tvResult.text = message
        return true
    }
}
