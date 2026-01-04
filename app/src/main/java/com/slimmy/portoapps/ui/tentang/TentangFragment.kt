package com.slimmy.portoapps.ui.tentang

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.slimmy.portoapps.R
import com.slimmy.portoapps.data.DatabaseHelper
import com.slimmy.portoapps.databinding.FragmentTentangBinding
import com.slimmy.portoapps.util.CustomToast
import com.slimmy.portoapps.util.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var pdfGenerator: PdfGenerator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTentangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        pdfGenerator = PdfGenerator(requireContext())
        
        loadProfile()
        setupButtons()
    }

    private fun setupButtons() {
        binding.btnDownloadCv.setOnClickListener {
            showDownloadConfirmationDialog()
        }
    }

    private fun showDownloadConfirmationDialog() {
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

        tvTitle.text = "Unduh CV"
        tvMessage.text = "Apakah Anda ingin membuat dan mengunduh CV dalam format ATS?"
        ivIcon.setImageResource(R.drawable.ic_download)
        btnConfirm.text = "Unduh"
        btnCancel.text = "Batal"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            processDownload()
        }

        dialog.show()
    }

    private fun processDownload() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                CustomToast.show(requireContext(), "Memulai pengunduhan CV...", R.drawable.ic_download)
            }
            
            val file = pdfGenerator.generateCV()
            
            withContext(Dispatchers.Main) {
                if (file != null) {
                    CustomToast.show(requireContext(), "CV Berhasil diunduh!", R.drawable.ic_info)
                } else {
                    CustomToast.show(requireContext(), "Gagal mengunduh CV", R.drawable.ic_info)
                }
            }
        }
    }

    private fun loadProfile() {
        lifecycleScope.launch(Dispatchers.IO) {
            val profile = dbHelper.getProfile()
            val stats = dbHelper.getDataStats() // Ambil data statistik riil
            
            withContext(Dispatchers.Main) {
                profile?.let { data ->
                    binding.tvNama.text = data.nama
                    binding.tvTitle.text = data.title
                    binding.tvBio.text = data.bio
                    
                    binding.tvTtl.text = "${data.tempatLahir}, ${data.tanggalLahir}"
                    binding.tvGender.text = data.gender
                    binding.tvAgama.text = data.agama
                    binding.tvWarganegara.text = data.warganegara
                    binding.tvStatus.text = data.status

                    // Update Stats dengan data riil dari DB
                    binding.tvStatPengalaman.text = "${stats.first}+"
                    binding.tvStatPendidikan.text = "${stats.second}+"
                    binding.tvStatProyek.text = "${stats.third}+"

                    binding.mainContent.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}