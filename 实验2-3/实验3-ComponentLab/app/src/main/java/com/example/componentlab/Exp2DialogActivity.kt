package com.example.componentlab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * 实验3-2：创建自定义布局的 AlertDialog
 *
 * 创建一个自定义布局（dialog_custom_login.xml），
 * 并调用 AlertDialog.Builder 的 setView() 把该布局添加到 AlertDialog 中。
 */
class Exp2DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp2_dialog)

        findViewById<Button>(R.id.btn_open_dialog).setOnClickListener {
            showCustomDialog()
        }
    }

    private fun showCustomDialog() {
        // 加载自定义布局
        val view = layoutInflater.inflate(R.layout.dialog_custom_login, null)

        // 用 setView() 把自定义布局添加到 AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        // 自定义布局内的两个按钮
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.btn_sign_in).setOnClickListener {
            val username = view.findViewById<EditText>(R.id.et_username).text.toString().trim()
            Toast.makeText(
                this,
                getString(R.string.sign_in_success) + if (username.isEmpty()) "" else "欢迎，$username",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}
