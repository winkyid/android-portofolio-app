package com.slimmy.portoapps.ui.kontak

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.slimmy.portoapps.R
import com.slimmy.portoapps.data.DatabaseHelper
import com.slimmy.portoapps.databinding.FragmentKontakBinding

class KontakFragment : Fragment() {

    private var _binding: FragmentKontakBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKontakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = DatabaseHelper(requireContext())
        loadData()
    }

    private fun loadData() {
        val profile = dbHelper.getProfile()
        profile?.let { data ->
            binding.tvEmail.text = data.email
            binding.tvPhone.text = data.phone
            binding.tvLocation.text = data.address

            binding.cardLokasi.setOnClickListener {
                showConfirmationDialog(
                    title = "Buka Lokasi",
                    message = "Anda akan diarahkan ke Google Maps untuk melihat lokasi:\n${data.address}",
                    iconRes = R.drawable.ic_location,
                    url = data.mapsUrl
                )
            }

            binding.cardEmail.setOnClickListener {
                val emailUrl = "mailto:${data.email}"
                showConfirmationDialog(
                    title = "Kirim Email",
                    message = "Anda akan diarahkan ke aplikasi Email untuk menghubungi:\n${data.email}",
                    iconRes = R.drawable.ic_email,
                    url = emailUrl
                )
            }

            binding.cardTelepon.setOnClickListener {
                val cleanPhone = data.phone.replace(Regex("[^0-9]"), "")
                val finalPhone = if (cleanPhone.startsWith("0")) {
                    "62" + cleanPhone.substring(1)
                } else {
                    cleanPhone
                }
                val waUrl = "https://wa.me/$finalPhone"
                showConfirmationDialog(
                    title = "Hubungi WhatsApp",
                    message = "Anda akan diarahkan ke WhatsApp untuk mengirim pesan ke nomor:\n${data.phone}",
                    iconRes = R.drawable.ic_phone,
                    url = waUrl
                )
            }

            binding.rowSocial.getChildAt(0).setOnClickListener {
                showConfirmationDialog("Buka Website", "Buka link website portofolio?", R.drawable.ic_person, data.website)
            }
            binding.rowSocial.getChildAt(1).setOnClickListener {
                showConfirmationDialog("Buka GitHub", "Lihat profil GitHub saya?", R.drawable.ic_github, data.github)
            }
            binding.rowSocial.getChildAt(2).setOnClickListener {
                showConfirmationDialog("Buka LinkedIn", "Hubungkan melalui LinkedIn?", R.drawable.ic_linkedin, data.linkedin)
            }
        }
    }

    private fun showConfirmationDialog(title: String, message: String, iconRes: Int, url: String) {
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

        tvTitle.text = title
        tvMessage.text = message
        ivIcon.setImageResource(iconRes)

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}