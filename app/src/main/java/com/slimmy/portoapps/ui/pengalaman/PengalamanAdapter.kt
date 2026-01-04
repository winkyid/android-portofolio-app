package com.slimmy.portoapps.ui.pengalaman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slimmy.portoapps.data.Pengalaman
import com.slimmy.portoapps.databinding.ItemPengalamanBinding

class PengalamanAdapter : ListAdapter<Pengalaman, PengalamanAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPengalamanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPengalamanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: Pengalaman) {
            binding.tvPerusahaan.text = item.perusahaan
            binding.tvPosisi.text = item.posisi
            binding.tvTahun.text = "${item.tahunMulai} - ${item.tahunSelesai}"
            binding.tvDeskripsi.text = item.deskripsi
            
            // Handle Task List di UI
            binding.containerTasks.removeAllViews()
            if (item.tasks.isNotEmpty()) {
                binding.containerTasks.visibility = View.VISIBLE
                item.tasks.forEach { task ->
                    val taskView = TextView(binding.root.context).apply {
                        text = "• $task"
                        textSize = 12f
                        setPadding(0, 4, 0, 4)
                    }
                    binding.containerTasks.addView(taskView)
                }
            } else {
                binding.containerTasks.visibility = View.GONE
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pengalaman>() {
        override fun areItemsTheSame(oldItem: Pengalaman, newItem: Pengalaman): Boolean {
            return oldItem.perusahaan == newItem.perusahaan && oldItem.posisi == newItem.posisi
        }

        override fun areContentsTheSame(oldItem: Pengalaman, newItem: Pengalaman): Boolean {
            return oldItem == newItem
        }
    }
}