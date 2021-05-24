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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    // Auth
    private lateinit var auth: FirebaseAuth

    // Inputs
    private lateinit var emailTV: TextView
    private lateinit var passwordTV: TextView
    private lateinit var loginProgressView: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        emailTV = findViewById(R.id.emailText)
        passwordTV = findViewById(R.id.passwdText)
        loginProgressView = findViewById(R.id.loginProgress)

        // Shared Preferences
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        val isLogged = sharedPref.getBoolean(getString(R.string.logged), false)
        if (isLogged) {
            // TODO: Enviar o user para a sua pagina consoante o cargo

            val i = Intent(this@Login, MainActivity::class.java)
            startActivity(i)
            finish()

            // TODO: AQUI É O DO EMPLOYEE BABE ZE CARLOS 
            /*val i = Intent(this@Login, MainActivityEmployee::class.java)
            startActivity(i)
            finish()*/

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

                        val request = ServiceBuilder.buildService(Endpoints::class.java)
                        val call = request.login(user!!.uid)

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

                                        // TODO: Enviar o user para a sua pagina consoante o cargo

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

                                        // TODO: Enviar o user para a sua pagina consoante o cargo


                                    }
                                    loginProgressView.visibility = View.GONE
                                }
                            }

                            override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                                Log.e("****Login", t.toString())
                            }
                        })
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

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}