package com.pcandroiddev.expensemanager.utils.orientationstate

import android.content.Context
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OrientationManager @Inject constructor() {


    fun getOrientationState(context: Context): Flow<OrientationState> {
        return callbackFlow {
            val orientationEventListener = object : OrientationEventListener(context) {
                override fun onOrientationChanged(orientation: Int) {
                    when (orientation) {
                        Surface.ROTATION_0 -> {
                            println("Portrait")
                            Log.d("OrientationManager", "onOrientationChanged: Portrait")
                            trySend(OrientationState.Portrait)
                        }

                        Surface.ROTATION_90 -> {
                            println("Landscape")
                            Log.d("OrientationManager", "onOrientationChanged: Landscape")
                            trySend(OrientationState.Landscape)
                        }

                        Surface.ROTATION_180 -> {
                            println("Reverse Portrait")
                            Log.d("OrientationManager", "onOrientationChanged: Reverse Portrait")
                        }

                        Surface.ROTATION_270 -> {
                            println("Reverse Landscape")
                            Log.d("OrientationManager", "onOrientationChanged: Reverse Landscape")
                            trySend(OrientationState.Landscape)
                        }

                        else -> {
                            println("Unknown Orientation")
                            Log.d("OrientationManager", "onOrientationChanged: Unknown Orientation")

                        }
                    }
                }
            }

            orientationEventListener.enable()

            awaitClose {
                orientationEventListener.disable()
            }
        }.flowOn(Dispatchers.IO)
    }

}