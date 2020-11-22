package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.rss.RssMessageSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
//import org.springframework.integration.dsl.IntegrationFlows
//import org.springframework.integration.dsl.PollerFactory
//import org.springframework.integration.dsl.context.IntegrationFlowBeanPostProcessor
//import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Service
import java.util.*

@Service
class Scheduler {

    @Autowired
    private lateinit var configurableListableBeanFactory: ConfigurableListableBeanFactory

    //@Autowired
    //private lateinit var postProcessor: IntegrationFlowBeanPostProcessor

    fun scheduleRssPolling(urls: List<String>, cron: String) {
        if (urls.isEmpty()) {
            return
        }

        /*
        val rssMessageSource = RssMessageSource(urls)

        val flow = IntegrationFlows
            .from(rssMessageSource)
            {
                    e -> e.poller { p: PollerFactory -> p.cron(cron) }
            }
            .handle(messageHandler())
            .get()

        val uuid = UUID.randomUUID().toString()

        registerIntegrationBean("RssFlow-$uuid", flow)

         */
    }

    /*
    private fun messageHandler(): MessageHandler {
        //uri

        return MessageHandler { message ->
            println("message headers ${message.headers}")
            println("payload ${message.payload}")
        }
    }
     */

    private fun registerIntegrationBean(beanName: String, bean: Any) {
        configurableListableBeanFactory.registerSingleton(beanName, bean)
        //postProcessor.postProcessBeforeInitialization(bean, beanName)
        //postProcessor.postProcessAfterInitialization(bean, beanName)
    }
}