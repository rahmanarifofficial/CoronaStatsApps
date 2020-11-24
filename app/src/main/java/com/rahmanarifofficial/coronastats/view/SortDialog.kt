package com.rahmanarifofficial.coronastats.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rahmanarifofficial.coronastats.R
import kotlinx.android.synthetic.main.dialog_sort.*

class SortDialog : RoundedBottomSheetDialogFragment() {

    lateinit var callback: (selectedOption: Int) -> Unit

    companion object {
        fun show(fm: FragmentManager, callback: (selectedOption: Int) -> Unit) =
            SortDialog().apply {
                this.callback = callback
            }.show(fm, "DIALOG SORT")
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
        return inflater.inflate(R.layout.dialog_sort, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        eventUI()
    }

    private fun eventUI() {
        sortAscProvince?.setOnClickListener {
            callback.invoke(1)
            dismiss()
        }
        sortDescProvince?.setOnClickListener {
            callback.invoke(2)
            dismiss()
        }
        sortAscPos?.setOnClickListener {
            callback.invoke(3)
            dismiss()
        }
        sortDescPos?.setOnClickListener {
            callback.invoke(4)
            dismiss()
        }
    }
}