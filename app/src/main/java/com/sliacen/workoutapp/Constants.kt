package com.sliacen.workoutapp


object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumpingjacks
        )
        exerciseList.add(jumpingJacks)

        val wallSit = ExerciseModel(
            2,
            "Wall Sit",
            R.drawable.ic_wallsit
        )
        exerciseList.add(wallSit)

        val pushUp = ExerciseModel(
            3,
            "Push Up",
            R.drawable.ic_pushup
        )
        exerciseList.add(pushUp)

        val crunch = ExerciseModel(
            4,
            "Abdominal Crunch",
            R.drawable.ic_crunch
        )
        exerciseList.add(crunch)

        val stepUpOnChair = ExerciseModel(
            5,
            "Step Up to Chair",
            R.drawable.ic_chairstep
        )
        exerciseList.add(stepUpOnChair)

        val squat = ExerciseModel(
            6,
            "Squat",
            R.drawable.ic_squat
        )
        exerciseList.add(squat)

        val tricepDip = ExerciseModel(
            7,
            "Tricep Dip",
            R.drawable.ic_tricepdip
        )
        exerciseList.add(tricepDip)

        val plank = ExerciseModel(
            8,
            "Plank",
            R.drawable.ic_plank
        )
        exerciseList.add(plank)

        val highStep = ExerciseModel(
            9,
            "High Knees",
            R.drawable.ic_highstep
        )
        exerciseList.add(highStep)

        val lunges = ExerciseModel(
            10,
            "Lunges",
            R.drawable.ic_lunge
        )
        exerciseList.add(lunges)

        val pushUpRotation = ExerciseModel(
            11,
            "Push Up and Rotation",
            R.drawable.ic_pushup_rotation
        )
        exerciseList.add(pushUpRotation)

        val sidePlank = ExerciseModel(
            12,
            "Side Plank",
            R.drawable.ic_sideplank
        )
        exerciseList.add(sidePlank)

        return exerciseList
    }

}