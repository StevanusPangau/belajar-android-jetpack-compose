package com.learning.teorisideeffectdaneffecthandling

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.learning.teorisideeffectdaneffecthandling.ui.theme.TeoriSideEffectDanEffectHandlingTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeoriSideEffectDanEffectHandlingTheme {
            }
        }
    }
}

// ======================= Contoh Lifecycle Composable =======================
/*
alur Composable sangatlah simpel jika dibandingkan dengan Activity maupun Fragment.
Dimulai dari masuk ke Composition, kemudian Recomposition 0 kali atau lebih,
dan yang terakhir keluar dari Composition. Jadi, satu satunya cara
untuk memodifikasi Composition adalah melalui Recomposition.
*/
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        if (count > 0) {
            Text("Button clicked $count times:")
        }
        Button(onClick = { count++ }) {
            Text("Click me!")
        }
        Button(onClick = { count = 0 }) {
            Text("Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            Counter()
        }
    }
}

// ======================= Contoh Side Effect =======================
/*
Side Effect adalah segala sesuatu yang mengubah state dari luar scope Composable
Function. Idealnya, Composable Function seharusnya bebas dari Side Effect karena
bisa menyebabkan Recomposition yang tak terprediksi dan berulang kali.
 */
var i = 0

@Composable
fun SideEffect() {
    i++ // mengubah nilai di sini akan menyebabkan side effect
    Log.d("Check", "$i")
    Button(onClick = {}) {
        Text(text = "Click")
    }
}

@Preview(showBackground = true)
@Composable
private fun SideEffectPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            SideEffect()
        }
    }
}


// ======================= Contoh Effect Handler (Side Effect API) =======================

// >>>>>>>> launchedEffect <<<<<<<<
/*
digunakan untuk melakukan aksi tertentu yang hanya dipanggil sekali ketika initial composition atau parameter Key berubah.
 */
@Composable
fun MyCountdown() {
    var timer by remember { mutableStateOf(60) }
    Text("Countdown : $timer")
    LaunchedEffect(true) {
        while (timer > 0) {
            delay(1000)
            timer--
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyCountdownPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            MyCountdown()
        }
    }
}

// >>>>>>>> rememberUpdatedState <<<<<<<<
/*
digunakan untuk menandai suatu nilai supaya tidak ter-restart walaupun key berubah.
 */
@Composable
fun MyCountdownTimeout(onTimeout: () -> Unit) {
    var timer by remember { mutableStateOf(10) }
    val currentOnTimeout by rememberUpdatedState(onTimeout)
    Text("Countdown : $timer")
    LaunchedEffect(true) {
        while (timer > 0) {
            delay(1000)
            timer--
        }
        currentOnTimeout()
    }
}

@Composable
fun TimeOutScreen() {
    Text("Timeout!")
}

@Composable
fun MyApp() {
    var showTimeOutScreen by remember { mutableStateOf(false) }
    if (showTimeOutScreen) {
        TimeOutScreen()
    } else {
        MyCountdownTimeout(onTimeout = {
            Log.d("MyApp", "onTimeout called")
            showTimeOutScreen = true
        })
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            MyApp()
        }
    }
}

// >>>>>>>> rememberCoroutineScope <<<<<<<<
/*
membuat coroutine scope untuk menjalankan suspend function di luar Composable Function yang aware dengan lifecycle compose.
 */
@Composable
fun MyRememberedCoroutineScope() {
    var timer by remember { mutableStateOf(60) }
    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }
    Column {
        Text(text = "Countdown : $timer")
        Button(onClick = {
            job?.cancel()
            timer = 60
            job = scope.launch {
                while (timer > 0) {
                    delay(1000)
                    timer--
                }
            }
        }) {
            Text("Start")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyRememberedCoroutineScopePreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            MyRememberedCoroutineScope()
        }
    }
}

// >>>>>>>> DisposableEffect <<<<<<<<
/*
biasanya digunakan untuk membersihkan sesuatu ketika meninggalkan composition.
 */
@Composable
fun MyCountdownDisposableEffect() {
    var timer by remember { mutableStateOf(60) }
    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    Column {
        Text(text = "Countdown : $timer")
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    job?.cancel()
                    timer = 60
                    job = scope.launch {
                        while (timer > 0) {
                            delay(1000)
                            timer--
                        }
                    }
                } else if (event == Lifecycle.Event.ON_STOP) {
                    job?.cancel()
                    timer = 60
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyCountdownDisposableEffectPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            MyCountdownDisposableEffect()
        }
    }
}

// >>>>>>>> produceState <<<<<<<<
/*
berfungsi untuk membuat non Compose State menjadi Compose State baru.
Biasanya digunakan untuk mengubah data yang berasal dari repository menjadi sebuah UI state.
 */
data class ImageUiState(
    val imageData: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

// contoh seperti code beriktu: di comment karna kita tidak membuat ViewModel

//@Composable
//fun ImageScreen(
//    modifier: Modifier = Modifier,
//    viewModel: MyViewModel = viewModel()
//) {
//
//    val uiState by produceState(initialValue = ImageUiState(isLoading = true)) {
//        val image = viewModel.image
//        value = if (image != null) {
//            ImageUiState(imageData = image)
//        } else {
//            ImageUiState(isError = true)
//        }
//    }
//}


// >>>>>>>> derivedStateOf <<<<<<<<
/*
mengubah satu atau lebih State menjadi sebuah State baru.
Berbeda dengan remember, perubahan kode di dalamnya tidak akan menyebabkan Recomposition.
 */

// Salah satu contoh penggunaannya yaitu untuk membuat tombol jump to bottom. Berikut contoh simpelnya.

//val jumpToBottomButtonEnabled by remember {
//    derivedStateOf {
//        scrollState.firstVisibleItemIndex != 0 ||
//                scrollState.firstVisibleItemScrollOffset > jumpThreshold
//    }
//}


// >>>>>>>> snapshotFlow <<<<<<<<
/*
mengonversi State pada Compose menjadi Flow.
Kita dapat menggunakannya di dalam Side API lain, seperti LaunchedEffect.
 */
@Composable
fun MySnapshotFlow() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(500) { index ->
                Text(text = "Item: $index")
            }
        }

        var showButtonSnapshot by remember { mutableStateOf(false) }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            AnimatedVisibility(showButtonSnapshot) {
                Button({}) {
                    Text("Jump to Top")
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index -> index > 2 }
                .distinctUntilChanged()
                .collect {
                    showButtonSnapshot = it
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MySnapshotFlowPreview() {
    TeoriSideEffectDanEffectHandlingTheme {
        Column {
            MySnapshotFlow()
        }
    }
}