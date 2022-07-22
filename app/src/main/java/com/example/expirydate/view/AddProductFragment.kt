package com.example.expirydate.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.expirydate.R
import com.example.expirydate.databinding.FragmentAddProductBinding
import com.example.expirydate.viewModel.AddProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : Fragment() {

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding: FragmentAddProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        initUI()
        initListener()
        return binding.root
    }

    private fun initUI() {
        binding.TitleViewId.pageNameTv.text = "Add New product"
    }

    private fun initListener() {
        binding.TitleViewId.backIv.setOnClickListener(this::onBackPressed)
    }

    private fun onBackPressed(v: View) {
        activity?.onBackPressed()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}