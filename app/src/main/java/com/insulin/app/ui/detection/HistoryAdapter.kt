package com.insulin.app.ui.detection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.insulin.app.databinding.ItemHistoryListBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Kosong
    }

    override fun getItemCount() = 0 // Tidak ada data

    inner class ViewHolder(binding: ItemHistoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Kosong
    }
}
