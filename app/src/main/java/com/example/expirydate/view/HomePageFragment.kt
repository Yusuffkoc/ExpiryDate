package com.example.expirydate.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.expirydate.adapter.ProductAdapter
import com.example.expirydate.databinding.FragmentHomePageBinding
import com.example.expirydate.viewModel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {

    private lateinit var binding: FragmentHomePageBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        initObserve()
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.addProduct.setOnClickListener {
            showAddWordDialog()
        }
    }

    private fun initObserve() {
        viewModel.getAllWords().observe(this) {
            productAdapter.submitList(it)
        }
    }

    private fun setUpRecyclerView() {

        binding.rvProducts.apply {
            productAdapter = ProductAdapter()
            layoutManager = LinearLayoutManager(this.context)
            adapter = productAdapter
        }
    }

    private fun showAddWordDialog() {
        MaterialDialog(requireContext()).show {
            input { dialog, text ->
                viewModel.saveWord(text.toString()).observe(viewLifecycleOwner) {
                    if (it) {
                        Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this.context, "Failure", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            positiveButton(text = "Submit")
            negativeButton {"cancel"}
        }
    }

}