package com.suwiki.remote.common.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseDatabaseModule {

  @Singleton
  @Provides
  fun provideFirebaseDatabase(): FirebaseDatabase {
    return FirebaseDatabase.getInstance()
  }
}
