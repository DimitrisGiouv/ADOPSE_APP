package com.example.adopse_app
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val items: List<Item>): RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.course_content_item, parent, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val continueButton: LinearLayout = itemView.findViewById(R.id.continue_button)
        private val minutesLayout : LinearLayout = itemView.findViewById(R.id.minutes_layout)
        private val minutesTextView: TextView = itemView.findViewById(R.id.minutes)
        private val moduleTitleTextView: TextView = itemView.findViewById(R.id.module_title)
        private val greyBar: LinearLayout = itemView.findViewById(R.id.grey_bar)

        fun bind(item:Item){
            minutesTextView.text = item.minutes
            moduleTitleTextView.text = item.moduleTitle
        }

    }

}