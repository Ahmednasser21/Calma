package com.metafortech.calma.presentation.authentication.google

import android.app.Activity

interface GoogleSignInUseCase {
    suspend fun signInWithGoogle(activity: Activity): Pair<String, String>
}