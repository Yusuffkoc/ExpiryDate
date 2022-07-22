package com.example.expirydate.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.expirydate.R
import com.example.expirydate.adapter.ProductAdapter
import com.example.expirydate.databinding.FragmentHomePageBinding
import com.example.expirydate.model.Product
import com.example.expirydate.viewModel.ProductsViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment(), ProductAdapter.ClickListener {

    private lateinit var binding: FragmentHomePageBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var productImage: ImageView? = null
    private var uploadedImageForProduct: Product? = null


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
//        binding.addProduct.setOnClickListener {
//            showAddWordDialog()
//        }
        binding.addProduct.setOnClickListener {
            navigateAddNewProductPage()
        }
        productAdapter.setClickListener(this)
    }

    private fun navigateAddNewProductPage() {
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_homePageFragment_to_addProductFragment)
    }

    private fun initObserve() {
        viewModel.getAllWords().observe(viewLifecycleOwner) {
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
                viewModel.saveProductToDatabase(text.toString()).observe(viewLifecycleOwner) {
                    if (it) {
                        Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this.context, "Failure", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            positiveButton(text = "Submit")
            negativeButton { "cancel" }
        }
    }

    override fun deleteProduct(product: Product) {
        viewModel.let {
            viewModel.deleteProduct(product)
            Toast.makeText(this.context, "${product.productName}  Deleted.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun uploadImage(iv: ImageView, product: Product) {
        productImage = iv
        uploadedImageForProduct = product
        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            Toast.makeText(this.context, "uploaded Image", Toast.LENGTH_SHORT).show()
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            productImage?.setImageURI(uri)

            // Use Uri object instead of File to avoid storage permissions
            saveImageToDatabase(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this.context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, getString(R.string.task_canceled), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun saveImageToDatabase(uri: Uri) {
        uploadedImageForProduct?.let { viewModel.saveImageUrl(uri.toString(), it.id) }
        viewModel.getAllWords()
        productImage = null
    }

}