package com.example.kemahub

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BeneFoodAdapter(private val donationList: List<DonationModel>) : RecyclerView.Adapter<BeneFoodAdapter.BeneFoodViewHolder>() {

    class BeneFoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoodImg: ImageView = itemView.findViewById(R.id.ivBeneFoodImg)
        val tvFoodName: TextView = itemView.findViewById(R.id.tvBeneFoodName)
        val tvViewDetails: TextView = itemView.findViewById(R.id.tvBeneViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneFoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bene_food, parent, false)
        return BeneFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeneFoodViewHolder, position: Int) {
        val donation = donationList[position]

        holder.tvFoodName.text = donation.foodName

        // Base64 අකුරු ටික ආයේ ෆොටෝ එකක් කරනවා
        if (!donation.imageBase64.isNullOrEmpty()) {
            try {
                val decodedString = Base64.decode(donation.imageBase64, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                holder.ivFoodImg.setImageBitmap(decodedByte)
            } catch (e: Exception) {
                holder.ivFoodImg.setImageResource(R.drawable.logo) // අවුලක් ගියොත් logo එක පෙන්නනවා
            }
        } else {
            holder.ivFoodImg.setImageResource(R.drawable.logo) // ෆොටෝ එකක් නැත්නම් logo එක පෙන්නනවා
        }

        // View Details එබුවම මේ අලුත් පිටුවට යනවා (දත්ත ටිකත් අරගෙනම)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, BeneDonationDetailsActivity::class.java)
            intent.putExtra("id", donation.id)
            intent.putExtra("name", donation.foodName)
            intent.putExtra("qty", donation.quantity)
            intent.putExtra("location", donation.pickupLocation)
            intent.putExtra("contact", donation.contact)
            intent.putExtra("image", donation.imageBase64)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = donationList.size
}