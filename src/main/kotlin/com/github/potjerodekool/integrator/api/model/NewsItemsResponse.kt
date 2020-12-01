package com.github.potjerodekool.integrator.api.model

class NewsItemsResponse(val total: Long,
                        val pages: Int,
                        val currentPage: Int,
                        val records: List<FeedEntryModel>)