package com.suwiki.remote.common.di

import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val firebaseDatabaseModule = module {
  single<FirebaseDatabase> {
    FirebaseDatabase.getInstance()
  }
}
