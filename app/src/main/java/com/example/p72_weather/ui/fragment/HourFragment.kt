package com.example.p72_weather.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.p72_weather.R
import com.example.p72_weather.adapter.WeatherAdapter
import com.example.p72_weather.databinding.FragmentHourBinding
import com.example.p72_weather.model.MainResponse
import com.example.p72_weather.ui.MainActivity
import com.example.p72_weather.util.Resource
import com.example.p72_weather.viewmodel.MainViewModel

class HourFragment : Fragment() {
    private var _binding: FragmentHourBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: WeatherAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHourBinding.inflate(inflater)
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
        adapter = WeatherAdapter(null)
        recyclerView.adapter = adapter

        observe()
    }

    private fun observe() {
        viewModel.currentResponse.observe(viewLifecycleOwner) {
            when (it) {
                Resource.Loading -> loading(true)

                is Resource.Error -> {
                    loading(false)
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    loading(false)
                    observeCurrentDay(it.response)
                }
            }
        }
    }

    private fun observeCurrentDay(response: MainResponse) {
        viewModel.currentDay.observe(viewLifecycleOwner) {
            val list = viewModel.getAdapterListForHours(response, it)
            adapter.submitList(list)
        }
    }

    private fun loading(enabled: Boolean) = with(binding) {
        if (enabled) {
            recyclerView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

}


























