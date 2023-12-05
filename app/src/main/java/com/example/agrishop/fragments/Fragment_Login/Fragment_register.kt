package com.example.agrishop.fragments.Fragment_Login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.agrishop.Data.Users
import com.example.agrishop.R


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class Fragment_register:Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val firestore = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_register_page, container, false)
        val FirstName = view.findViewById<EditText>(R.id.FirstName)
        val LastName = view.findViewById<EditText>(R.id.LastName)
        val emailEditText = view.findViewById<EditText>(R.id.email)
        val passwordEditText = view.findViewById<EditText>(R.id.Password)
        val submit = view.findViewById<AppCompatButton>(R.id.submit)
        val reallayout=view.findViewById<ConstraintLayout>(R.id.reallayout)
        val lottie=view.findViewById<ConstraintLayout>(R.id.constraintLayout)
        val gotoLOginPage=view.findViewById<TextView>(R.id.gotologinPageRegi)


        database = Firebase.database.reference

        submit.setOnClickListener {
            reallayout.visibility=View.GONE
            lottie.visibility=View.VISIBLE
            // Retrieve email and password when  the button is clicked
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Check if email or password is empty
            if (email.isEmpty() || password.isEmpty()||FirstName.text.isEmpty()||LastName.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Fill all the Feilds",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            else if(Patterns.EMAIL_ADDRESS.equals(email)){
                Toast.makeText(
                    requireContext(),
                    "Wrong Email Format",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            gotoLOginPage.setOnClickListener {
                navigate(Fragment_intro())
            }

            // Log the details
            Log.d("logindetails", "login Details: $email $password")

            // Perform user creation with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->

                 if (task.isSuccessful) {

                     val userInfo:Users= Users(FirstName.text.toString(),LastName.text.toString(),email,""
                     )


                     addData(userInfo)
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail: success")
                    val user = auth.currentUser
                    Log.d("user", "onCreateView: ${user!!.uid}")
                     lottie.visibility=View.GONE
                     reallayout.visibility=View.VISIBLE
                     Toast.makeText(requireContext(), "Account Created SuccessFull", Toast.LENGTH_SHORT).show()
                     navigate(Fragment_login())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("fail", "createUserWithEmail: failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                     lottie.visibility=View.GONE
                     reallayout.visibility=View.VISIBLE
                }
            }
        }




        return view
    }

    private fun addData(userInfo: Users) {
//        database.child("users").child(auth.uid.toString()).setValue(userInfo)
        firestore.collection("users").document(auth.uid.toString()).set(userInfo)
            .addOnSuccessListener {
                Log.d(TAG, "SuccessFully Added")
            }
            .addOnFailureListener { e ->
                // Handle failure
                // For example: Log.d(TAG, "Error adding document: ${e.message}")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        auth= Firebase.auth
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
              navigate(Fragment_intro())
            }

        })

    }


    private fun navigate(fragmentRegister: Fragment) {


        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()


        transaction.replace(R.id.fragmentContainerView, fragmentRegister)


        transaction.commit()
    }
}