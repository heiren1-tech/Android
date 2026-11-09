package com.example.componentlab

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * 通知工具类：负责创建通知渠道、检查/申请权限并发送通知。
 * Android 8.0(API 26) 起通知必须归属到渠道；Android 13(API 33) 起发送通知需要运行时权限。
 */
object NotificationHelper {

    const val CHANNEL_ID = "exp3_channel"
    private const val CHANNEL_NAME = "实验3通知"

    /** 创建通知渠道（Android 8.0 及以上） */
    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "实验3 Android 界面组件通知"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    /** 是否已获得通知权限（Android 13 以下默认有权限） */
    fun hasPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** 申请通知权限（Android 13 及以上） */
    fun requestPermission(context: android.app.Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission(context)) {
            androidx.core.app.ActivityCompat.requestPermissions(
                context, arrayOf(Manifest.permission.POST_NOTIFICATIONS), requestCode
            )
        }
    }

    /**
     * 发送一条通知。
     * 小图标使用应用程序图标（@drawable/ic_launcher），标题与内容由调用方传入。
     */
    @SuppressLint("MissingPermission")
    fun send(context: Context, title: String, content: String, notifyId: Int = 1) {
        ensureChannel(context)
        if (!hasPermission(context)) {
            Toast.makeText(context, "未授予通知权限，无法发送通知", Toast.LENGTH_SHORT).show()
            return
        }
        // 点击通知后跳回主页
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)   // 通知图标 = 应用程序图标
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notifyId, notification)
        } catch (e: SecurityException) {
            Toast.makeText(context, "发送通知失败：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
