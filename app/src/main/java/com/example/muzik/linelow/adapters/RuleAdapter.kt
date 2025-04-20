package com.example.muzik.linelow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.linelow.databinding.ItemRuleBinding
import java.io.File
import android.graphics.BitmapFactory
import com.example.muzik.linelow.R
import com.example.muzik.linelow.db.Rule

class RuleAdapter(
    private val onLikeClick: (Rule) -> Unit,
    private val onEditClick: (Rule) -> Unit,
    private val onDeleteClick: (Rule) -> Unit
) : RecyclerView.Adapter<RuleAdapter.RuleViewHolder>() {

    private var rules: List<Rule> = emptyList()

    fun submitList(newRules: List<Rule>) {
        rules = newRules
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleViewHolder {
        val binding = ItemRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RuleViewHolder, position: Int) {
        holder.bind(rules[position])
    }

    override fun getItemCount(): Int = rules.size

    inner class RuleViewHolder(private val binding: ItemRuleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rule: Rule) {
            binding.ruleTitle.text = rule.title
            val file = File(rule.imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding.ruleImage.setImageBitmap(bitmap)
            }
            binding.likeButton.setImageResource(if (rule.isLiked) R.drawable.liked_icon else R.drawable.item_like)
            binding.likeButton.setOnClickListener { onLikeClick(rule) }
            binding.editButton.setOnClickListener { onEditClick(rule) }
            binding.deleteButton.setOnClickListener { onDeleteClick(rule) }
        }
    }
}