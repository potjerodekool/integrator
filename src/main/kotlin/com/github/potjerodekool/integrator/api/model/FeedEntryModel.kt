package com.github.potjerodekool.integrator.api.model

import java.time.LocalDateTime

data class FeedEntryModel(val link: String,
                          val title: String?,
                          val description: String,
                          val categories: List<String>,
                          val pubDate: LocalDateTime?)