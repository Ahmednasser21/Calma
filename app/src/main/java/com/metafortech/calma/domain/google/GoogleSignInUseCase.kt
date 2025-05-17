package com.metafortech.calma.domain.google

import android.app.Activity

interface GoogleSignInUseCase {
    suspend fun signInWithGoogle(activity: Activity): Pair<String, String>
}