package com.example.metricconverter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerMetric: Spinner
    private lateinit var spinnerFromUnit: Spinner
    private lateinit var spinnerToUnit: Spinner
    private lateinit var etValue: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen UI
        spinnerMetric = findViewById(R.id.spinnerMetric)
        spinnerFromUnit = findViewById(R.id.spinnerFromUnit)
        spinnerToUnit = findViewById(R.id.spinnerToUnit)
        etValue = findViewById(R.id.etValue)
        tvResult = findViewById(R.id.tvResult)

        // Atur spinner metric dan tambahkan listener untuk perhitungan otomatis
        setupMetricSpinner()

        // Tambahkan listener untuk mendeteksi perubahan teks pada etValue dan menghitung hasil secara otomatis
        etValue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateResult()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun setupMetricSpinner() {
        val metrics = listOf("Panjang", "Berat", "Suhu")
        val metricAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, metrics)
        spinnerMetric.adapter = metricAdapter

        spinnerMetric.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMetric = metrics[position]
                updateUnitsSpinner(selectedMetric)
                calculateResult()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak perlu melakukan apa-apa jika tidak ada item yang dipilih
            }
        }
    }

    private fun updateUnitsSpinner(metric: String) {
        val units = when (metric) {
            "Panjang" -> listOf("Meter", "Kilometer", "Centimeter")
            "Berat" -> listOf("Gram", "Kilogram", "Miligram")
            "Suhu" -> listOf("Celsius", "Fahrenheit")
            else -> emptyList()
        }

        val unitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        spinnerFromUnit.adapter = unitAdapter
        spinnerToUnit.adapter = unitAdapter

        spinnerFromUnit.isEnabled = true
        spinnerToUnit.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    private fun calculateResult() {
        val inputText = etValue.text.toString()
        if (inputText.isEmpty()) {
            tvResult.text = "Hasil: "
            return
        }
        val value = inputText.toDoubleOrNull()
        if (value == null) {
            tvResult.text = "Masukkan nilai numerik"
            return
        }

        val fromUnit = spinnerFromUnit.selectedItem.toString()
        val toUnit = spinnerToUnit.selectedItem.toString()
        val result = convert(value, fromUnit, toUnit)
        tvResult.text = "Hasil: $result"
    }

    private fun convert(value: Double, fromUnit: String, toUnit: String): Double {
        return when (fromUnit to toUnit) {
            "Meter" to "Kilometer" -> value / 1000
            "Kilometer" to "Meter" -> value * 1000
            "Celsius" to "Fahrenheit" -> (value * 9 / 5) + 32
            "Fahrenheit" to "Celsius" -> (value - 32) * 5 / 9
            else -> value
        }
    }


}
