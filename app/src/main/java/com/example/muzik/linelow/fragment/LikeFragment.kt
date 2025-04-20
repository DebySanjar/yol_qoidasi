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
import com.example.muzik.linelow.databinding.FragmentLikeBinding
import com.example.muzik.linelow.db.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikeFragment : Fragment() {
    private var _binding: FragmentLikeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = RuleAdapter(
            onLikeClick = { rule ->
                CoroutineScope(Dispatchers.IO).launch {
                    val updatedRule = rule.copy(isLiked = !rule.isLiked)
                    DatabaseProvider.getDatabase(requireContext()).ruleDao().update(updatedRule)
                    loadLikedRules()
                    if (!updatedRule.isLiked) {
                        CoroutineScope(Dispatchers.Main).launch {
                            findNavController().navigate(R.id.action_likeFragment_to_homeFragment)
                        }
                    }
                }
            },
            onEditClick = { /* Keyinchalik */ },
            onDeleteClick = { rule ->
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseProvider.getDatabase(requireContext()).ruleDao().delete(rule)
                    loadLikedRules()
                }
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
        loadLikedRules()
    }

    private fun loadLikedRules() {
        CoroutineScope(Dispatchers.IO).launch {
            val likedRules =
                DatabaseProvider.getDatabase(requireContext()).ruleDao().getLikedRules()
            CoroutineScope(Dispatchers.Main).launch {
                adapter.submitList(likedRules)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}