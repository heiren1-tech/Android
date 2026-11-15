package com.example.componentlab

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：ContextMenu 上下文菜单（浮动形式）
 *
 * - context menu 与某个具体 View 绑定，用 registerForContextMenu 为 ListView 注册
 * - 长按列表项时调用 onCreateContextMenu 生成菜单
 * - onContextItemSelected 处理选中的菜单项
 */
class DemoContextMenuActivity : AppCompatActivity() {

    private val books = listOf("疯狂Java讲义", "疯狂Ajax讲义", "疯狂XML讲义", "疯狂Android讲义")

    /** 记录被长按项的位置（MenuItem.getMenuInfo 需要 API 26，这里手动记录以保证兼容性） */
    private var contextPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_contextmenu)

        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            books
        )

        // 为 ListView 注册上下文菜单
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)

        // 记录被长按项的位置，并用它的内容作为菜单标题
        val info = menuInfo as? AdapterView.AdapterContextMenuInfo
        contextPosition = info?.position ?: -1
        if (contextPosition in books.indices) {
            menu.setHeaderTitle(books[contextPosition])
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val book = if (contextPosition in books.indices) books[contextPosition] else ""
        Toast.makeText(this, "$book → ${item.title}", Toast.LENGTH_SHORT).show()
        return true
    }
}
