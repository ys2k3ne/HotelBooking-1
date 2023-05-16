package com.example.hotelbooking.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelbooking.DetailActivity
import com.example.hotelbooking.EditHotelActivity
import com.example.hotelbooking.R
import com.example.hotelbooking.models.DataClass

class HotelAdapter(private val context: Context, private var dataList: List<DataClass>, private val isAdmin: Boolean) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].hotelImage)
            .into(holder.recImage)
        holder.recTitle.text = dataList[position].hotelName
        holder.recDesc.text = dataList[position].hotelAddress
        holder.recPriority.text = dataList[position].hotelPrice

        holder.recCard.setOnClickListener {
            val intent = if (isAdmin) {
                Intent(context, EditHotelActivity::class.java)
            } else {
                Intent(context, DetailActivity::class.java)
            }

            intent.putExtra("HotelID", dataList[holder.adapterPosition].hotelID)
            intent.putExtra("HotelId", dataList[holder.adapterPosition].hotelID)
            intent.putExtra("Image", dataList[holder.adapterPosition].hotelImage)
            intent.putExtra("Address", dataList[holder.adapterPosition].hotelAddress)
            intent.putExtra("Hotel Name", dataList[holder.adapterPosition].hotelName)
            intent.putExtra("Hotel Price", dataList[holder.adapterPosition].hotelPrice)
            intent.putExtra("Hotel Rating", dataList[holder.adapterPosition].hotelRating)
            intent.putExtra("description", dataList[holder.adapterPosition].description)
            intent.putExtra("available", dataList[holder.adapterPosition].available)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun searchDataList(searchList: List<DataClass>) {
        val oldSize = dataList.size
        dataList = searchList
        val newSize = dataList.size
        if (oldSize == newSize) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(0, newSize)
        }
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recTitle: TextView = itemView.findViewById(R.id.recTitle)
    val recDesc: TextView = itemView.findViewById(R.id.recDesc)
    val recPriority: TextView = itemView.findViewById(R.id.recPriority)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
}
