package com.github.potjerodekool.integrator.api

import com.github.potjerodekool.integrator.api.model.FeedEntryModel
import com.github.potjerodekool.integrator.api.model.NewsItemsResponse
import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntry
import com.github.potjerodekool.integrator.service.FeedEntryService
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Api
@RestController
@CrossOrigin
class FeedController(private val feedEntryService: FeedEntryService) {

    @GetMapping("/news", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getNewsItems(): NewsItemsResponse {
        return getNewsItems(0)
    }

    @GetMapping("/news/{page}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getNewsItems(@PathVariable("page") page: Int): NewsItemsResponse {
        val result = feedEntryService.findFeedEntries(page)

        val records = result.toList()
                .filterNotNull()
                .map { it -> toFeedEntryModel(it) }
        return NewsItemsResponse(
                result.totalElements,
                result.totalPages,
                page,
                records
        )
    }

    private fun toFeedEntryModel(feedEntry: FeedEntry): FeedEntryModel {
        return FeedEntryModel(
                link = feedEntry.link,
                title = feedEntry.title,
                description = feedEntry.description,
                pubDate = feedEntry.pubDate,
                categories = feedEntry.categories.map { it.name }
        )
    }

}