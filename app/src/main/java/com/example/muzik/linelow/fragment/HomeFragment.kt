package com.example.muzik.linelow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.muzik.linelow.R
import com.example.muzik.linelow.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categories = listOf("Ogohlantiruvchi", "Taqiqlovchi", "Imtiyozli", "Buyuruvchi")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.viewPager.adapter = RulePagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

            val tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
            val tabText = tabView.findViewById<TextView>(R.id.tab_text)
            tabText.text = categories[position]
            tab.customView = tabView
        }.attach()


        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private inner class RulePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = categories.size
        override fun createFragment(position: Int): Fragment =
            RuleFragment.newInstance(categories[position])
    }
}