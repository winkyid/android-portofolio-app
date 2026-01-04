package com.slimmy.portoapps.ui.skill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.slimmy.portoapps.data.DatabaseHelper
import com.slimmy.portoapps.databinding.FragmentSkillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SkillFragment : Fragment() {

    private var _binding: FragmentSkillBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: SkillAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSkillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())
        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = SkillAdapter()
        binding.rvSkill.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSkill.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val allSkills = dbHelper.getSkills()
            
            // Group skills by category
            val groupedSkills = allSkills.groupBy { it.kategori }
                .map { (category, skills) ->
                    SkillGroup(category, skills)
                }
            
            withContext(Dispatchers.Main) {
                adapter.submitList(groupedSkills)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
