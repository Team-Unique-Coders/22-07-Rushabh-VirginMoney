package com.project.peoplerooms.ui.people

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.peoplerooms.R
import com.project.peoplerooms.data.model.people.People
import com.project.peoplerooms.data.model.people.PeopleItemModel
import com.project.peoplerooms.databinding.ListItemBinding
import android.widget.ImageView

class PeopleAdapter(val list: People, val onClickAction: (PeopleItemModel) -> Unit) : RecyclerView.Adapter<PeopleAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PeopleAdapter.MyViewHolder, position: Int) {
        holder.initUI(list[position])
        holder.binding.ivImage.setOnClickListener {
            onClickAction.invoke(
                list[position] ?: PeopleItemModel()
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun initUI(model: PeopleItemModel) {
            binding.apply {
                tvName.text = model.firstName + " " + model.lastName
                tvContact.text = model.email
                //load image from URL - GLIDE
                Glide.with(view.context) //reference of parent
                    .load(model.avatar) //the resource that needs to be loaded
                    //what if URL fails?
                    .placeholder(R.drawable.img_1)
                    .into(view.findViewById<ImageView>(R.id.ivImage)) //imageView that will display image
            }
        }
    }
}