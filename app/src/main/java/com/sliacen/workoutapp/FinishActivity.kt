package com.sliacen.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.sliacen.workoutapp.databinding.ActivityFinishBinding
import com.sliacen.workoutapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {

    private var binding : ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.finishButton?.setOnClickListener {
            finish()
        }

        val dao = (application as WorkoutApp).db.historyDao()

        addDateToDatabase(dao)

    }

    /** Un-assign the binding to avoid a memory leak */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun addDateToDatabase(historyDao: HistoryDao) {

        val c = Calendar.getInstance()
        val dateTime = c.time
        Log.e("Date: ", "" + dateTime)

        val sdf = SimpleDateFormat("yyyy MMM dd HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)

        lifecycleScope.launch{
            historyDao.insert(HistoryEntity(date))
            Log.e(
                "Date: ",
                "Added...."
            )
        }

    }

}