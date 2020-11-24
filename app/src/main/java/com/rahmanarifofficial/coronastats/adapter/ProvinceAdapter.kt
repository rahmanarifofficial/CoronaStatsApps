package com.rahmanarifofficial.coronastats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmanarifofficial.coronastats.R
import com.rahmanarifofficial.coronastats.model.Attributes
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_province.view.*

class ProvinceAdapter(
    private val itemList: List<Attributes>,
    private val onItemClick: ((Int, Attributes) -> Unit)? = null
) : RecyclerView.Adapter<ProvinceAdapter.DefaultViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProvinceAdapter.DefaultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_province, parent, false)
        return DefaultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ProvinceAdapter.DefaultViewHolder, position: Int) {
        holder.bindItem(position, itemList[position], onItemClick)
    }

    inner class DefaultViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItem(
            pos: Int,
            item: Attributes,
            onItemClick: ((Int, Attributes) -> Unit)?
        ) {
            itemView.tvProvinceFilter.text = item.Provinsi
            itemView.cbProvince.isChecked = item.isSelected
            onItemClick?.let {
                containerView.setOnClickListener {
                    onItemClick(pos, item)
                }
            }

        }
    }
}