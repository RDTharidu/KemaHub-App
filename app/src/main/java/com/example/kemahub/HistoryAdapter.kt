package com.example.kemahub

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class HistoryAdapter(
    private val historyList: List<FoodItem>,
    private val callbacks: SelectionCallbacks? = null
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val selectedItemsIds = mutableSetOf<String>()
    private var isSelectionMode = false

    interface SelectionCallbacks {
        fun onSelectionModeChanged(isInSelectionMode: Boolean)
        fun onSelectionCountChanged(count: Int)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemFoodName)
        val tvDesc: TextView = itemView.findViewById(R.id.tvItemFoodDesc)
        val tvQty: TextView = itemView.findViewById(R.id.tvItemFoodQty)
        val btnRequest: Button = itemView.findViewById(R.id.btnRequestFood)

        val ivDelete: ImageView = itemView.findViewById(R.id.ivDeleteIcon)
        val ivSelected: ImageView = itemView.findViewById(R.id.ivSelectedIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val food = historyList[position]

        holder.tvName.text = food.name
        holder.tvDesc.text = food.description
        holder.tvQty.text = "Quantity: ${food.quantity}"

        holder.btnRequest.visibility = View.GONE

        updateSelectionUI(holder, food)

        holder.ivDelete.setOnClickListener {
            if (!isSelectionMode) {
                showSingleDeleteConfirmationDialog(holder, food)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (callbacks != null && !isSelectionMode) {
                isSelectionMode = true
                callbacks.onSelectionModeChanged(true)
                toggleSelection(food.id)
                return@setOnLongClickListener true
            }
            false
        }

        holder.itemView.setOnClickListener {
            if (isSelectionMode && callbacks != null) {
                toggleSelection(food.id)
            }
        }
    }

    private fun updateSelectionUI(holder: HistoryViewHolder, food: FoodItem) {
        if (isSelectionMode) {
            holder.ivDelete.visibility = View.GONE
            holder.ivSelected.visibility = View.VISIBLE

            if (selectedItemsIds.contains(food.id)) {
                holder.ivSelected.setImageResource(android.R.drawable.checkbox_on_background)
                holder.itemView.setBackgroundColor(Color.parseColor("#E0F7FA"))
            } else {
                holder.ivSelected.setImageResource(android.R.drawable.checkbox_off_background)
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            }
        } else {
            holder.ivDelete.visibility = View.VISIBLE
            holder.ivSelected.visibility = View.GONE
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun toggleSelection(foodId: String) {
        if (selectedItemsIds.contains(foodId)) {
            selectedItemsIds.remove(foodId)
        } else {
            selectedItemsIds.add(foodId)
        }

        callbacks?.onSelectionCountChanged(selectedItemsIds.size)

        if (selectedItemsIds.isEmpty()) {
            isSelectionMode = false
            callbacks?.onSelectionModeChanged(false)
        }
        notifyDataSetChanged()
    }

    fun getSelectedCount(): Int = selectedItemsIds.size

    fun clearSelection() {
        selectedItemsIds.clear()
        isSelectionMode = false
        callbacks?.onSelectionModeChanged(false)
        callbacks?.onSelectionCountChanged(0)
        notifyDataSetChanged()
    }


    fun deleteSelectedItems() {
        if (selectedItemsIds.isEmpty()) return
        val dbRef = FirebaseDatabase.getInstance().getReference("Donations")

        selectedItemsIds.forEach { id ->
            dbRef.child(id).removeValue()
        }

        clearSelection()
    }

    private fun showSingleDeleteConfirmationDialog(holder: HistoryViewHolder, food: FoodItem) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Delete Donation")
            .setMessage("Are you sure you want to delete '${food.name}'?")
            .setPositiveButton("Yes, Delete") { _, _ ->
                val dbRef = FirebaseDatabase.getInstance().getReference("Donations").child(food.id)
                dbRef.removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Donation Deleted!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Failed to delete.", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun getItemCount(): Int = historyList.size
}