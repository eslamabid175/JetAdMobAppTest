package com.eslam.jetadmobapp.feateres.feater_addmob

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.koin.androidx.compose.koinViewModel

@Preview
@Composable
fun AdmobScreenPreview() {
    AdmobScreen(
        modifier = Modifier,
        viewModel = AdmobViewModel(),
        viewState = AdmobState()
    )
}

@Composable
fun AdmobScreenFactory(
    modifier: Modifier,
) {
    val viewModel: AdmobViewModel = koinViewModel()
    val viewState by viewModel.state.collectAsState()
    AdmobScreen(
        modifier = modifier,
        viewModel = viewModel,
        viewState = viewState
    )
}

@Composable
fun AdmobScreen(
    modifier: Modifier,
    viewModel: AdmobViewModel,
    viewState: AdmobState,
) {
    val action: (AdmobViewIntent) -> Unit = { viewModel.dispatchViewAction(it) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        action(AdmobViewIntent.AdMob.LoadBanner)
    }

    DisposableEffect(viewState.adView) {
        onDispose {
            viewState.adView?.destroy()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp), // Adjust bottom padding as needed
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AdmobInterstitialButtons(context = context, action = action)
            Spacer(modifier = Modifier.height(24.dp))
            AdmobVideoButtons(context = context, action = action)
        }
        if (viewState.adView != null) {
            viewState.adView?.let { adView ->
                AndroidView(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    factory = { adView }
                )
            }
        }
    }
}

@Composable
fun AdmobInterstitialButtons(
    action: (AdmobViewIntent) -> Unit,
    context: Context,
) {
    Text("AdmobInterstitial")
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
        action(AdmobViewIntent.AdMob.LoadInterstitial)
    }) {
        Text("AdmobInterstitial load")
    }
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
        action(AdmobViewIntent.AdMob.ShowInterstitial(activity = context as Activity))
    }) {
        Text("AdmobInterstitial show")
    }
}

@Composable
fun AdmobVideoButtons(
    action: (AdmobViewIntent) -> Unit,
    context: Context,
) {
    Text("AdmobVideo")
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
        action(AdmobViewIntent.AdMob.LoadVideo)
    }) {
        Text("AdmobVideo load")
    }
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = {
        action(AdmobViewIntent.AdMob.ShowVideo(activity = context as Activity))
    }) {
        Text("AdmobVideo show")
    }
}