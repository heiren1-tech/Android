package com.example.layoutlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 实验5：Android Compose —— 课程学习任务列表（添加 / 勾选 / 删除，完成数与删除线同步） */

private val TaskRed = Color(0xFFC62828)
private val CardGray = Color(0xFFF3F3F3)
private val TextDark = Color(0xFF333333)
private val TextGray = Color(0xFF888888)

/** 一条学习任务：任务名 + 是否完成 */
data class Task(val name: String, val done: Boolean)

class TaskListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = TaskRed,
                    onPrimary = Color.White
                )
            ) {
                TaskListScreen()
            }
        }
    }
}

@Composable
fun TaskListScreen() {
    // 输入框内容
    var input by remember { mutableStateOf("") }
    // 任务列表（初始状态：3 项任务，完成 1 项）
    val tasks = remember {
        mutableStateListOf(
            Task("学习 Column 和 Row", done = true),
            Task("学习状态管理", done = false),
            Task("完成 Compose 实验", done = false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "课程学习任务",
            color = TaskRed,
            fontSize = 26.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
        Spacer(Modifier.height(14.dp))

        // 输入新任务 + 添加按钮
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("请输入学习任务", color = TextGray, fontSize = 14.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = TextDark, fontSize = 14.sp)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        tasks.add(Task(input.trim(), done = false))
                        input = ""
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TaskRed)
            ) {
                Text("添加", fontSize = 15.sp)
            }
        }
        Spacer(Modifier.height(10.dp))

        // 完成计数
        Text(
            text = "已完成: ${tasks.count { it.done }} / ${tasks.size}",
            color = TextGray,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(10.dp))

        // 任务列表
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(tasks) { index, task ->
                TaskItem(
                    task = task,
                    onToggle = {
                        tasks[index] = task.copy(done = !task.done)
                    },
                    onDelete = {
                        tasks.removeAt(index)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardGray, RoundedCornerShape(10.dp))
            .padding(start = 4.dp, end = 4.dp, top = 2.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(checkedColor = TaskRed)
        )
        // 任务名：完成后加删除线并变灰
        Text(
            text = task.name,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = if (task.done) TextGray else TextDark,
            textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
        )
        TextButton(onClick = onDelete) {
            Text("删除", color = TaskRed, fontSize = 14.sp)
        }
    }
}
