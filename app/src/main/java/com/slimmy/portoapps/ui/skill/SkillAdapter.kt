package com.slimmy.portoapps.ui.skill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.slimmy.portoapps.R
import com.slimmy.portoapps.data.Skill
import com.slimmy.portoapps.databinding.ItemSkillGroupBinding

class SkillAdapter : ListAdapter<SkillGroup, SkillAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSkillGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemSkillGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: SkillGroup) {
            binding.tvCategoryName.text = item.category
            
            val context = binding.root.context
            val currentChipCount = binding.chipGroupSkills.childCount
            val targetChipCount = item.skills.size

            // Optimasi: Gunakan kembali view yang sudah ada (View Recycling manual dalam ChipGroup)
            // Hapus view berlebih jika ada
            if (currentChipCount > targetChipCount) {
                binding.chipGroupSkills.removeViews(targetChipCount, currentChipCount - targetChipCount)
            }

            item.skills.forEachIndexed { index, skill ->
                val chip = if (index < currentChipCount) {
                    // Pakai chip yang sudah ada
                    binding.chipGroupSkills.getChildAt(index) as Chip
                } else {
                    // Buat baru hanya jika kurang
                    val newChip = Chip(context).apply {
                        isClickable = false
                        isCheckable = false
                        setChipBackgroundColorResource(android.R.color.transparent)
                        chipStrokeWidth = 1f * context.resources.displayMetrics.density
                        setChipStrokeColorResource(android.R.color.darker_gray)
                        textSize = 12f // Sedikit lebih kecil agar lebih ringan
                    }
                    binding.chipGroupSkills.addView(newChip)
                    newChip
                }
                
                // Update hanya teksnya saja (murah)
                chip.text = skill.nama
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SkillGroup>() {
        override fun areItemsTheSame(oldItem: SkillGroup, newItem: SkillGroup): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: SkillGroup, newItem: SkillGroup): Boolean {
            return oldItem == newItem
        }
    }
}

data class SkillGroup(
    val category: String,
    val skills: List<Skill>
)
