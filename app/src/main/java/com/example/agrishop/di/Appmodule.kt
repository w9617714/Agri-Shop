package com.example.agrishop.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.agrishop.Firebase.FirebaseCommon
import com.example.agrishop.Util.Constants.INTRODUCTION_SP
import com.example.agrishop.Viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Appmodule {
    @Provides
    @Singleton
    fun getFirebaseAut()=FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun getFirebaseDatabase() = FirebaseDatabase.getInstance()
    @Provides
    @Singleton
    fun getFirebasefirestore() = Firebase.firestore

    @Provides
    fun provideIntroductionSp(
        application:Application
    )=application.getSharedPreferences(INTRODUCTION_SP,MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesFirebaseCommon(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    )=FirebaseCommon(firestore,firebaseAuth)


    @Provides
    @Singleton
    fun provideStorage()=FirebaseStorage.getInstance().reference


}


@Singleton
@Component(modules = [Appmodule::class])
interface AppComponent {
    // Define methods to inject your dependencies, such as your ViewModels
    fun inject(viewModel: LoginViewModel)
}
