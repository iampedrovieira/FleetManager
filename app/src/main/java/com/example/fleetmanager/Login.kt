package com.example.fleetmanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputLogin
import com.example.fleetmanager.api.ServiceBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    // Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // Inputs
    private lateinit var emailTV: TextView
    private lateinit var passwordTV: TextView
    private lateinit var loginProgressView: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailTV = findViewById(R.id.emailText)
        passwordTV = findViewById(R.id.passwdText)
        loginProgressView = findViewById(R.id.loginProgress)

        auth = Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        googleLoginbtn.setOnClickListener {
            signIn()
        }

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        val isLogged = sharedPref.getBoolean(getString(R.string.logged), false)
        val isEmployee = sharedPref.getBoolean(getString(R.string.isEmployee), false)

        if (isLogged) {
            if (isEmployee) {
                val i = Intent(this@Login, MainActivityEmployee::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this@Login, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    fun login(view: View) {
        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()

        loginProgressView.visibility = View.VISIBLE

        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("****Login", "signInWithEmail:success")
                        val user = auth.currentUser
                        getUser(user!!.uid)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("****Login", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this@Login,
                            getString(R.string.loginFailed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        loginProgressView.visibility = View.GONE
                    }
                }
        } else {
            Toast.makeText(this, getString(R.string.emptyFields), Toast.LENGTH_SHORT).show()
            loginProgressView.visibility = View.GONE
        }
        closeKeyBoard()
    }

    private fun signIn() {
        loginProgressView.visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception

            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("****Login", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("****Login", "Google sign in failed", e)
                }
            } else {
                Log.w("****Login", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("****Login", "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("****Login", user!!.uid)
                    getUser(user!!.uid)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("****Login", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun getUser(uid: String) {
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.login(uid)

        call.enqueue(object : Callback<OutputLogin> {
            override fun onResponse(
                call: Call<OutputLogin>,
                response: Response<OutputLogin>
            ) {
                Log.d("****Login", "entrou no call")
                Log.d("****Login", response.toString())
                val c: OutputLogin = response.body()!!

                if (!c.status) {
                    Log.d("****Login", "Entroi no status false")
                    Toast.makeText(
                        this@Login,
                        getString(R.string.loginFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                    loginProgressView.visibility = View.GONE
                }

                if (c.status) {
                    Log.d("****Login", "Entrou no status true")
                    if (c.userid == "") {
                        // se nao tem userid e employer
                        // Admin faz login: guardar o uid no company e meter isEmployee = false
                        val sharedPref: SharedPreferences =
                            getSharedPreferences(
                                getString(R.string.preference_file_key),
                                Context.MODE_PRIVATE
                            )
                        with(sharedPref.edit()) {
                            putBoolean(getString(R.string.logged), true)
                            putBoolean(getString(R.string.isEmployee), false)
                            putString(getString(R.string.company), c.companyid)
                            remove(getString(R.string.uid))
                            commit()
                        }

                        val i = Intent(this@Login, MainActivity::class.java)
                        startActivity(i)
                        finish()

                    } else {
                        // se tem userid e companyid e employee
                        // User faz login: guardar uid no uid e meter isEmployee = true
                        val sharedPref: SharedPreferences =
                            getSharedPreferences(
                                getString(R.string.preference_file_key),
                                Context.MODE_PRIVATE
                            )
                        with(sharedPref.edit()) {
                            putBoolean(getString(R.string.logged), true)
                            putBoolean(getString(R.string.isEmployee), true)
                            putString(getString(R.string.uid), c.userid)
                            putString(getString(R.string.company), c.companyid)
                            commit()
                        }

                        val i = Intent(this@Login, MainActivityEmployee::class.java)
                        startActivity(i)
                        finish()

                    }
                    loginProgressView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                Log.e("****Login", t.toString())
            }
        })
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
}