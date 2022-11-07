package com.sliacen.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sliacen.workoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val IMPERIAL_UNITS_VIEW = "IMPERIAL_UNIT_VIEW"
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        /** Adding functionality to the Imperial-Metric Units Radio Buttons */
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleImperialUnitsView()
            }
        }

        binding?.buttonCalculate?.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) calculateMetricUnits()
            else calculateImperialUnits()

        }
    }

    private fun calculateImperialUnits() {
        if(validateImperialUnits()) {
            val heightFeet : Float =
                binding?.etImperialUnitHeightFeet?.text.toString().toFloat()
            val heightInches : Float =
                binding?.etImperialUnitHeightInch?.text.toString().toFloat()
            val weightValue : Float =
                binding?.etImperialUnitWeight?.text.toString().toFloat()

            val totalHeight: Float = heightFeet * 12 + heightInches
            val bmi = weightValue / (totalHeight * totalHeight) * 703

            displayBMIResult(bmi)
        } else {
            Toast.makeText(this@BMIActivity,
                "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateMetricUnits() {
        if(validateMetricUnits()) {
            val heightValue : Float =
                binding?.etMetricUnitHeight?.text.toString().toFloat() / 100
            val weightValue : Float =
                binding?.etMetricUnitWeight?.text.toString().toFloat()
            val bmi = weightValue / (heightValue * heightValue)

            displayBMIResult(bmi)
        } else {
            Toast.makeText(this@BMIActivity,
                "Please enter valid values.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilImperialUnitHeightFeet?.visibility = View.GONE
        binding?.tilImperialUnitHeightInch?.visibility = View.GONE
        binding?.tilImperialUnitWeight?.visibility = View.GONE

        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared
        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared

        binding?.linearLayoutDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleImperialUnitsView() {
        currentVisibleView = IMPERIAL_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilImperialUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilImperialUnitHeightInch?.visibility = View.VISIBLE
        binding?.tilImperialUnitWeight?.visibility = View.VISIBLE

        binding?.etImperialUnitHeightFeet?.text!!.clear() // height value is cleared
        binding?.etImperialUnitHeightInch?.text!!.clear() // height value is cleared
        binding?.etImperialUnitWeight?.text!!.clear() // weight value is cleared

        binding?.linearLayoutDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel : String
        val bmiDesc: String

        if(bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDesc = "Oops!  You really need to take better care of yourself!  Eat more!"
        } else if(bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDesc = "Oops!  You really need to take better care of yourself!  Eat more!"
        } else if(bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDesc = "Oops!  You really need to take better care of yourself!  Eat more!"
        } else if(bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDesc = "Great!  You are in good shape!"
        } else if(bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDesc = "Oops!  You really need to take better care of yourself!  Workout more!"
        } else if(bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class I (Moderately Obese)"
            bmiDesc = "Oops!  You really need to take better care of yourself!  Workout more!"
        } else if(bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class II (Severely Obese)"
            bmiDesc = "You are in a very dangerous condition!  Act now!"
        } else {
            bmiLabel = "Obese Class III (Very Severely Obese)"
            bmiDesc = "You are in a very dangerous condition!  Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDesc?.text = bmiDesc
        binding?.linearLayoutDisplayBMIResult?.visibility = View.VISIBLE
    }

    private fun validateMetricUnits() : Boolean {
        var isValid = true

        if(binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun validateImperialUnits() : Boolean {
        var isValid = true

        when {
            binding?.etImperialUnitWeight?.text.toString().isEmpty() -> isValid = false
            binding?.etImperialUnitHeightInch?.text.toString().isEmpty() -> isValid = false
            binding?.etImperialUnitHeightFeet?.text.toString().isEmpty() -> isValid = false
        }

        return isValid
    }
}