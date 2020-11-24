package com.rahmanarifofficial.coronastats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmanarifofficial.coronastats.R
import com.rahmanarifofficial.coronastats.model.Attributes
import com.rahmanarifofficial.coronastats.model.Feature
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_case.view.*

class CaseAdapter(
    private val context: Context,
    private val itemList: List<Attributes>,
    private val onItemClick: ((Int, Attributes) -> Unit)? = null
) : RecyclerView.Adapter<CaseAdapter.DefaultViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CaseAdapter.DefaultViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_list_case, parent, false)
        return DefaultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CaseAdapter.DefaultViewHolder, position: Int) {
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
            itemView.tvProvince.text = item.Provinsi
            itemView.tvPositiveCase.text = item.Kasus_Posi.toString()
            itemView.tvHealthCase.text = item.Kasus_Semb.toString()
            itemView.tvDeathCase.text = item.Kasus_Meni.toString()
            onItemClick?.let {
                containerView.setOnClickListener {
                    onItemClick(pos, item)
                }
            }
        }
    }
}