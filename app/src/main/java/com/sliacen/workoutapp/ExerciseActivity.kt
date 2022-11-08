package com.sliacen.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sliacen.workoutapp.databinding.ActivityExerciseBinding
import com.sliacen.workoutapp.databinding.DialogCustomBackConfirmBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private val maxRestTime = 10   // total seconds of rest time

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private val maxExerciseTime = 30   // total seconds of exercise time

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var soundSetting = true
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

//        if (supportActionBar != null) {
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        setUpRestView()
        setUpExerciseStatusRecyclerView()

    }

    /**
     * Asks user to confirm if they want to exit the exercise
     */
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this@ExerciseActivity)
        val dialogBinding = DialogCustomBackConfirmBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)   // cannot click outside dialog box to cancel
        dialogBinding.buttonYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.buttonNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setUpExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(
            this@ExerciseActivity, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter

    }

    /**
     * Rest View is the View between exercises
     */
    private fun setUpRestView() {
        if (soundSetting && currentExercisePosition >= 0) {
            try {
                val soundURI = Uri.parse(
                    "android.resource://com.sliacen.workoutapp/"
                            + R.raw.level_complete
                )
                player = MediaPlayer.create(applicationContext, soundURI)
                player?.isLooping = false
                player?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE

        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setUpExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE

        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (soundSetting) speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())

        setExerciseProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.restProgressBar?.progress = restProgress

        restTimer = object : CountDownTimer((maxRestTime*1000).toLong(), 1000) {
            override fun onTick(p0: Long) {
                restProgress++
                binding?.restProgressBar?.progress = maxRestTime - restProgress
                binding?.tvRestTimer?.text = (maxRestTime - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                /* Check for the current item */
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setUpExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.exerciseProgressBar?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(
            (maxExerciseTime*1000).toLong(), 1000) {
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.exerciseProgressBar?.progress = maxExerciseTime - exerciseProgress
                binding?.tvExerciseTimer?.text = (maxExerciseTime - exerciseProgress).toString()
            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()

                if (currentExercisePosition  < exerciseList?.size!! - 1) {
                    setUpRestView()
                } else {
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if(exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player != null) {
            player!!.stop()
        }

        binding = null
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}