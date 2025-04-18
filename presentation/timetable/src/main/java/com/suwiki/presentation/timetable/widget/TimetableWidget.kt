package com.suwiki.presentation.timetable.widget

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.fillMaxSize
import com.suwiki.common.model.timetable.Timetable
import com.suwiki.presentation.common.ui.extension.decodeFromUri
import com.suwiki.presentation.timetable.widget.timetable.GlanceTimetable
import kotlinx.serialization.json.Json

class TimetableWidget : GlanceAppWidget() {

  override val sizeMode = SizeMode.Exact

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent {
      val size = LocalSize.current
      val prefs = currentState<Preferences>()
      val timetableUri = prefs[TimetableWidgetReceiver.timetableWidgetKey] ?: ""
      val timetable = Json.decodeFromUri<Timetable>(timetableUri)

      GlanceTimetable(
        modifier = GlanceModifier
          .fillMaxSize(),
        size = size.width,
        timetable = timetable,
      )
    }
  }
}
