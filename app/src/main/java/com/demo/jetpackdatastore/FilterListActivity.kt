package com.demo.jetpackdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.jetpackdatastore.data.DataHelper
import com.demo.jetpackdatastore.proto.AdFilterUtils
import com.demo.jetpackdatastore.proto.model.Ad
import com.demo.jetpackdatastore.proto.model.AdCategory
import com.demo.jetpackdatastore.proto.model.AdFilter
import com.demo.jetpackdatastore.proto.model.AdType
import kotlinx.android.synthetic.main.filter_list.*
import kotlinx.coroutines.launch


class FilterListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var dataStoreUtils: AdFilterUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_list)
        dataStoreUtils = AdFilterUtils(this)
        initList()
        initFilters()
        observeFilters()
    }

    private fun initFilters() {
        val typeAdapter: ArrayAdapter<AdType> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_type.adapter = typeAdapter
        sp_type.onItemSelectedListener = this

        val categoryAdapter: ArrayAdapter<AdCategory> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, DataHelper.categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_category.adapter = categoryAdapter
        sp_category.onItemSelectedListener = this
    }

    private fun initList() {
        rv_filter_list.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rv_filter_list.adapter = FilterListAdapter()
    }

    private fun observeFilters() {
        dataStoreUtils.getAdFilter().asLiveData().observe(this, Observer {
            setFilters(it)
            setListDataWithFilters(it)
        })
    }

    private fun setFilters(filter: AdFilter) {
        sp_type.setSelection(getTypePosition(filter.adType))
        sp_category.setSelection(getCategoryPosition(filter.adCategory))
    }

    private fun getCategoryPosition(adCategory: AdCategory): Int {
        return DataHelper.categories.indexOfFirst {
            it.ordinal == adCategory.ordinal
        }
    }

    private fun getTypePosition(adType: AdType): Int {
        return DataHelper.types.indexOfFirst {
            it.ordinal == adType.ordinal
        }
    }

    private fun setListDataWithFilters(filter: AdFilter) {
        (rv_filter_list.adapter as FilterListAdapter).setAds(DataHelper.getFilteredList(filter))
    }

    class FilterListAdapter : RecyclerView.Adapter<FilterListAdapter.FilterListItemViewHolder>() {
        private var items : MutableList<Ad>? = null

        fun setAds(list: MutableList<Ad>) {
            this.items = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterListItemViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_list_item, parent, false)
            return FilterListItemViewHolder(view)
        }

        class FilterListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(ad: Ad) {
                title.text = ad.title
                type.text = ad.type.name
                category.text = ad.category.name
            }

            private val title: TextView = view.findViewById(R.id.tv_ad_title)
            private val type: TextView = view.findViewById(R.id.tv_ad_type)
            private val category: TextView = view.findViewById(R.id.tv_ad_category)
        }

        override fun getItemCount(): Int {
            return items?.size?:0
        }

        override fun onBindViewHolder(holder: FilterListItemViewHolder, position: Int) {
            holder.bind(items!!.get(position))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedId = parent!!.id
        if (R.id.sp_category == selectedId) {
            lifecycleScope.launch {
                dataStoreUtils.updateAdCategory(DataHelper.categories[position])
            }
        } else if (R.id.sp_type == selectedId) {
            lifecycleScope.launch {
                dataStoreUtils.updateAdType(DataHelper.types[position])
            }
        }
    }
}