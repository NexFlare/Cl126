package com.nexflare.cloud126

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var otpEntered: Boolean=false
    private var mobileEntered: Boolean=false
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth: FirebaseAuth
    private var dialog: ProgressDialog? = null
    var verificationId: String?=null
    var token:PhoneAuthProvider.ForceResendingToken?=null
    var number: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth= FirebaseAuth.getInstance()
        loginBtn.setOnClickListener{
            if (!mobileEntered) {
                if(phoneNumberEt.text.toString().length==10) {
                    loginDetailTv.text = "OTP has been send to your \n mobile number. Please enter it below."
                    mobileEntered = true
                    number=phoneNumberEt.text.toString()
                    phoneNumberTil.visibility= View.GONE
                    otpTil.visibility=View.VISIBLE
                    if(phoneNumberTil.isErrorEnabled){
                        phoneNumberTil.isErrorEnabled=false
                    }
                    loginBtn.text = "LOGIN"
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+number,
                            60,
                            TimeUnit.SECONDS,
                            this@MainActivity,
                            callback
                    )
                }
                else{
                    phoneNumberTil.error="Enter valid phone number"
                }
            }
            else if(!otpEntered){
                if(otpEt.text.toString().isNotEmpty()) {
                    val code = otpEt.text.toString()
                    Log.d("TAGGER",code);
                    val phoneAuthCredentials = PhoneAuthProvider.getCredential(this.verificationId!!, code)
                    dialog = ProgressDialog(this@MainActivity)
                    dialog!!.setMessage("Please wait...")
                    dialog!!.setCancelable(false)
                    dialog!!.show()
                    otpEntered=true
                    signInWithPhoneAuthCredential(phoneAuthCredentials)
                }
                else{
                    Toast.makeText(this@MainActivity,"Please enter correct Otp",Toast.LENGTH_LONG).show()
                }
            }
            else{

                if(nameEt.text.toString().isEmpty())
                    nameTil.error="Please enter your name"
            }
        }


        callback=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                if (p0 != null) {
                    dialog = ProgressDialog(this@MainActivity)
                    dialog!!.setMessage("Please wait...")
                    dialog!!.setCancelable(false)
                    dialog!!.show()
                    signInWithPhoneAuthCredential(p0)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                Toast.makeText(this@MainActivity,p0?.localizedMessage,Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verificationId, token)
                this@MainActivity.verificationId=verificationId
                this@MainActivity.token=token
                loginBtn.isEnabled=true
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String?) {
                super.onCodeAutoRetrievalTimeOut(p0)
                this@MainActivity.verificationId=verificationId

            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@MainActivity,BrowseActivity::class.java))
                        finishAffinity()
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGGER", "signInWithCredential:success")
                        //Toast.makeText(this@MainActivity,"Sign in complete",Toast.LENGTH_SHORT).show()

                        // ...
                    } else {
                        dialog?.dismiss()
                        // Sign in failed, display a message and update the UI
                        Log.w("TAGGER", "signInWithCredential:failure", task.exception)


                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this@MainActivity,"Please enter a valid otp.",Toast.LENGTH_LONG).show()
                        }
                        if(task.exception is FirebaseNetworkException){
                            Toast.makeText(this@MainActivity,"Please check your network connection",Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                })
    }
}
