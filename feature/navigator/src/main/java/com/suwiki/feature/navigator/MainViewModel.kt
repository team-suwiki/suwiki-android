package com.suwiki.feature.navigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suwiki.core.android.recordException
import com.suwiki.core.model.exception.NetworkException
import com.suwiki.core.model.exception.UnknownException
import com.suwiki.data.openmajor.datasource.LocalOpenMajorDataSource
import com.suwiki.domain.notice.usecase.CheckUpdateMandatoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val checkUpdateMandatoryUseCase: CheckUpdateMandatoryUseCase,
  private val localOpenMajorDataSource: LocalOpenMajorDataSource,
) : ContainerHost<MainState, MainSideEffect>, ViewModel() {
  override val container: Container<MainState, MainSideEffect> = container(MainState())

  private val mutex = Mutex()
  private var isFirstVisit: Boolean = true

  val needShowSuwikiServiceEnd = localOpenMajorDataSource.isSuwikiServerEndPopupVisible()

  fun neverShow() = viewModelScope.launch {
    localOpenMajorDataSource.setShowSuwikiServerEndPopup(false)
  }

  fun checkUpdateMandatory(versionCode: Long) = intent {
    if (isFirstVisit.not()) return@intent
    checkUpdateMandatoryUseCase(versionCode)
      .onSuccess { updateMandatory ->
        reduce { state.copy(showUpdateMandatoryDialog = updateMandatory) }
      }
    isFirstVisit = true
  }

  fun openPlayStoreSite() = intent { postSideEffect(MainSideEffect.OpenPlayStoreSite) }

  fun onShowToast(msg: String) = intent {
    mutex.withLock {
      reduce { state.copy(toastMessage = msg, toastVisible = true) }
      delay(SHOW_TOAST_LENGTH)
      reduce { state.copy(toastVisible = false) }
    }
  }

  fun handleException(throwable: Throwable) = intent {
    when (throwable) {
      is NetworkException -> reduce { state.copy(showNetworkErrorDialog = true) }
      is ConnectException -> reduce { state.copy(showNetworkErrorDialog = true) }
      else -> {
        onShowToast(throwable.message ?: UnknownException().message)
        recordException(throwable)
      }
    }
  }

  fun hideNetworkErrorDialog() = intent { reduce { state.copy(showNetworkErrorDialog = false) } }

  companion object {
    private const val SHOW_TOAST_LENGTH = 3000L
  }
}
