package com.metafortech.calma.domain.google

import android.app.Activity
import com.metafortech.calma.presentation.authentication.GoogleSignInHandler
import javax.inject.Inject

class GoogleSignInUseCaseImpl @Inject constructor(
    private val googleSignInHandler: GoogleSignInHandler
) : GoogleSignInUseCase {
    override suspend fun signInWithGoogle(activity: Activity): Pair<String, String> {
        val result = googleSignInHandler.signInWithGoogle(activity)
        val data = googleSignInHandler.handleSignIn(result).split("|")
        return Pair(data[0], data[1]) // name, email
    }
}