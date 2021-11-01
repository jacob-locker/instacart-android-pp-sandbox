package com.instacart.android.challenges

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val items = ArrayList<ItemRow>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFoodName: TextView = itemView.findViewById(R.id.tvFoodName)

        fun bind(row: ItemRow) {
            tvFoodName.text = "${row.name} (${row.count})"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(newItems: List<ItemRow>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
