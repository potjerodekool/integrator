package com.github.potjerodekool.integrator.rss

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import org.xml.sax.InputSource
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object RssFeedFetcher {

    private val client = HttpClient.newHttpClient()

    fun fetchFeed(uri: String): SyndFeed {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofInputStream())
        val syndFeedInput = SyndFeedInput()
        return syndFeedInput.build(InputSource(response.body()))
    }
}