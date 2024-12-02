package com.example.adopse_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Lecturer(val name:String, val position: String, val imageResource: Int)


class LecturerAdapter(private val lecturers: List<Lecturer>) :
    RecyclerView.Adapter<LecturerAdapter.LecturerViewHolder>() {

    class LecturerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.lecturer_image)
        val nameView: TextView = view.findViewById(R.id.lecturer_name)
        val positionView: TextView = view.findViewById(R.id.lecturer_position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lecturer_item, parent, false)
        return LecturerViewHolder(view)
    }

    override fun onBindViewHolder(holder: LecturerViewHolder, position: Int) {
        val lecturer = lecturers[position]
        holder.imageView.setImageResource(lecturer.imageResource)
        holder.nameView.text = lecturer.name
        holder.positionView.text = lecturer.position
    }

    override fun getItemCount() = lecturers.size
}