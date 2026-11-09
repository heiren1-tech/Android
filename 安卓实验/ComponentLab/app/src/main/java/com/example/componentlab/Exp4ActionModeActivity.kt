package com.example.componentlab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat

/**
 * 实验3-4：创建上下文操作模式(ActionMode)的上下文菜单
 *
 * - 使用 ListView 创建 List
 * - 为 List Item 创建 ActionMode 形式的上下文菜单
 * - 实现 ActionMode.Callback 接口，长按列表项时调用 startSupportActionMode() 显示操作栏
 */
class Exp4ActionModeActivity : AppCompatActivity(), ActionMode.Callback {

    private val names = listOf("One", "Two", "Three", "Four", "Five")
    private val selected = mutableSetOf<Int>()

    private var actionMode: ActionMode? = null
    private lateinit var adapter: DroidAdapter
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp4_actionmode)

        adapter = DroidAdapter()
        listView = findViewById(R.id.list_view)
        listView.adapter = adapter

        // 长按列表项 → 启动 ActionMode 形式的上下文菜单
        listView.setOnItemLongClickListener { _, _, position, _ ->
            if (actionMode == null) {
                actionMode = startSupportActionMode(this)
            }
            toggleSelection(position)
            true
        }

        // 处于 ActionMode 时，单击用于多选；否则弹出 Toast
        listView.setOnItemClickListener { _, _, position, _ ->
            if (actionMode != null) {
                toggleSelection(position)
            } else {
                Toast.makeText(this, names[position], Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** 选中/取消选中，并同步 ActionMode 标题与列表高亮 */
    private fun toggleSelection(position: Int) {
        if (!selected.add(position)) {
            selected.remove(position)
        }
        actionMode?.title = getString(R.string.selected_count, selected.size)
        adapter.notifyDataSetChanged()
    }

    // ================= ActionMode.Callback =================

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_actionmode, menu)
        mode.title = getString(R.string.selected_count, selected.size)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            Toast.makeText(this, R.string.deleted_toast, Toast.LENGTH_SHORT).show()
            mode.finish()   // 结束 ActionMode，会回调 onDestroyActionMode
            return true
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        actionMode = null
        selected.clear()
        adapter.notifyDataSetChanged()
    }

    /** 自定义 Adapter：根据选中状态改变行背景 */
    private inner class DroidAdapter : BaseAdapter() {

        private val inflater = LayoutInflater.from(this@Exp4ActionModeActivity)

        override fun getCount(): Int = names.size
        override fun getItem(position: Int): Any = names[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: inflater.inflate(R.layout.list_item_droid, parent, false)
            view.findViewById<TextView>(R.id.tv_name).text = names[position]
            view.setBackgroundColor(
                if (selected.contains(position)) {
                    ContextCompat.getColor(this@Exp4ActionModeActivity, R.color.action_selected)
                } else {
                    ContextCompat.getColor(this@Exp4ActionModeActivity, android.R.color.transparent)
                }
            )
            return view
        }
    }
}
