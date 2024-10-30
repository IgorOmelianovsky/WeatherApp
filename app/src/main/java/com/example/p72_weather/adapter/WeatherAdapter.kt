package com.example.p72_weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.p72_weather.R
import com.example.p72_weather.databinding.AdapterItemBinding
import com.example.p72_weather.model.AdapterModel
import com.example.p72_weather.viewmodel.MainViewModel

class WeatherAdapter(private val viewModel: MainViewModel?) :
    ListAdapter<AdapterModel, WeatherAdapter.ViewHolder>(Comparator()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = AdapterItemBinding.bind(itemView)

        fun bindData(adapterModel: AdapterModel) = with(binding) {
            tvTemp.text = adapterModel.temp
            tvTime.text = adapterModel.time
            tvWind.text = adapterModel.wind
            tvConditionText.text = adapterModel.conditionText
            Glide.with(root).load(adapterModel.conditionIcon).into(ivConditionIcon)

            if (viewModel != null) {
                itemView.setOnClickListener {
                    val tabLayout = viewModel.getTabLayout()
                    viewModel.setCurrentDay(adapterPosition)
                    tabLayout.selectTab(tabLayout.getTabAt(1))
                }
            }
        }

    }

    class Comparator : DiffUtil.ItemCallback<AdapterModel>() {

        override fun areItemsTheSame(oldItem: AdapterModel, newItem: AdapterModel): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: AdapterModel, newItem: AdapterModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindData(item)
    }

}