package com.example.peoplerooms.ui.room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.peoplerooms.R
import com.example.peoplerooms.data.model.people.PeopleItemModel
import com.example.peoplerooms.data.model.rooms.Rooms
import com.example.peoplerooms.data.model.rooms.RoomsItemModel
import com.example.peoplerooms.databinding.ListItemBinding
import com.example.peoplerooms.databinding.RoomListItemBinding
import com.example.peoplerooms.ui.people.PeopleAdapter

class RoomAdapter(
    val list: Rooms,
    val onClickAction: (RoomsItemModel) -> Unit
) : RecyclerView.Adapter<RoomAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoomAdapter.MyViewHolder, position: Int) {
        holder.initUI(list[position])
        holder.binding.tvId.setOnClickListener {
            onClickAction.invoke(
                list[position] ?: RoomsItemModel()
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val binding = RoomListItemBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun initUI(model: RoomsItemModel) {
            binding.apply {
                tvId.text = model.id
                tvIsOccupied.text = model.isOccupied.toString()
                tvMaxOccupancy.text = model.maxOccupancy.toString()
            }
        }
    }
}