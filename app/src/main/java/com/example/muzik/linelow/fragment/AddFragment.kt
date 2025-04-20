package com.example.muzik.linelow.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.muzik.linelow.R
import com.example.muzik.linelow.databinding.FragmentAddBinding
import com.example.muzik.linelow.db.DatabaseProvider
import com.example.muzik.linelow.db.Rule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private var imagePath: String? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val bitmap = BitmapFactory.decodeStream(
                        requireContext().contentResolver.openInputStream(uri)
                    )
                    binding.imageView.setImageBitmap(bitmap)
                    saveImageToFile(bitmap)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf("Ogohlantiruvchi", "Taqiqlovchi", "Imtiyozli", "Buyuruvchi")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        binding.selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImage.launch(intent)
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val desc = binding.descEditText.text.toString()
            val category = binding.categorySpinner.selectedItem.toString()

            if (title.isNotEmpty() && desc.isNotEmpty() && imagePath != null) {
                val rule = Rule(
                    title = title,
                    description = desc,
                    imagePath = imagePath!!,
                    category = category
                )
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseProvider.getDatabase(requireContext()).ruleDao().insert(rule)
                    CoroutineScope(Dispatchers.Main).launch {
                        findNavController().navigate(R.id.action_addFragment_to_homeFragment)
                    }
                }
            }
        }
    }

    private fun saveImageToFile(bitmap: Bitmap) {
        val file = File(requireContext().filesDir, "rule_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        imagePath = file.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}