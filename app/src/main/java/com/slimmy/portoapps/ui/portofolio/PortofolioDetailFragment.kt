package com.slimmy.portoapps.ui.portofolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.slimmy.portoapps.R
import com.slimmy.portoapps.data.DatabaseHelper
import com.slimmy.portoapps.databinding.FragmentPortofolioDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PortofolioDetailFragment : Fragment() {

    private var _binding: FragmentPortofolioDetailBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortofolioDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        dbHelper = DatabaseHelper(requireContext())
        
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        
        val portofolioId = arguments?.getInt("portofolioId") ?: -1
        if (portofolioId != -1) {
            loadDetail(portofolioId)
        }
    }

    private fun loadDetail(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val portofolio = dbHelper.getPortofolioById(id)
            withContext(Dispatchers.Main) {
                portofolio?.let { item ->
                    binding.tvJudul.text = item.judul
                    binding.tvType.text = item.type
                    binding.tvTeknologi.text = item.teknologi
                    binding.tvDeskripsi.text = item.deskripsi
                    
                    binding.btnSourceCode.setOnClickListener {
                        openInAppWebView(item.sourceLink)
                    }
                    
                    binding.btnLiveView.setOnClickListener {
                        openInAppWebView(item.liveLink)
                    }
                }
            }
        }
    }

    private fun openInAppWebView(url: String) {
        if (url.isNotEmpty()) {
            val bundle = Bundle().apply {
                putString("url", url)
            }
            findNavController().navigate(R.id.action_portofolioDetailFragment_to_appWebViewFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}