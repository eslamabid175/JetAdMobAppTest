package com.eslam.jetadmobapp.core.di

import com.eslam.jetadmobapp.feateres.feater_addmob.AdmobViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { AdmobViewModel() }
}