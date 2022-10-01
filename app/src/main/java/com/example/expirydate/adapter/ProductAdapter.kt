package com.example.expirydate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.expirydate.R
import com.example.expirydate.databinding.ItemProductBinding
import com.example.expirydate.model.Product

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.WordViewHolder>() {

    private var productList: MutableList<Product> = mutableListOf()

    lateinit var clickListener: ClickListener

    inner class WordViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.product = item
            Glide.with(binding.root).load(item.imageUrl).placeholder(R.drawable.ic_baseline_add_24)
                .into(binding.productImage)
            binding.deleteIv.setOnClickListener {
                clickListener.deleteProduct(item)
                productList.remove(item)
                notifyItemRemoved(adapterPosition)
            }

            binding.uploadImageCv.setOnClickListener {
                clickListener.uploadImage(binding.productImage, item)
            }

            binding.productCv.setOnClickListener {
                clickListener.openProductDetail(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        if (productList.isNotEmpty()){
            val word = productList[position]
            holder.bind(word)
        }
    }


    override fun getItemCount(): Int {
        return productList.size
    }

    fun submitList(list: List<Product>) {
        productList = list as MutableList<Product>
        notifyDataSetChanged()
    }

    @JvmName("setClickListener1")
    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun deleteProduct(product: Product)
        fun uploadImage(iv: ImageView, product: Product)
        fun openProductDetail(product: Product)
    }

}