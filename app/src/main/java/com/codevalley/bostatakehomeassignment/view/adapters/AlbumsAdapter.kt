package com.codevalley.bostatakehomeassignment.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codevalley.bostatakehomeassignment.databinding.ItemAlbumsBinding
import com.codevalley.bostatakehomeassignment.model.albumsModel.AlbumsModelItem
import java.util.ArrayList

class AlbumsAdapter(private val onClickListenerItem: OnClickListener) :
    RecyclerView.Adapter<AlbumsAdapter.ViewHolder>() {

    private var dataList: ArrayList<AlbumsModelItem> = ArrayList()



    class OnClickListener(val clickListener: (data: ArrayList<AlbumsModelItem>, position: Int) -> Unit) {
        fun onClick(data: ArrayList<AlbumsModelItem>, position: Int) =
            clickListener(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlbumsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text=dataList[position].title
        holder.itemView.setOnClickListener {
            onClickListenerItem.onClick(dataList, position)
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(data: List<AlbumsModelItem>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(var binding: ItemAlbumsBinding) :
        RecyclerView.ViewHolder(binding.root)


}