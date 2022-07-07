package com.example.expirydate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expirydate.databinding.ItemProductBinding
import com.example.expirydate.model.Product

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.WordViewHolder>() {

    private var productList: List<Product> = emptyList()

    inner class WordViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.product = item
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
        val word = productList[position]
        holder.bind(word)
    }


    override fun getItemCount(): Int {
        return productList.size
    }

    fun submitList(list: List<Product>) {
        productList = list
        notifyDataSetChanged()
    }

}