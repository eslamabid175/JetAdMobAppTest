package com.eslam.jetadmobapp.feateres.feater_addmob


import android.app.Activity

sealed class AdmobViewIntent {

    object AdMob {
        data object LoadInterstitial : AdmobViewIntent()
        data class ShowInterstitial(val activity: Activity) : AdmobViewIntent()
        data object LoadVideo : AdmobViewIntent()
        data class ShowVideo(val activity: Activity) : AdmobViewIntent()
        data object LoadBanner : AdmobViewIntent()
    }

}