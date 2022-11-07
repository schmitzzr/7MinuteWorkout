package com.sliacen.workoutapp

class ExerciseModel (
    private var id: Int,
    private var name: String,
    private var image: Int,
    private var isCompleted: Boolean = false,
    private var isSelected: Boolean = false
) {

    /** Getters */
    fun getId() : Int {
        return id
    }
    fun getName() : String {
        return name
    }
    fun getImage() : Int {
        return image
    }
    fun getIsCompleted() : Boolean {
        return isCompleted
    }
    fun getIsSelected() : Boolean {
        return isSelected
    }

    /** Setters */
    fun setId(theId: Int) {
        id = theId
    }
    fun setName(theName: String) {
        name = theName
    }
    fun setImage(theImage: Int) {
        image = theImage
    }
    fun setIsCompleted(isComp: Boolean) {
        isCompleted = isComp
    }
    fun setIsSelected(isSel: Boolean) {
        isSelected = isSel
    }

}