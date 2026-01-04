package com.slimmy.portoapps

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.slimmy.portoapps.databinding.ActivityMainBinding
import com.slimmy.portoapps.util.AppConfig
import com.slimmy.portoapps.util.ThemeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var themeManager: ThemeManager
    private lateinit var appConfig: AppConfig
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeManager = ThemeManager(this)
        appConfig = AppConfig(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupNavigation()
        setupThemeToggle()
        setupAboutAppButton()
        setupBackPressHandler()
        
        lifecycleScope.launch {
            val isDarkMode = themeManager.isDarkMode.first()
            updateThemeIcon(isDarkMode)
        }

        if (savedInstanceState == null) {
            binding.root.postDelayed({
                showWelcomeDialog()
                autoCheckUpdate()
            }, 600)
        }
    }

    private fun setupViewPager() {
        pagerAdapter = MainPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 5
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val activeTab = when(position) {
                    0 -> binding.tabTentang
                    1 -> binding.tabPendidikan
                    2 -> binding.tabPengalaman
                    3 -> binding.tabSkill
                    4 -> binding.tabPortofolio
                    5 -> binding.tabKontak
                    else -> binding.tabTentang
                }
                updateTabUI(activeTab)
                
                val titles = listOf("Profil Saya", "Pendidikan", "Pengalaman", "Keahlian", "Portofolio", "Kontak")
                binding.tvToolbarTitle.text = titles[position]
            }
        })
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val navController = navHostFragment.navController
                
                if (binding.navHostFragment.isVisible) {
                    navController.navigateUp()
                } else {
                    showExitConfirmationDialog()
                }
            }
        })
    }

    private fun showExitConfirmationDialog() {
        val dialog = Dialog(this)
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

        tvTitle.text = "Keluar Aplikasi"
        tvMessage.text = "Apakah Anda yakin ingin menutup aplikasi Portofolio?"
        ivIcon.setImageResource(R.drawable.ic_refresh)
        ivIcon.setColorFilter(Color.parseColor("#FF3B30"))
        
        btnConfirm.text = "Keluar"
        btnConfirm.setBackgroundColor(Color.parseColor("#FF3B30"))
        btnCancel.text = "Batal"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    private fun showWelcomeDialog() {
        val dialog = Dialog(this)
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

        tvTitle.text = "Selamat Datang!"
        tvMessage.text = "Selamat datang di aplikasi Portofolio Saya. Jelajahi pengalaman, karya, dan keahlian saya di sini."
        
        // --- MODIFIKASI ICON WELCOME AGAR ROUNDED ---
        // Karena layout aslinya hanya ImageView, kita buatkan efek rounded secara dinamis 
        // atau kita manipulasi propertiesnya. Cara paling bersih: berikan background rounded tipis.
        
        ImageViewCompat.setImageTintList(ivIcon, null)
        ivIcon.setImageResource(R.drawable.icon_apps)
        ivIcon.setPadding(0, 0, 0, 0)
        
        // Gunakan background rounded tipis (sama dengan di tentang aplikasi)
        ivIcon.setBackgroundResource(R.drawable.bg_ios_card) 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ivIcon.clipToOutline = true
        }
        
        btnConfirm.text = "Mari Mulai"
        btnCancel.visibility = View.GONE

        btnConfirm.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun autoCheckUpdate() {
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
                    val currentVersion = getCurrentVersion()
                    if (currentVersion != latestVersion) {
                        withContext(Dispatchers.Main) {
                            showUpdateDialog(latestVersion, currentVersion)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCurrentVersion(): String {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            packageInfo.versionName ?: ""
        } catch (e: Exception) { "" }
    }

    private fun showUpdateDialog(latestVersion: String, currentVersion: String) {
        val dialog = Dialog(this)
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

        tvTitle.text = "Pembaruan Tersedia"
        tvMessage.text = "Versi terbaru ($latestVersion) tersedia. Versi Anda saat ini ($currentVersion) sudah usang. Disarankan untuk segera memperbarui aplikasi."
        ivIcon.setImageResource(R.drawable.ic_info)
        btnConfirm.text = "Update"
        btnCancel.text = "Nanti"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://portoapps-android.vercel.app/download/latest/"))
            startActivity(intent)
        }
        dialog.show()
    }

    fun navigateToDetail(bundle: Bundle) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
            
        navHostFragment.navController.navigate(R.id.portofolioDetailFragment, bundle, navOptions)
    }

    private fun setupNavigation() {
        val tabs = listOf(
            binding.tabTentang to 0,
            binding.tabPendidikan to 1,
            binding.tabPengalaman to 2,
            binding.tabSkill to 3,
            binding.tabPortofolio to 4,
            binding.tabKontak to 5
        )

        tabs.forEach { (layout, position) ->
            layout.setOnClickListener {
                val currentPos = binding.viewPager.currentItem
                val diff = abs(currentPos - position)
                
                if (diff > 1) {
                    binding.viewPager.animate()
                        .alpha(0f)
                        .setDuration(150)
                        .withEndAction {
                            binding.viewPager.setCurrentItem(position, false)
                            binding.viewPager.animate().alpha(1f).setDuration(150).start()
                        }
                        .start()
                } else {
                    binding.viewPager.setCurrentItem(position, true)
                }
            }
        }
        
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isOverlay = destination.id == R.id.aboutAppFragment || destination.id == R.id.fileManagerFragment || destination.id == R.id.pdfViewerFragment || destination.id == R.id.portofolioDetailFragment || destination.id == R.id.appWebViewFragment
            
            if (isOverlay) {
                binding.navHostFragment.visibility = View.VISIBLE
                binding.viewPager.visibility = View.GONE
                binding.appBar.visibility = View.GONE
                binding.iosTabBar.visibility = View.GONE
            } else {
                binding.navHostFragment.visibility = View.GONE
                binding.viewPager.visibility = View.VISIBLE
                binding.appBar.visibility = View.VISIBLE
                binding.iosTabBar.visibility = View.VISIBLE
            }
        }
    }

    private fun clearTabUI() {
        val tabContainer = binding.iosTabBar
        val inactiveColor = "#8E8E93"
        for (i in 0 until tabContainer.childCount) {
            val child = tabContainer.getChildAt(i) as? LinearLayout ?: continue
            val icon = child.getChildAt(0) as ImageView
            val text = child.getChildAt(1) as TextView
            ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(Color.parseColor(inactiveColor)))
            text.setTextColor(Color.parseColor(inactiveColor))
        }
    }

    private fun updateTabUI(activeLayout: LinearLayout) {
        val tabContainer = binding.iosTabBar
        val isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val activeColor = if (isDark) "#0A84FF" else "#007AFF"
        val inactiveColor = "#8E8E93"
        for (i in 0 until tabContainer.childCount) {
            val child = tabContainer.getChildAt(i) as? LinearLayout ?: continue
            val icon = child.getChildAt(0) as ImageView
            val text = child.getChildAt(1) as TextView
            if (child == activeLayout) {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(Color.parseColor(activeColor)))
                text.setTextColor(Color.parseColor(activeColor))
            } else {
                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(Color.parseColor(inactiveColor)))
                text.setTextColor(Color.parseColor(inactiveColor))
            }
        }
    }

    private fun setupThemeToggle() {
        binding.btnThemeToggle.setOnClickListener {
            lifecycleScope.launch {
                val currentMode = themeManager.isDarkMode.first()
                val newMode = !currentMode
                themeManager.setDarkMode(newMode)
                if (newMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                updateThemeIcon(newMode)
            }
        }
    }

    private fun setupAboutAppButton() {
        binding.btnAboutApp.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_up).setExitAnim(android.R.anim.fade_out).setPopEnterAnim(android.R.anim.fade_in).setPopExitAnim(R.anim.slide_out_down).build()
            navHostFragment.navController.navigate(R.id.aboutAppFragment, null, navOptions)
        }
    }

    private fun updateThemeIcon(isDarkMode: Boolean) {
        if (isDarkMode) binding.btnThemeToggle.setImageResource(R.drawable.ic_light_mode) else binding.btnThemeToggle.setImageResource(R.drawable.ic_dark_mode)
    }
}
