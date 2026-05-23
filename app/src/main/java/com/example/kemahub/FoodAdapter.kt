package com.example.kemahub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class FoodAdapter(private val foodList: List<FoodItem>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemFoodName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvItemFoodDesc)
        val tvQty: TextView = itemView.findViewById(R.id.tvItemFoodQty)
        val btnRequest: Button = itemView.findViewById(R.id.btnRequestFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]

        holder.tvName.text = food.name
        holder.tvDesc.text = food.description
        holder.tvQty.text = "Quantity: ${food.quantity}"

        // Request බොත්තම එබුවම වෙන දේ
        holder.btnRequest.setOnClickListener {
            // අදාළ ID එකෙන් Database එකට යනවා
            val dbRef = FirebaseDatabase.getInstance().getReference("Donations").child(food.id)

            // Status එක Requested කියලා වෙනස් කරනවා
            dbRef.child("status").setValue("Requested")
                .addOnSuccessListener {
                    Toast.makeText(holder.itemView.context, "${food.name} Requested successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(holder.itemView.context, "Failed to request. Try again.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}