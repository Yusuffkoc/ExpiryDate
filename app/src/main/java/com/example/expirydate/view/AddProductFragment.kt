package com.example.expirydate.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.example.expirydate.R
import com.example.expirydate.base.BaseFragment
import com.example.expirydate.databinding.FragmentAddProductBinding
import com.example.expirydate.model.Product
import com.example.expirydate.util.ImageUploadUtil
import com.example.expirydate.viewModel.AddProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BaseFragment() {

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding: FragmentAddProductBinding
    private var updateProduct: Boolean = false
    private var product: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        initUI()
        initData()
        initListener()
        return binding.root
    }

    private fun initData() {
        product = arguments?.getParcelable<Product>("product")
        if (product != null) {
            updateProduct = true
            changeSubmitButtonText()
            binding.TitleViewId.pageNameTv.text = getString(R.string.update_new_product)
            binding.productNameEt.setText(product!!.productName)
            binding.productDetailEt.setText(product!!.productDetail)
            if (!product!!.imageUrl.isNullOrEmpty()) {
                binding.productImageIv.setImageURI(product!!.imageUrl?.toUri())
            }
        }
    }

    private fun changeSubmitButtonText() {
        binding.addProductBtn.text = getString(R.string.update_btn)
    }

    private fun initUI() {
        binding.TitleViewId.pageNameTv.text = getString(R.string.add_new_product)
    }

    private fun initListener() {
        binding.TitleViewId.backIv.setOnClickListener(this::onBackPressed)
        binding.addProductBtn.setOnClickListener(this::addProduct)
        binding.productImageIv.setOnClickListener(this::addImage)
    }

    private fun addImage(v: View) {
        ImageUploadUtil.uploadPhoto(this)
    }

    private fun addProduct(v: View) {
        if (validProductDetails()) {
            val productName = binding.productNameEt.text.trim().toString()
            val productDetails = binding.productDetailEt.text.trim().toString()
            val imageUrl = product?.imageUrl
            if (updateProduct) {
                val product = getProductWillUpdate(productName, productDetails, imageUrl)
                viewModel.updateProduct(product = product)
            } else {
                viewModel.addProductToDatabase(
                    productName = productName,
                    productDetail = productDetails
                )
            }
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    backPressed()
                }, 300)
            }
        }
        updateProduct = false
    }

    private fun getProductWillUpdate(
        productName: String,
        productDetails: String,
        imageUrl: String?
    ): Product {
        return Product(
            id = product!!.id,
            productName = productName,
            productDetail = productDetails,
            "12.22.2222",
            imageUrl = imageUrl
        )
    }

    private fun validProductDetails(): Boolean {
        if (binding.productNameEt.text.isNullOrEmpty()) {
            showToast(getString(R.string.empty_product_name))
            return false
        }
        return true
    }

    private fun onBackPressed(v: View) {
        backPressed()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            showToast(getString(R.string.uploaded_image))
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            binding.productImageIv.setImageURI(uri)
            // Use Uri object instead of File to avoid storage permissions
            product.let { it?.imageUrl = uri.toString() }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast(getString(R.string.task_canceled))
        }
    }

}