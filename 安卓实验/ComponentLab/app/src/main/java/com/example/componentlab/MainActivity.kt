package com.example.componentlab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/** 入口菜单：实验3 的 5 个任务 + 4-AndroidUI 课件的示例 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ===== 实验3 =====
        bind(R.id.btn_exp1, Exp1ListActivity::class.java)
        bind(R.id.btn_exp2, Exp2DialogActivity::class.java)
        bind(R.id.btn_exp3, Exp3MenuActivity::class.java)
        bind(R.id.btn_exp4, Exp4ActionModeActivity::class.java)
        bind(R.id.btn_exp5, Exp5RecyclerActivity::class.java)

        // ===== 4-AndroidUI 课件示例 =====
        bind(R.id.btn_demo_button, DemoButtonActivity::class.java)
        bind(R.id.btn_demo_check, DemoCheckBoxActivity::class.java)
        bind(R.id.btn_demo_notification, DemoNotificationActivity::class.java)
        bind(R.id.btn_demo_dialog, DemoDialogActivity::class.java)
        bind(R.id.btn_demo_optionmenu, DemoOptionMenuActivity::class.java)
        bind(R.id.btn_demo_contextmenu, DemoContextMenuActivity::class.java)
        bind(R.id.btn_demo_popupmenu, DemoPopupMenuActivity::class.java)
    }

    private fun bind(buttonId: Int, target: Class<*>) {
        findViewById<Button>(buttonId).setOnClickListener {
            startActivity(Intent(this, target))
        }
    }
}
