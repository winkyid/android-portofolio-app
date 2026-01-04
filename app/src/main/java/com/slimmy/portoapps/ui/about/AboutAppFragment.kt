package com.slimmy.portoapps.ui.about

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.slimmy.portoapps.R
import com.slimmy.portoapps.databinding.FragmentAboutAppBinding
import com.slimmy.portoapps.util.AppConfig
import com.slimmy.portoapps.util.CustomToast
import com.slimmy.portoapps.util.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding get() = _binding!!
    private lateinit var appConfig: AppConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        appConfig = AppConfig(requireContext())

        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCheckUpdate.setOnClickListener {
            checkUpdate()
        }

        binding.btnClearDownloads.setOnClickListener {
            showClearConfirmationDialog()
        }

        // Penghubungan tombol File Manager yang terlewat
        binding.btnFileManager.setOnClickListener {
            findNavController().navigate(R.id.action_aboutAppFragment_to_fileManagerFragment)
        }

        displayAppInfo()
        
        lifecycleScope.launch {
            val savedVersion = appConfig.latestVersion.first()
            binding.tvLatestVersion.text = savedVersion
        }
    }

    private fun displayAppInfo() {
        val context = requireContext()
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                context.packageManager.getPackageInfo(context.packageName, 0)
            }

            binding.tvAppName.text = getString(context.applicationInfo.labelRes)
            binding.tvAppVersion.text = packageInfo.versionName
            binding.tvAppPackage.text = context.packageName
            binding.tvAppDeveloper.text = "Winky Kurniatama"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun showClearConfirmationDialog() {
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

        tvTitle.text = "Bersihkan Data"
        tvMessage.text = "Semua file CV yang telah diunduh di folder Download akan dihapus secara permanen. Lanjutkan?"
        ivIcon.setImageResource(R.drawable.ic_refresh)
        ivIcon.setColorFilter(Color.parseColor("#FF3B30"))
        
        btnConfirm.text = "Hapus"
        btnConfirm.setBackgroundColor(Color.parseColor("#FF3B30"))
        btnCancel.text = "Batal"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            clearAppDownloads()
        }

        dialog.show()
    }

    private fun clearAppDownloads() {
        val pdfGenerator = PdfGenerator(requireContext())
        val folder = pdfGenerator.getAppDownloadFolder()
        
        if (folder.exists() && folder.isDirectory) {
            val files = folder.listFiles()
            var deletedCount = 0
            files?.forEach { file ->
                if (file.delete()) deletedCount++
            }
            CustomToast.show(requireContext(), "$deletedCount file berhasil dihapus", R.drawable.ic_refresh)
        } else {
            CustomToast.show(requireContext(), "Folder download tidak ditemukan", R.drawable.ic_info)
        }
    }

    private fun checkUpdate() {
        binding.btnCheckUpdate.isEnabled = false
        binding.btnCheckUpdate.text = "Checking..."

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://info-winkyid.vercel.app/api/version.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(response)
                    val latestVersion = jsonObject.getString("latestVersionName")

                    appConfig.saveLatestVersion(latestVersion)

                    withContext(Dispatchers.Main) {
                        binding.tvLatestVersion.text = latestVersion
                        handleUpdateResult(latestVersion)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showErrorDialog("Gagal terhubung ke server.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showErrorDialog("Terjadi kesalahan koneksi.")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.btnCheckUpdate.isEnabled = true
                    binding.btnCheckUpdate.text = "Cek Update"
                }
            }
        }
    }

    private fun handleUpdateResult(latestVersion: String) {
        val currentVersion = binding.tvAppVersion.text.toString()

        if (currentVersion == latestVersion) {
            showUpdateDialog(
                title = "Sudah Terbaru",
                message = "Versi aplikasi Anda sudah yang terbaru ($currentVersion).",
                iconRes = R.drawable.ic_info,
                confirmText = "Oke",
                isUpdateAvailable = false
            )
        } else {
            showUpdateDialog(
                title = "Pembaruan Tersedia",
                message = "Versi terbaru ($latestVersion) tersedia. Versi Anda saat ini ($currentVersion) sudah usang. Disarankan untuk segera memperbarui aplikasi.",
                iconRes = R.drawable.ic_info,
                confirmText = "Update",
                isUpdateAvailable = true
            )
        }
    }

    private fun showUpdateDialog(
        title: String,
        message: String,
        iconRes: Int,
        confirmText: String,
        isUpdateAvailable: Boolean
    ) {
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
        btnConfirm.text = confirmText
        
        if (!isUpdateAvailable) {
            btnCancel.visibility = View.GONE
        } else {
            btnCancel.text = "Nanti"
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            if (isUpdateAvailable) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://portoapps-android.vercel.app/download/latest/"))
                startActivity(intent)
            }
        }

        dialog.show()
    }

    private fun showErrorDialog(error: String) {
        showUpdateDialog(
            title = "Kesalahan",
            message = error,
            iconRes = R.drawable.ic_info,
            confirmText = "Oke",
            isUpdateAvailable = false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}