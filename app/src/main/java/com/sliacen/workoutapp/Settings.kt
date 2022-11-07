package com.sliacen.workoutapp

object Settings {

    var audioSetting: Boolean = true    // Whether or not audio is played
    var exerciseLength: Int = 30        // Duration of the exercises
    var restLength: Int = 10            // Duration of the rests

    /** Resets to default settings */
    fun resetToDefault() {
        audioSetting = true
        exerciseLength = 30
        restLength = 30
    }

}