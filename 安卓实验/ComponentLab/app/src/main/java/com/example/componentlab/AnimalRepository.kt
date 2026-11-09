package com.example.componentlab

import androidx.annotation.DrawableRes

/** 列表数据项：动物名称 + 图片资源 */
data class Animal(val id: Long, val name: String, @DrawableRes val imageRes: Int)

/**
 * 动物数据源。
 * 说明：实验要求使用 QQ 群附件中的动物图片，此处使用同等效果的图片资源代替，
 * 如需替换，把图片放进 res/drawable 并修改下面的 imageRes 即可。
 */
object AnimalRepository {

    val animals: List<Animal> = listOf(
        Animal(1L, "Lion", R.drawable.animal_lion),
        Animal(2L, "Tiger", R.drawable.animal_tiger),
        Animal(3L, "Monkey", R.drawable.animal_monkey),
        Animal(4L, "Dog", R.drawable.animal_dog),
        Animal(5L, "Cat", R.drawable.animal_cat),
        Animal(6L, "Elephant", R.drawable.animal_elephant)
    )
}
