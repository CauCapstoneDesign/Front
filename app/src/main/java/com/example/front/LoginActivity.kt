package com.example.front


import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*




class LoginActivity : AppCompatActivity() {
    lateinit var Google_Login: SignInButton
    lateinit var Google_Logout: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var acct:GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        Google_Login = findViewById(R.id.Google_Login)
        Google_Login.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        Google_Logout=Google_logout
        val listener = object : View.OnClickListener{
            override fun onClick(p0: View?) {
                signOut()
                mGoogleSignInClient.signOut()
            }
        }
        Google_Logout.setOnClickListener(listener)

        //Get the Spot Info

    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
            else {
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("GoogleActivity", "firebaseAuthWithGoogle:" + acct.id!!)
        this.acct=acct
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "인증 실패", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show()

                        val nextIntent = Intent(this, MapsActivity::class.java)
                        startActivity(nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        finish()
//                        acct = GoogleSignIn.getLastSignedInAccount()
//                        if (acct != null) {
//                            val personName = acct.displayName
//                            val personGivenName = acct.givenName
//                            val personFamilyName = acct.familyName
//                            val personEmail = acct.email
//                            val personId = acct.id
//                            val personPhoto = acct.photoUrl
//                        }
                    }
                }
    }

    companion object {
        private val RC_SIGN_IN = 100
    }
}