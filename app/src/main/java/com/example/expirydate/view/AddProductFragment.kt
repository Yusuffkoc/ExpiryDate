package com.example.expirydate.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.expirydate.R
import com.example.expirydate.base.BaseFragment
import com.example.expirydate.databinding.FragmentAddProductBinding
import com.example.expirydate.model.Product
import com.example.expirydate.util.ImageUploadUtil
import com.example.expirydate.viewModel.AddProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AddProductFragment : BaseFragment() {

    companion object {
        fun newInstance() = AddProductFragment()
    }

    private lateinit var viewModel: AddProductViewModel
    private lateinit var binding: FragmentAddProductBinding
    private var updateProduct: Boolean = false
    private var product: Product? = null
    private var imageUrl: String? = null
    private val dateFormat = "dd.MM.yyyy"
    private val outputDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

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
            setExistingData()
        }
    }

    private fun setExistingData() {
        binding.TitleViewId.pageNameTv.text = getString(R.string.update_new_product)
        binding.productNameEt.setText(product!!.productName)
        binding.productDetailEt.setText(product!!.productDetail)
        binding.selectDate.text = product!!.expiryDate
        imageUrl=product?.imageUrl
        if (!product!!.imageUrl.isNullOrEmpty()) {
            binding.productImageIv.setImageURI(product!!.imageUrl?.toUri())
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
        binding.dateSelectCv.setOnClickListener(this::selectDate)
    }

    private fun selectDate(v: View) {
        binding.productDetailEt.isFocusable = false
        val picker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Choose Date..")
                .build()

        picker.show(this.parentFragmentManager, "")

        picker.addOnPositiveButtonClickListener {
//            showToast(it.toString())
            binding.selectDate.text = outputDateFormat.format(it)
        }
        picker.addOnNegativeButtonClickListener {
//            showToast("negatif")
        }
        picker.addOnCancelListener {
//            showToast("cancel")
        }
        picker.addOnDismissListener {
//            showToast("dismiss")
        }
    }

    private fun addImage(v: View) {
        ImageUploadUtil.uploadPhoto(this)
    }

    private fun addProduct(v: View) {
        if (validProductDetails()) {
            val productName = binding.productNameEt.text.trim().toString()
            val productDetails = binding.productDetailEt.text.trim().toString()
            if (imageUrl == null) {
                imageUrl = ""
            }
            val expirtydate = binding.selectDate.text.trim().toString()
            if (updateProduct) {
                val product = getProductWillUpdate(
                    product!!.id,
                    productName,
                    productDetails,
                    expirtydate,
                    imageUrl
                )
                viewModel.updateProduct(product = product)
            } else {
                val product =
                    getProductWillUpdate(0, productName, productDetails, expirtydate, imageUrl)
                viewModel.addProductToDatabase(product = product)
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
        id: Int,
        productName: String,
        productDetails: String,
        expirtyDate: String,
        imageUrl: String?
    ): Product {
        return Product(
            id = id,
            productName = productName,
            productDetail = productDetails,
            expiryDate = expirtyDate,
            imageUrl = imageUrl
        )
    }

    private fun validProductDetails(): Boolean {
        if (binding.productNameEt.text.trim().isEmpty()) {
            showToast(getString(R.string.empty_product_name))
            return false
        }
        if (binding.selectDate.text.trim().isEmpty()) {
            showToast(getString(R.string.empty_product_date))
            return false
        }
        if (!checkDateArrange()) {
            showToast(getString(R.string.empty_product_valid_date))
            return false
        }
        return true
    }

    private fun checkDateArrange(): Boolean {
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern(dateFormat)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val current = LocalDateTime.now().format(formatter)
        val currentDate: Date = outputDateFormat.parse(current)
        val userSelectedDate: Date = outputDateFormat.parse(binding.selectDate.text.toString())
        val result = currentDate.compareTo(userSelectedDate)
        return when {
            result > 0 -> {
                false
            }
            else -> {
                true
            }
        }
    }

    private fun onBackPressed(v: View) {
        backPressed()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddProductViewModel::class.java]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            showToast(getString(R.string.uploaded_image))
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            binding.productImageIv.setImageURI(uri)
            // Use Uri object instead of File to avoid storage permissions
            imageUrl = uri.toString()
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast(getString(R.string.task_canceled))
        }
    }

}