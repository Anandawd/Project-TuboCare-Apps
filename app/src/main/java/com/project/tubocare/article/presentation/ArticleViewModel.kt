package com.project.tubocare.article.presentation

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.tubocare.alarm.MedicationReminderReceiver
import com.project.tubocare.article.domain.model.Article
import com.project.tubocare.article.domain.repository.ArticleRepository
import com.project.tubocare.article.presentation.event.ArticleEvent
import com.project.tubocare.article.presentation.state.ArticleState
import com.project.tubocare.article.presentation.util.downloadAndSaveImageArticle
import com.project.tubocare.core.util.Resource
import com.project.tubocare.medication.domain.model.ChecklistEntry
import com.project.tubocare.medication.domain.model.Medication
import com.project.tubocare.medication.presentation.event.MedicationEvent
import com.project.tubocare.medication.presentation.state.MedicationState
import com.project.tubocare.medication.presentation.util.getDayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class  ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val application: Application
) : ViewModel() {

    var state by mutableStateOf(ArticleState())
        private set

    init {
        getArticles()
    }

    fun onEvent(event: ArticleEvent) {
        when (event) {
            ArticleEvent.GetArticles -> {
                getArticles()
            }

            is ArticleEvent.GetArticleById -> {
                getArticleById(event.value)
            }

            ArticleEvent.ClearToast -> {
                clearToastMessage()
            }

            ArticleEvent.ResetStatus -> {
                resetStatus()
            }

            is ArticleEvent.SelectArticle -> {
                onSelectedArticle(event.data)
            }
        }
    }

    private fun getArticles() = viewModelScope.launch {
        articleRepository.getArticles()
            .catch { e ->
                throw Exception("Terjadi kesalahan yang tidak diketahui: ${e.message}")
            }
            .collectLatest {
                when (it) {
                    is Resource.Success -> {
                        state = state.copy(
                            articleList = it.data?.sortedBy { article -> article.id } ?: emptyList(),
                            selectionArticle = it.data?.firstOrNull()
                        )
                        // Unduh dan simpan gambar secara lokal
                        it.data?.forEach { article ->
                            article.localImagePath = article.imageUrl.let { url ->
                                downloadAndSaveImageArticle(application, url, article.documentId)
                            }
                            // Perbarui artikel di database lokal dengan path gambar lokal
                            articleRepository.updateLocalArticle(article.toArticleEntity())
                        }
                    }

                    is Resource.Error -> {
                        throw Exception("Terjadi kesalahan yang tidak diketahui: ${it.message}")
                    }

                    else -> {}
                }
            }
    }

    private fun getArticleById(id: String) = viewModelScope.launch {
        state = state.copy(isLoading = true)
        articleRepository.getArticleById(id).collect { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { article ->
                        setArticleState(article)
                        state = state.copy(isSuccessGetDetail = true)
                    }
                }

                is Resource.Error -> {
                    state = state.copy(
                        isSuccessGetDetail = false,
                        errorGetDetail = result.message
                    )
                }

                else -> {}
            }
        }
        state = state.copy(isLoading = false)
    }

    private fun setArticleState(article: Article) {
        state = state.copy(
            documentId = article.documentId,
            id = article.id,
            title = article.title,
            content = article.content,
            imageUrl = article.imageUrl,
            imagePath = article.localImagePath,
        )
    }

    private fun resetStatus() {
        state = state.copy(
            isSuccessGetDetail = false,
        )
    }

    private fun clearToastMessage() {
        state = state.copy(
            errorGetDetail = null,
        )
    }

    private fun onSelectedArticle(article: Article) {
        state = state.copy(selectionArticle = article)
    }
}
/* private fun getArticleById(id: String) = viewModelScope.launch {
     articleRepository.getArticleById(id)
         .catch { e ->
             state = state.copy(errorGetDetail = e.localizedMessage)
         }
         .collectLatest { result ->
             when (result) {
                 is Resource.Success -> {
                     result.data?.let { article ->
                         val localImagePath = downloadAndSaveImageArticle(application, article.imageUrl, article.documentId)
                         val updatedArticle = article.copy(localImagePath = localImagePath)
                         state = state.copy(
                             documentId = updatedArticle.documentId,
                             id = updatedArticle.id,
                             title = updatedArticle.title,
                             content = updatedArticle.content,
                             imageUrl = updatedArticle.imageUrl,
                             imagePath = updatedArticle.localImagePath,
                             isSuccessGetDetail = true
                         )
                         // Perbarui artikel di database lokal dengan path gambar lokal
                         articleRepository.updateLocalArticle(updatedArticle.toArticleEntity())
                     }
                 }

                 is Resource.Error -> {
                     state = state.copy(
                         isSuccessGetDetail = false,
                         errorGetDetail = result.message
                     )
                 }

                 else -> {}
             }
         }
 }*/

/*  private fun getArticles() = viewModelScope.launch {
      Log.d("ArticleVM", "loading data...")
      articleRepository.getArticles().collect {
          when (it) {
              is Resource.Success -> {
                  Log.d("ArticleVM", "success: ${it.data}")
                  state = state.copy(
                      articleList = it.data?.sortedBy { article -> article.id } ?: emptyList(),
                      selectionArticle = it.data?.firstOrNull()
                  )
                  it.data?.forEach { article ->
                      article.localImagePath = article.imageUrl.let { url ->
                          downloadAndSaveImageArticle(application, url, article.documentId)
                      }
                      articleRepository.updateLocalArticle(article.toArticleEntity())
                  }
              }

              is Resource.Error -> {
                  Log.e("ArticleVM", "Error: ${it.message}")
              }

              else -> {}

          }
      }
  }*/
