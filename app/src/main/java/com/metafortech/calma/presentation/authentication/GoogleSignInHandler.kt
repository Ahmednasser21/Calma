package com.metafortech.calma.presentation.authentication

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.metafortech.calma.BuildConfig
import javax.inject.Inject

class GoogleSignInHandler @Inject constructor() {

    suspend fun signInWithGoogle(activity: Activity): GetCredentialResponse {
        val credentialManager = CredentialManager.Companion.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.googleclientid)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return credentialManager.getCredential(
            request = request,
            context = activity,
        )
    }

    fun handleSignIn(result: GetCredentialResponse): String {
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.Companion.createFrom(credential.data)

                        val displayName = googleIdTokenCredential.displayName
                        val email = googleIdTokenCredential.id
                        return "$displayName|$email"

                    } catch (_: GoogleIdTokenParsingException) {

                        return "fail"
                    }
                } else {
                    return "fail"
                }
            }

            else -> {
                return "fail"
            }
        }
    }
}