package com.example.componentlab

import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 课件示例：利用单选钮、复选框获取用户信息
 *
 * - 用 RadioGroup 组织 RadioButton 获取性别
 * - 用 CheckBox 获取喜欢的颜色
 * - 实时把用户信息显示在 TextView 上
 */
class DemoCheckBoxActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var rgGender: RadioGroup
    private lateinit var cbRed: CheckBox
    private lateinit var cbBlue: CheckBox
    private lateinit var cbGreen: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_checkbox)

        tvResult = findViewById(R.id.tv_result)
        rgGender = findViewById(R.id.rg_gender)
        cbRed = findViewById(R.id.cb_red)
        cbBlue = findViewById(R.id.cb_blue)
        cbGreen = findViewById(R.id.cb_green)

        // 单选钮组：状态变化时更新结果
        rgGender.setOnCheckedChangeListener { _, _ -> updateResult() }

        // 复选框：状态变化时更新结果
        listOf(cbRed, cbBlue, cbGreen).forEach { cb ->
            cb.setOnCheckedChangeListener { _, _ -> updateResult() }
        }

        updateResult()
    }

    private fun updateResult() {
        val gender = if (rgGender.checkedRadioButtonId == R.id.rb_male) {
            getString(R.string.male)
        } else {
            getString(R.string.female)
        }

        val colors = mutableListOf<String>()
        if (cbRed.isChecked) colors.add(getString(R.string.color_red_option))
        if (cbBlue.isChecked) colors.add(getString(R.string.color_blue_option))
        if (cbGreen.isChecked) colors.add(getString(R.string.color_green_option))

        val base = getString(R.string.gender_result, gender)
        tvResult.text = if (colors.isEmpty()) {
            base
        } else {
            "$base；喜欢的颜色：${colors.joinToString("、")}"
        }
    }
}
