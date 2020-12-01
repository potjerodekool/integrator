package com.github.potjerodekool.integrator.config

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer





@Configuration
@EnableAsync
@EnableScheduling
class AsyncConfiguration(private val taskExecutionProperties: TaskExecutionProperties): AsyncConfigurer  {

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return SimpleAsyncUncaughtExceptionHandler()
    }

    @Bean(name = ["taskExecutor"])
    override fun getAsyncExecutor(): AsyncTaskExecutor? {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 1
        executor.maxPoolSize = 2
        executor.setQueueCapacity(50)
        executor.keepAliveSeconds = 5
        return executor
    }

    @Bean
    protected fun getTaskExecutor(): ConcurrentTaskExecutor? {
        return ConcurrentTaskExecutor(this.asyncExecutor)
    }

    @Bean
    protected fun webMvcConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
                configurer.setTaskExecutor(getTaskExecutor()!!)
            }
        }
    }

    /*
    @Bean
    fun webMvcConfigurerConfigurer(taskExecutor: AsyncTaskExecutor?, callableProcessingInterceptor: CallableProcessingInterceptor?)
            : WebMvcConfigurer? {

        return object : WebMvcConfigurer {
            override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
                configurer.setDefaultTimeout(360000).setTaskExecutor(taskExecutor!!)
                configurer.registerCallableInterceptors(callableProcessingInterceptor)
                super.configureAsyncSupport(configurer)
            }
        }
    }

    @Bean
    fun callableProcessingInterceptor(): CallableProcessingInterceptor? {
        return object : TimeoutCallableProcessingInterceptor() {
            @Throws(Exception::class)
            override fun <T> handleTimeout(request: NativeWebRequest, task: Callable<T>): Any {
                return super.handleTimeout(request, task)
            }
        }
    }
     */
}