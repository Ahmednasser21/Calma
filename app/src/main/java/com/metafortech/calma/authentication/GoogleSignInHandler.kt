package com.metafortech.calma.authentication

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.metafortech.calma.BuildConfig

private const val TAG = "GoogleSignInHandler"

class GoogleSignInHandler {

    suspend fun signInWithGoogle(activity: Activity): GetCredentialResponse {
        val credentialManager = CredentialManager.create(activity)

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
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        val displayName = googleIdTokenCredential.displayName
                        val email = googleIdTokenCredential.id
                        return "$displayName|$email"

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Invalid Google ID token response", e)
                        return ""
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential: ${credential.type}")
                    return ""
                }
            }

            else -> {
                Log.e(TAG, "Unexpected credential type: ${credential.javaClass.name}")
                return ""
            }
        }
    }
}