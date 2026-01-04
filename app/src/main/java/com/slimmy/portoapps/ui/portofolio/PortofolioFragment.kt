package com.slimmy.portoapps.ui.portofolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.slimmy.portoapps.data.DatabaseHelper
import com.slimmy.portoapps.databinding.FragmentPortofolioBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PortofolioFragment : Fragment() {

    private var _binding: FragmentPortofolioBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: PortofolioAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortofolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = PortofolioAdapter()
        binding.rvPortofolio.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPortofolio.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val portofolio = dbHelper.getPortofolio()
            withContext(Dispatchers.Main) {
                adapter.submitList(portofolio)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
