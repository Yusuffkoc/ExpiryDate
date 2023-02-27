package com.example.expirydate.view

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.example.expirydate.R
import com.example.expirydate.adapter.ProductAdapter
import com.example.expirydate.base.BaseFragment
import com.example.expirydate.databinding.FragmentHomePageBinding
import com.example.expirydate.model.Product
import com.example.expirydate.util.ImageUploadUtil
import com.example.expirydate.viewModel.ProductsViewModel
import com.example.expirydate.worker.NotificationWM
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomePageFragment : BaseFragment(), ProductAdapter.ClickListener {

    private lateinit var binding: FragmentHomePageBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private var productImage: ImageView? = null
    private var uploadedImageForProduct: Product? = null
    private var productList: MutableList<Product>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        initObserve()
        initListener()
        initData()
//        notification()
        return binding.root
    }

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel=NotificationChannel("My notification","My notification",NotificationManager.IMPORTANCE_DEFAULT)
            var manager = this.context!!.getSystemService<NotificationManager>()
            manager!!.createNotificationChannel(channel)
        }

        var builder = NotificationCompat.Builder(requireContext(), "My notification")
        builder.setContentTitle("Title")
        builder.setContentText("Context sssd ....")
        builder.setSmallIcon(R.mipmap.ic_launcher_foreground)
        builder.setAutoCancel(true)

        var manager = NotificationManagerCompat.from(requireContext())
        manager.notify(1, builder.build())
    }

    private fun initData() {
        viewModel.getProducts()
    }

    private fun initListener() {
//        binding.addProduct.setOnClickListener {
//            showAddWordDialog()
//        }
        binding.addProduct.setOnClickListener {
            navigateAddNewProductPage()
            //createWorkManager()
        }
        productAdapter.setClickListener(this)
    }

    private fun navigateAddNewProductPage() {
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_homePageFragment_to_addProductFragment)
    }

    private fun initObserve() {
        viewModel.getProducts().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                productList = it.toMutableList()
                val list = it.reversed()
                productAdapter.submitList(list)
                productAdapter.notifyDataSetChanged()
            }
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
            productList?.remove(product)
            productList?.let { it1 -> productAdapter.submitList(it1) }
            productAdapter.notifyDataSetChanged()
            Toast.makeText(this.context, "${product.productName}  Deleted.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun uploadImage(iv: ImageView, product: Product) {
        productImage = iv
        uploadedImageForProduct = product
        ImageUploadUtil.uploadPhoto(this)
    }

    override fun openProductDetail(product: Product) {
        val bundle = Bundle().apply {
            putParcelable("product", product)
        }
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_homePageFragment_to_addProductFragment, bundle)
    }

    private fun saveImageToDatabase(uri: Uri) {
        uploadedImageForProduct?.let { viewModel.saveImageUrl(uri.toString(), it.id) }
        viewModel.getProducts()
        productImage = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            showToast(getString(R.string.uploaded_image))
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            productImage?.setImageURI(uri)

            // Use Uri object instead of File to avoid storage permissions
            saveImageToDatabase(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            showToast(ImagePicker.getError(data))
        } else {
            showToast(getString(R.string.task_canceled))
        }
    }


    private fun createWorkManager() {
        val workManager = WorkManager.getInstance(requireContext())
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        val work = PeriodicWorkRequestBuilder<NotificationWM>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(work)
    }

}