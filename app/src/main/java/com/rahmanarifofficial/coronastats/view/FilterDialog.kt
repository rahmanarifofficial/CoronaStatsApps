package com.rahmanarifofficial.coronastats.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rahmanarifofficial.coronastats.R
import com.rahmanarifofficial.coronastats.adapter.ProvinceAdapter
import com.rahmanarifofficial.coronastats.datasource.Status
import com.rahmanarifofficial.coronastats.model.Attributes
import com.rahmanarifofficial.coronastats.utils.ViewModelFactory
import com.rahmanarifofficial.coronastats.viewmodel.DataViewModel
import kotlinx.android.synthetic.main.dialog_filter.*

class FilterDialog : RoundedBottomSheetDialogFragment() {
    private lateinit var dataViewModel: DataViewModel
    private lateinit var callback: (selectedList: ArrayList<Attributes>) -> Unit
    private var itemList = ArrayList<Attributes>()
    private var itemListSelected = ArrayList<Attributes>()
    private lateinit var adapter: ProvinceAdapter

    companion object {
        fun show(
            fm: FragmentManager,
            callback: (selectedList: ArrayList<Attributes>) -> Unit
        ) =
            FilterDialog().apply {
                this.callback = callback
            }.show(fm, "DIALOG FILTER")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(R.id.design_bottom_sheet) as View
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }
        return inflater.inflate(R.layout.dialog_filter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
    }

    private fun initObject() {
        val factory = ViewModelFactory.getInstance(context!!)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]
        adapter = ProvinceAdapter(itemList) { pos: Int, item: Attributes ->
            item.isSelected = !item.isSelected
            if (item.isSelected) {
                itemListSelected.add(item)
                dataViewModel.updateSelected(item.FID ?: 0, true)
                itemList[pos].isSelected = true
            } else {
                itemListSelected.remove(item)
                dataViewModel.updateSelected(item.FID ?: 0, false)
                itemList[pos].isSelected = false
            }
            countCheckedSelected()
            adapter.notifyItemChanged(pos)
        }
    }

    private fun initUI() {
        dataViewModel.getData(0, null).observe(viewLifecycleOwner, Observer {
            it?.let { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        res.data?.let { data ->
                            vProgress?.visibility = View.GONE
                            rvProvince?.visibility = View.VISIBLE
                            btnApplyFilter?.visibility = View.VISIBLE
                            itemList.clear()
                            data.filter { d -> !(d.Provinsi.equals("Indonesia", true)) }.forEach {
                                itemList.add(it)
                                if (it.isSelected) {
                                    itemListSelected.add(it)
                                }
                                countCheckedSelected()
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        vProgress?.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        vProgress?.visibility = View.VISIBLE
                    }
                }
            }

        })
        rvProvince?.adapter = adapter
        rvProvince?.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
    }

    private fun eventUI() {
        btnApplyFilter?.setOnClickListener {
            callback.invoke(itemList)
            dismiss()
        }
        btnResetFilter?.setOnClickListener {
            for (item in itemListSelected) {
                dataViewModel.updateSelected(item.FID ?: 0, false)
            }
            Handler().postDelayed({
                btnResetFilter?.visibility = View.GONE
            }, 300)
        }
    }

    private fun countCheckedSelected() {
        if (itemListSelected.size > 0) {
            btnResetFilter?.visibility = View.VISIBLE
        }
    }
}