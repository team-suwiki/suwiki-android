package com.suwiki.presentation.openmajor.model

import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

data class OpenMajor(
  val id: UUID = UUID.randomUUID(),
  val name: String,
  val isSelected: Boolean = false,
)

fun List<String>.toBookmarkedOpenMajorList(
  searchValue: String,
  selectedOpenMajor: String,
) = filter { openMajor ->
  if (searchValue.isNotEmpty()) {
    searchValue in openMajor
  } else {
    true
  }
}.map { name ->
  OpenMajor(
    name = name,
    isSelected = selectedOpenMajor == name,
  )
}.toPersistentList()

fun List<String>.toOpenMajorList(
  searchValue: String,
  selectedOpenMajor: String,
) = filter { openMajor ->
  if (searchValue.isNotEmpty()) {
    searchValue in openMajor
  } else {
    true
  }
}.map { name ->
  OpenMajor(
    name = name,
    isSelected = selectedOpenMajor == name,
  )
}.toPersistentList()
