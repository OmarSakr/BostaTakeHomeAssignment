package com.codevalley.bostatakehomeassignment.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codevalley.bostatakehomeassignment.databinding.ItemImagesBinding
import com.codevalley.bostatakehomeassignment.model.photosModel.PhotosModelItem
import java.util.ArrayList

class ImagesAdapter(private val onClickListenerItem: OnClickListener) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private var dataList: ArrayList<PhotosModelItem> = ArrayList()

    class OnClickListener(val clickListener: (data: ArrayList<PhotosModelItem>, position: Int) -> Unit) {
        fun onClick(data: ArrayList<PhotosModelItem>, position: Int) =
            clickListener(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivAlbumImage.load(dataList[position].url)
        holder.itemView.setOnClickListener {
            onClickListenerItem.onClick(dataList, position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(data: List<PhotosModelItem>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }


    class ViewHolder(var binding: ItemImagesBinding) :
        RecyclerView.ViewHolder(binding.root)

}