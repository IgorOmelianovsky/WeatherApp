package com.example.p72_weather.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.p72_weather.R
import com.example.p72_weather.adapter.ViewPagerAdapter
import com.example.p72_weather.databinding.FragmentMainBinding
import com.example.p72_weather.ui.MainActivity
import com.example.p72_weather.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initialise() = with(binding) {
        viewModel = (requireActivity() as MainActivity).viewModel
        val fragmentList = listOf(DayFragment(), HourFragment())
        viewPager.adapter = ViewPagerAdapter(requireActivity(), fragmentList)
        viewModel.setTabLayout(tbTabLayout)
        TabLayoutMediator(tbTabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Days"
                1 -> tab.text = "Hours"
            }
        }.attach()
    }

}























