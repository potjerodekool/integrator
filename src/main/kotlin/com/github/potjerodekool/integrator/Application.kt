package com.github.potjerodekool.integrator

/*
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.channel.PublishSubscribeChannel
import org.springframework.integration.dsl.*
import org.springframework.integration.dsl.context.IntegrationFlowBeanPostProcessor
import org.springframework.integration.transformer.GenericTransformer
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.support.GenericMessage
 */

import com.github.potjerodekool.integrator.service.Scheduler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

// https://medium.com/@venkivenki4b6/spring-dynamically-register-beans-in-4-ways-at-run-time-c1dc45dcbeb9
@SpringBootApplication
@Configuration
class Application {

    @Autowired
    private lateinit var scheduler: Scheduler

    /*
    @Autowired()
    @Qualifier("myChannel")
    private lateinit var channel: DirectChannel


    @Bean(name = ["myChannel"])
    fun channel(): DirectChannel {
        val channel = DirectChannel()
        channel.subscribe(messageHandler())
        return channel
    }

     */

    @PostConstruct
    fun postConstruct() {
        //scheduler.scheduleRssPolling(listOf("https://spring.io/blog.atom"), "0/5 * * ? * *")
        //channel.send(GenericMessage("Hello world"))
    }

    /*
    fun messageHandler(): MessageHandler {
        return MessageHandler {
            println(it)
        }
    }

     */
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}