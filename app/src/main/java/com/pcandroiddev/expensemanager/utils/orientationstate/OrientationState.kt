package com.pcandroiddev.expensemanager.utils.orientationstate

sealed interface OrientationState {
    object Portrait : OrientationState
    object Landscape : OrientationState
}