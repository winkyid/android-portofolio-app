package com.slimmy.portoapps.ui.filemanager

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.slimmy.portoapps.R
import com.slimmy.portoapps.databinding.FragmentFileManagerBinding
import com.slimmy.portoapps.util.CustomToast
import com.slimmy.portoapps.util.PdfGenerator
import java.io.File

class FileManagerFragment : Fragment() {

    private var _binding: FragmentFileManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var pdfGenerator: PdfGenerator
    private var fileList = mutableListOf<File>()
    private lateinit var adapter: FileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFileManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        pdfGenerator = PdfGenerator(requireContext())
        setupRecyclerView()
        loadFiles()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        adapter = FileAdapter(
            onDelete = { file -> showDeleteConfirmation(file) },
            onOpenBuiltIn = { file -> openPdfBuiltIn(file) },
            onOpenFolder = { openAppFolder() },
            onShare = { file -> shareFile(file) }
        )
        binding.rvFiles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFiles.adapter = adapter
    }

    private fun loadFiles() {
        try {
            val folder = pdfGenerator.getAppDownloadFolder()
            val files = folder.listFiles { file -> file.extension == "pdf" }
            fileList = files?.toMutableList() ?: mutableListOf()
            
            if (fileList.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvFiles.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.rvFiles.visibility = View.VISIBLE
                adapter.submitList(fileList.sortedByDescending { it.lastModified() })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDeleteConfirmation(file: File) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_custom_confirmation)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val tvTitle = dialog.findViewById<TextView>(R.id.tv_dialog_title)
        val tvMessage = dialog.findViewById<TextView>(R.id.tv_dialog_message)
        val ivIcon = dialog.findViewById<ImageView>(R.id.iv_dialog_icon)
        val btnCancel = dialog.findViewById<MaterialButton>(R.id.btn_dialog_cancel)
        val btnConfirm = dialog.findViewById<MaterialButton>(R.id.btn_dialog_confirm)

        tvTitle.text = "Hapus File"
        tvMessage.text = "Hapus permanen file ${file.name}?"
        ivIcon.setImageResource(R.drawable.ic_refresh)
        ivIcon.setColorFilter(Color.parseColor("#FF3B30"))
        
        btnConfirm.text = "Hapus"
        btnConfirm.setBackgroundColor(Color.parseColor("#FF3B30"))

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            if (file.delete()) {
                CustomToast.show(requireContext(), "File dihapus", R.drawable.ic_refresh)
                loadFiles()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openPdfBuiltIn(file: File) {
        val bundle = Bundle().apply {
            putString("filePath", file.absolutePath)
        }
        findNavController().navigate(R.id.action_fileManagerFragment_to_pdfViewerFragment, bundle)
    }

    private fun openAppFolder() {
        try {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Download%2F" + requireContext().getString(requireContext().applicationInfo.labelRes))
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.parse("content://com.android.externalstorage.documents/root/primary")
            intent.setDataAndType(uri, "vnd.android.document/root")
            startActivity(intent)
        }
    }

    private fun shareFile(file: File) {
        try {
            val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            // Pastikan menggunakan context dari activity untuk memunculkan chooser
            requireActivity().startActivity(Intent.createChooser(intent, "Share CV via"))
        } catch (e: Exception) {
            e.printStackTrace()
            CustomToast.show(requireContext(), "Gagal berbagi file", R.drawable.ic_info)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}