package com.eslam.jetadmobapp.core.di


import com.eslam.jetadmobapp.commons.addmob.AdMobManager
import org.koin.dsl.module

val commonsModule = module {
    single<AdMobManager> { AdMobManager(get()) }
}