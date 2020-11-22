package com.github.potjerodekool.integrator.rss

import com.rometools.rome.feed.synd.SyndFeed
import org.springframework.beans.factory.InitializingBean
//import org.springframework.integration.core.MessageSource
//import org.springframework.messaging.Message
//import org.springframework.messaging.support.MessageBuilder
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.logging.Level
import java.util.logging.Logger

class RssMessageSource() /*: MessageSource<List<SyndFeed>>, InitializingBean*/ {

    /*
    private companion object {
        private val logger: Logger = Logger.getLogger(RssMessageSource::class.java.name)
    }

    private val urls = CopyOnWriteArrayList<String>()

    constructor(urls: List<String>): this() {
        addUrls(urls)
    }

    override fun receive(): Message<List<SyndFeed>>? {
        val feeds = obtainFeedItems()
        return MessageBuilder.withPayload(feeds).build()
    }

    private fun obtainFeedItems(): List<SyndFeed> {
        val feed: MutableList<SyndFeed> = ArrayList()
        try {
            for (url in urls) {
                 feed.add(RssFeedFetcher.fetchFeed(url))
            }
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "IO Problem while retrieving feed", e)
        }
        return feed
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
    }

    fun addUrl(url: String) {
        this.urls += url
    }

    fun addUrls(urls: List<String>) {
        this.urls += urls
    }

    fun removeUrl(url: String) {
        this.urls -= url
    }

    fun removeUrls(urls: List<String>) {
        this.urls -= urls
    }

     */
}