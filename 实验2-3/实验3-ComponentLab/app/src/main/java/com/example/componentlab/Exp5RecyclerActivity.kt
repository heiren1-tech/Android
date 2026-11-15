package com.example.componentlab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 实验3-5：使用 RecyclerView 实现列表视图效果
 *
 * - 列表项包含图片、名称
 * - 使用 RecyclerView、Adapter 与 ViewHolder 展示数据
 * - 单击显示名称，并增加一条记录，列表立即更新
 * - 使用 setHasStableIds + bindingAdapterPosition，保证滚动正常、点击对象正确、更新后无错位
 */
class Exp5RecyclerActivity : AppCompatActivity() {

    private val items = AnimalRepository.animals.toMutableList()
    private var nextId = 100L

    private lateinit var adapter: AnimalAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exp5_recycler)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AnimalAdapter(items) { position ->
            val animal = items[position]

            // 单击显示名称
            Toast.makeText(this, "显示名称：${animal.name}", Toast.LENGTH_SHORT).show()

            // 增加一条记录，并立即更新列表（插入到被点击项之后）
            val insertAt = position + 1
            items.add(insertAt, animal.copy(id = nextId++))
            adapter.notifyItemInserted(insertAt)
            recyclerView.scrollToPosition(insertAt)
        }
        recyclerView.adapter = adapter
    }

    /** RecyclerView.Adapter + ViewHolder */
    private class AnimalAdapter(
        private val data: MutableList<Animal>,
        private val onItemClick: (position: Int) -> Unit
    ) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

        init {
            // 稳定的 id 能保证数据集更新后列表不错位
            setHasStableIds(true)
        }

        class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image: ImageView = view.findViewById(R.id.iv_animal)
            val name: TextView = view.findViewById(R.id.tv_name)
            val count: TextView = view.findViewById(R.id.tv_count)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_animal_card, parent, false)
            return AnimalViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        override fun getItemId(position: Int): Long = data[position].id

        override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
            val animal = data[position]
            holder.image.setImageResource(animal.imageRes)
            holder.name.text = animal.name
            holder.count.text = "第 ${position + 1} 项"

            holder.itemView.setOnClickListener {
                // 用 bindingAdapterPosition 而不是记住的 position，避免更新后点击错位
                val pos = holder.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick(pos)
                }
            }
        }
    }
}
