package com.slimmy.portoapps.ui.pendidikan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slimmy.portoapps.data.Pendidikan
import com.slimmy.portoapps.databinding.ItemPendidikanBinding

class PendidikanAdapter : ListAdapter<Pendidikan, PendidikanAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPendidikanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPendidikanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: Pendidikan) {
            binding.tvLevel.text = item.levelName
            binding.tvInstitusi.text = "• ${item.institusi}"
            binding.tvTahun.text = "• ${item.tahunMulai} - ${item.tahunSelesai}"
            
            if (item.jurusan.isNotEmpty()) {
                binding.tvDetail.visibility = View.VISIBLE
                binding.tvDetail.text = "• ${item.jurusan}"
            } else {
                binding.tvDetail.visibility = View.GONE
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pendidikan>() {
        override fun areItemsTheSame(oldItem: Pendidikan, newItem: Pendidikan): Boolean {
            return oldItem.institusi == newItem.institusi && oldItem.levelName == newItem.levelName
        }

        override fun areContentsTheSame(oldItem: Pendidikan, newItem: Pendidikan): Boolean {
            return oldItem == newItem
        }
    }
}
