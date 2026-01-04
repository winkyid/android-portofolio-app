package com.slimmy.portoapps.ui.portofolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slimmy.portoapps.MainActivity
import com.slimmy.portoapps.data.Portofolio
import com.slimmy.portoapps.databinding.ItemPortofolioBinding

class PortofolioAdapter : ListAdapter<Portofolio, PortofolioAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPortofolioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPortofolioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: Portofolio) {
            binding.tvJudul.text = item.judul
            binding.tvDeskripsi.text = item.deskripsi
            binding.tvTeknologi.text = item.teknologi
            
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("portofolioId", item.id)
                }
                // Menggunakan fungsi navigasi kustom di MainActivity untuk menghindari crash ViewPager2
                val activity = it.context as? MainActivity
                activity?.navigateToDetail(bundle)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Portofolio>() {
        override fun areItemsTheSame(oldItem: Portofolio, newItem: Portofolio): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Portofolio, newItem: Portofolio): Boolean {
            return oldItem == newItem
        }
    }
}
