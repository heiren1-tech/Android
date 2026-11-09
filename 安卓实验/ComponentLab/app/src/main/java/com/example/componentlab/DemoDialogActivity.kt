package com.example.componentlab

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：构建提醒对话框
 *
 * AlertDialog 有三个区域：标题、内容区域、操作按钮，使用 AlertDialog.Builder 创建。
 */
class DemoDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_dialog)

        findViewById<Button>(R.id.btn_show_dialog).setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_android)
                .setTitle("提醒")                      // 1. 标题
                .setMessage(R.string.dialog_msg)        // 2. 内容区域
                .setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }      // 3. 操作按钮
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}
