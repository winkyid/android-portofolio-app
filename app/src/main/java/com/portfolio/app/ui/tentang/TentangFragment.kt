package com.portfolio.app.ui.tentang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.portfolio.app.data.DatabaseHelper
import com.portfolio.app.databinding.FragmentTentangBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TentangFragment : Fragment() {

    private var _binding: FragmentTentangBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper

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
        loadProfile()
    }

    private fun loadProfile() {
        lifecycleScope.launch(Dispatchers.IO) {
            val profile = dbHelper.getProfile()
            withContext(Dispatchers.Main) {
                profile?.let {
                    binding.tvNama.text = it.nama
                    binding.tvTitle.text = it.title
                    binding.tvBio.text = it.bio
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
