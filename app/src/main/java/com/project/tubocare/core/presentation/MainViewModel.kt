package com.project.tubocare.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.auth.domain.repository.AuthRepository
import com.project.tubocare.core.domain.usecase.app_entry.AppEntryUseCase
import com.project.tubocare.core.util.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var splashState by mutableStateOf(true)
        private set
    var startDestination by mutableStateOf(Graph.AuthGraph)
        private set

    init {
        viewModelScope.launch {
            startDestination = if (authRepository.hasUser()){
                Graph.MainScreenGraph
            } else {
                Graph.AuthGraph
            }
            delay(300)
            splashState = false
        }
    }
}
*/

/*
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCase: AppEntryUseCase
): ViewModel() {

    var splashState by mutableStateOf(true)
        private set
    var startDestination by mutableStateOf(Graph.AuthGraph)
        private set

    init {
        appEntryUseCase.readAppEntry().onEach { shouldStartFromHome ->
            if (shouldStartFromHome) {
                startDestination = Graph.MainScreenGraph
            } else {
                startDestination = Graph.AuthGraph
            }
            delay(300)
            splashState = false
        }.launchIn(viewModelScope)
    }
}
*/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appEntryUseCase: AppEntryUseCase
): ViewModel() {

    var splashState by mutableStateOf(true)
        private set
    var startDestination by mutableStateOf(Graph.AuthGraph)
        private set

    init {
        appEntryUseCase.readAppEntry().onEach { shouldStartFromHome ->
            if (shouldStartFromHome && authRepository.hasUser()) {
                startDestination = Graph.MainScreenGraph
            } else {
                startDestination = Graph.AuthGraph
            }
            delay(300)
            splashState = false
        }.launchIn(viewModelScope)
    }
}

