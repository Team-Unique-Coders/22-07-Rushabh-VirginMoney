package com.project.peoplerooms.ui.room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.peoplerooms.R
import com.project.peoplerooms.data.model.rooms.RoomsItemModel
import com.project.peoplerooms.databinding.RoomListItemBinding

class RoomAdapter(
    var list: MutableList<RoomsItemModel>,
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
        val room = list[position]
        holder.initUI(room)
        holder.binding.tvId.setOnClickListener {
            onClickAction.invoke(
                list[position]
            )
        }
        val context = holder.itemView.context
        val color = if (room.isOccupied == true) {
            // occupied
            ContextCompat.getColor(context, R.color.occupied_color)
        } else {
            // available
            ContextCompat.getColor(context, R.color.available_color)
        }
        holder.binding.cardViewRoom.setCardBackgroundColor(color)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val binding = RoomListItemBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun initUI(model: RoomsItemModel) {
            binding.apply {
                tvId.text = "Room No : "+ model.id
                tvMaxOccupancy.text = "Max Space : " +  model.maxOccupancy.toString()
            }
        }
    }
}