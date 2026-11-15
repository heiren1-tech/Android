package com.example.componentlab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：Notification 通知
 *
 * - 使用 NotificationManager 服务发送通知
 * - Android 8.0 起使用通知渠道（NotificationChannel）
 * - Android 13 起需要 POST_NOTIFICATIONS 运行时权限
 */
class DemoNotificationActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_NOTIFICATION = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_notification)

        val etTitle = findViewById<EditText>(R.id.et_notify_title)
        val etText = findViewById<EditText>(R.id.et_notify_text)

        // 预填默认内容，方便直接点击发送
        etTitle.setText(getString(R.string.notification_title))
        etText.setText(getString(R.string.notification_text))

        NotificationHelper.requestPermission(this, REQUEST_NOTIFICATION)

        findViewById<Button>(R.id.btn_send).setOnClickListener {
            NotificationHelper.send(
                context = this,
                title = etTitle.text.toString().ifBlank { getString(R.string.notification_title) },
                content = etText.text.toString().ifBlank { getString(R.string.notification_text) },
                notifyId = 999
            )
        }
    }
}
