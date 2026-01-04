package com.slimmy.portoapps.ui.filemanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.slimmy.portoapps.R
import com.slimmy.portoapps.databinding.ItemFileManagerBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileAdapter(
    private val onDelete: (File) -> Unit,
    private val onOpenBuiltIn: (File) -> Unit,
    private val onOpenFolder: () -> Unit,
    private val onShare: (File) -> Unit
) : ListAdapter<File, FileAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFileManagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFileManagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            binding.tvFileName.text = file.name
            
            val date = Date(file.lastModified())
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val size = file.length() / 1024 // KB
            binding.tvFileInfo.text = "${sdf.format(date)} • ${size} KB"

            binding.cardFile.setOnClickListener { onOpenBuiltIn(file) }
            
            binding.btnMore.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                
                // Menambahkan menu dengan icon (Support Helper)
                popup.menu.add(0, 1, 0, "Bagikan CV").apply {
                    setIcon(R.drawable.ic_share)
                }
                popup.menu.add(0, 2, 1, "Buka Folder").apply {
                    setIcon(R.drawable.ic_folder)
                }
                popup.menu.add(0, 3, 2, "Hapus File").apply {
                    setIcon(R.drawable.ic_delete)
                }

                // Force showing icons
                try {
                    val fields = popup.javaClass.getDeclaredFields()
                    for (field in fields) {
                        if ("mPopup" == field.name) {
                            field.isAccessible = true
                            val menuPopupHelper = field.get(popup)
                            val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                            val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                            setForceIcons.invoke(menuPopupHelper, true)
                            break
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        1 -> onShare(file)
                        2 -> onOpenFolder()
                        3 -> onDelete(file)
                    }
                    true
                }
                popup.show()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem.absolutePath == newItem.absolutePath
        override fun areContentsTheSame(oldItem: File, newItem: File) = oldItem.lastModified() == newItem.lastModified()
    }
}