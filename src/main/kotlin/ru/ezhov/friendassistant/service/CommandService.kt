package ru.ezhov.friendassistant.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.Executors

private val logger = KotlinLogging.logger { }

@Service
class CommandService(
    @Value("\${client.host}")
    private val clientHost: String,
    @Value("\${client.token}")
    private val clientToken: String,
) {
    private val executor = Executors.newFixedThreadPool(4)

    fun doCommand(text: String) {
        executor.execute {
            try {
                HttpClients.createDefault().use { client ->
                    val httpPost = HttpPost("$clientHost/api/talk")
                    httpPost.addHeader(BasicHeader("Authorization", "Bearer $clientToken"))
                    httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                    httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")

                    val body = mapOf("text" to text)
                    val json = ObjectMapper().writeValueAsString(body)
                    httpPost.entity = StringEntity(json, Charsets.UTF_8)
                    client.execute(httpPost).use { response ->
                        logger.debug {
                            "url='${httpPost.uri}' " +
                                "arguments='$body' " +
                                "status='${response.statusLine.statusCode}' " +
                                "response body='${response.entity.content.readBytes().toString(Charsets.UTF_8)}'"
                        }
                    }
                }
            } catch (ex: Exception) {
                logger.warn(ex) { "Error when call" }
            }
        }
    }
}
