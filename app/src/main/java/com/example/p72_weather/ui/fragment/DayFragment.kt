package com.example.p72_weather.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.p72_weather.R
import com.example.p72_weather.adapter.WeatherAdapter
import com.example.p72_weather.databinding.FragmentDayBinding
import com.example.p72_weather.model.MainResponse
import com.example.p72_weather.ui.MainActivity
import com.example.p72_weather.util.Resource
import com.example.p72_weather.viewmodel.MainViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class DayFragment : Fragment() {
    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as MainActivity).viewModel

        checkLocationPermission()
        initialise()
        initialiseButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLocationPermission() {
        if (checkPermission()) {
            getLocation()
        } else {
            val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                val text = if (it) "Permission Granted" else "Permission Canceled"
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
                if (it) getLocation()
            }

            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val isGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

        return isGranted
    }

    private fun getLocation() {
        loading(true)
        val cancellationToken = CancellationTokenSource()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token,
        ).addOnCompleteListener {
            val coordinates = "${it.result.latitude},${it.result.longitude}"
            viewModel.setCurrentDay(0)
            viewModel.getData(coordinates)
        }
    }

    private fun loading(enabled: Boolean) = with(binding) {
        if (enabled) {
            linearLayout.visibility = View.INVISIBLE
            recyclerView.visibility = View.INVISIBLE
            progressBarFirst.visibility = View.VISIBLE
            progressBarSecond.visibility = View.VISIBLE
        } else {
            linearLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            progressBarFirst.visibility = View.INVISIBLE
            progressBarSecond.visibility = View.INVISIBLE
        }
    }

    private fun initialise() = with(binding) {
        adapter = WeatherAdapter(viewModel)
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
                    updateUI(it.response)
                }
            }
        }
    }

    private fun updateUI(response: MainResponse) = with(binding) {
        val list = viewModel.getAdapterListForDays(response)
        adapter.submitList(list)

        val temp = "${response.current.tempC} °C"
        val icon = "https:${response.current.condition.icon}"
        val wind = "Wind: ${response.current.windKph} km/h"
        val humidity = "Humidity: ${response.current.humidity}%"

        tvLastUpdated.text = response.current.lastUpdated
        tvCountry.text = response.location.country
        tvRegion.text = response.location.region
        tvCityName.text = response.location.name
        tvCurrentTemp.text = temp
        Glide.with(root).load(icon).into(ivConditionIcon)
        tvConditionText.text = response.current.condition.text
        tvWind.text = wind
        tvHumidity.text = humidity
    }

    private fun initialiseButtons() = with(binding) {
        btnSync.setOnClickListener {
            viewModel.setCurrentDay(0)
            getLocation()
        }

        btnSearch.setOnClickListener {
            startSearchAlertDialog()
        }
    }

    private fun startSearchAlertDialog() {
        val editText = EditText(requireContext())
        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(editText)
            .setTitle("Enter the name of the settlement")
            .setNegativeButton("CANCEL", null)
            .setPositiveButton("SUBMIT") { dialog, _ ->
                viewModel.setCurrentDay(0)
                viewModel.getData(editText.text.toString().trim())
                dialog.dismiss()
            }.create()
        dialog.show()
    }

}
























