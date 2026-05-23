package com.example.kemahub

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BeneClaimedAdapter(private val donationList: List<DonationModel>) : RecyclerView.Adapter<BeneClaimedAdapter.ClaimedViewHolder>() {

    class ClaimedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoodImg: ImageView = itemView.findViewById(R.id.ivClaimedFoodImg)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvClaimedFoodName)
        val tvFoodDesc: TextView = itemView.findViewById(R.id.tvClaimedFoodDesc)
        val tvStatus: TextView = itemView.findViewById(R.id.tvClaimedStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bene_claimed_food, parent, false)
        return ClaimedViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClaimedViewHolder, position: Int) {
        val donation = donationList[position]

        holder.tvFoodName.text = donation.foodName

        // විස්තර ටික ලස්සනට පේළි වලට කඩලා පෙන්නනවා
        holder.tvFoodDesc.text = "${donation.quantity} Portions\nPickup Location - ${donation.pickupLocation}\nContact - ${donation.contact}"

        // Status එක පෙන්නනවා
        holder.tvStatus.text = "Status - ${donation.status}"

        // Base64 ෆොටෝ එක Decode කරනවා
        if (!donation.imageBase64.isNullOrEmpty()) {
            try {
                val decodedString = Base64.decode(donation.imageBase64, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                holder.ivFoodImg.setImageBitmap(decodedByte)
            } catch (e: Exception) {
                holder.ivFoodImg.setImageResource(R.drawable.logo)
            }
        } else {
            holder.ivFoodImg.setImageResource(R.drawable.logo)
        }
    }

    override fun getItemCount(): Int = donationList.size
}