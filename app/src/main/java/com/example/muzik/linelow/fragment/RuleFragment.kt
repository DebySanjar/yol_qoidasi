package com.example.muzik.linelow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muzik.linelow.R
import com.example.muzik.linelow.adapters.RuleAdapter
import com.example.muzik.linelow.databinding.FragmentRuleBinding
import com.example.muzik.linelow.db.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RuleFragment : Fragment() {
    private var _binding: FragmentRuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RuleAdapter
    private lateinit var category: String

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): RuleFragment {
            val fragment = RuleFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(ARG_CATEGORY) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RuleAdapter(
            onLikeClick = { rule ->
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedRule = rule.copy(isLiked = !rule.isLiked)
                    DatabaseProvider.getDatabase(requireContext()).ruleDao().update(updatedRule)
                    loadRules()
                    if (updatedRule.isLiked) {
                        CoroutineScope(Dispatchers.Main).launch {
                            findNavController().navigate(R.id.action_homeFragment_to_likeFragment)
                        }
                    }
                }
            },
            onEditClick = { /* Hozircha bo‘sh, keyin qo‘shiladi */ },
            onDeleteClick = { rule ->
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseProvider.getDatabase(requireContext()).ruleDao().delete(rule)
                    loadRules()
                }
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        loadRules()
    }

    private fun loadRules() {
        CoroutineScope(Dispatchers.IO).launch {
            val rules = DatabaseProvider.getDatabase(requireContext()).ruleDao()
                .getRulesByCategory(category)
            CoroutineScope(Dispatchers.Main).launch {
                adapter.submitList(rules)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}