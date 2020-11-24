package com.rahmanarifofficial.coronastats.view

import android.R.attr.*
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rahmanarifofficial.coronastats.R
import com.rahmanarifofficial.coronastats.adapter.CaseAdapter
import com.rahmanarifofficial.coronastats.datasource.Status
import com.rahmanarifofficial.coronastats.model.Attributes
import com.rahmanarifofficial.coronastats.utils.Utils
import com.rahmanarifofficial.coronastats.utils.ViewModelFactory
import com.rahmanarifofficial.coronastats.viewmodel.DataViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var dataViewModel: DataViewModel
    private lateinit var adapter: CaseAdapter
    private var itemList = ArrayList<Attributes>()
    private var itemListSelected = ArrayList<Attributes>()
    private var mLastClickTime: Long = 0

    //==== SEARCHING VARIABLE =====//
    private var keyword: String = ""
    private var q: String = ""
    private var lastTextEdit = 0L
    private val idleMin = 1000L
    private var alreadyQueried = false
    private var lastKeywords = ""
    //==== SEARCHING VARIABLE =====//

    //==== SORTING VARIABLE =====//
    private var sortValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.installGooglePlayServicesProvider(this)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initObject()
        initUI()
        eventUI()
        loadData()
    }

    private fun initObject() {
        val factory = ViewModelFactory.getInstance(this)
        dataViewModel = ViewModelProvider(this, factory)[DataViewModel::class.java]
        adapter = CaseAdapter(this, itemList) { pos: Int, item: Attributes ->
        }
    }

    private fun initUI() {
        //SET STATUS BAR
        window.apply {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                statusBarColor = Color.TRANSPARENT
            }
        }
        rvBase?.adapter = adapter
        rvBase?.layoutManager = LinearLayoutManager(this)
    }

    private fun eventUI() {
        searchEdt?.addTextChangedListener {
            it?.let { d ->
                q = d.toString()
                if (q.isNotEmpty()) {
                    lastTextEdit = System.currentTimeMillis()
                    val inputFinishChecker = Runnable {
                        if (System.currentTimeMillis() > lastTextEdit + idleMin - 500) {
                            if (searchEdt != null) {
                                keyword = searchEdt.text.toString().trim()

                                if (keyword != lastKeywords && keyword.isNotEmpty()) {
                                    if (!alreadyQueried) {
                                        alreadyQueried = true
                                        lastKeywords = keyword
                                        Log.e("DATA KEYWORD", keyword)
                                        dataViewModel.keyword.value = keyword
                                    } else {
                                        alreadyQueried = false
                                    }
                                } else {
                                    alreadyQueried = false
                                }
                            } else {
                                alreadyQueried = false
                            }
                        }
                    }

                    Handler().postDelayed(inputFinishChecker, idleMin)
                    searchDrawable?.setImageResource(R.drawable.ic_close)
                } else {
                    dataViewModel.keyword.value = ""
                    searchDrawable?.setImageResource(R.drawable.ic_search)
                    alreadyQueried = false
                }
            }
        }

        searchDrawableLyt?.setOnClickListener {
            if (q.isNotEmpty()) {
                searchEdt?.setText("")
                searchEdt?.clearFocus()
                hideKeyboard()
            }
        }

        btnError?.setOnClickListener {
            refresh()
        }
        btnRefresh?.setOnClickListener {
            refresh()
        }

        btnSort?.setOnClickListener {
            SortDialog.show(supportFragmentManager) {
                sortValue = it
                loadData()
            }
        }

        btnFilter?.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            FilterDialog.show(supportFragmentManager) {
                Log.e("DATA SELECTED", Gson().toJson(it))
                itemListSelected.clear()
                it.filter { d->d.isSelected }.forEach { data->
                    itemListSelected.add(data)
                }
                loadData()
            }
        }
        btnLogout?.setOnClickListener {
            dataViewModel.logout {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun refresh() {
        for (item in itemListSelected){
            dataViewModel.updateSelected(item.FID?:0, false)
        }
        itemList.clear()
        itemListSelected.clear()
        sortValue = 0
        dataViewModel.keyword.value = ""
        searchEdt?.setText("")
        adapter.notifyDataSetChanged()
        loadData()
    }

    private fun loadData() {
        dataViewModel.getData(sortValue, itemListSelected).observe(this, Observer {
            it?.let { res ->
                when (res.status) {
                    Status.SUCCESS -> {
                        progressBar?.visibility = View.GONE
                        errorLyt?.visibility = View.GONE
                        searchLyt?.visibility = View.VISIBLE
                        btnSort?.visibility = View.VISIBLE
                        btnFilter?.visibility = View.VISIBLE
                        rvBase?.visibility = View.VISIBLE
                        res.data?.let { data ->
                            itemList.clear()
                            data.filter { d -> !(d.Provinsi.equals("Indonesia", true)) }.forEach {
                                itemList.add(it)
                            }
                            adapter.notifyDataSetChanged()
                            Log.e("DATA FIELD", Gson().toJson(itemList))
                        }
                    }
                    Status.ERROR -> {
                        progressBar?.visibility = View.GONE
                        rvBase?.visibility = View.GONE
                        searchLyt?.visibility = View.GONE
                        btnSort?.visibility = View.GONE
                        btnFilter?.visibility = View.GONE
                        errorLyt?.visibility = View.VISIBLE
                        tvError?.text = res.message
                    }
                    Status.LOADING -> {
                        progressBar?.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    private fun hideKeyboard() {
        try {
            val windowToken = currentFocus?.windowToken
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dataViewModel.isInitialized) {
            for (item in itemList) {
                dataViewModel.updateSelected(item.FID ?: 0, false)
            }
        }
    }
}