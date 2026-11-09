package com.example.componentlab

import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 实验3-1：Android ListView 的用法
 *
 * 要求：
 * (1) 注意列表项的布局（左边名称、右边图片）
 * (2) 图片使用图片资源（此处用 drawable 中的动物图片代替 QQ 群附件资源）
 * (3) 使用 Toast 显示选中的列表项信息
 * (4) 单击列表项后发送一条通知，通知图标为应用程序图标，标题为列表项内容
 */
class Exp1ListActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_NOTIFICATION = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp1_list)

        // Android 13+ 需要先申请通知权限
        NotificationHelper.requestPermission(this, REQUEST_NOTIFICATION)
        NotificationHelper.ensureChannel(this)

        // SimpleAdapter 的 5 个参数：context、data、列表项布局、from(Key 数组)、to(组件 id 数组)
        val data: List<Map<String, Any>> = AnimalRepository.animals.map {
            mapOf("name" to it.name, "image" to it.imageRes)
        }
        val adapter = SimpleAdapter(
            this,
            data,
            R.layout.list_item_animal,
            arrayOf("name", "image"),
            intArrayOf(R.id.tv_name, R.id.iv_animal)
        )

        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = adapter

        // (3)(4) 单击：Toast 显示选中信息 + 发送通知
        listView.setOnItemClickListener { _, _, position, _ ->
            val name = AnimalRepository.animals[position].name
            Toast.makeText(
                this,
                getString(R.string.selected_prefix) + name,
                Toast.LENGTH_SHORT
            ).show()

            NotificationHelper.send(
                context = this,
                title = name,                                   // 通知标题 = 列表项内容
                content = getString(R.string.notify_content_default),
                notifyId = position + 1
            )
        }
    }
}
