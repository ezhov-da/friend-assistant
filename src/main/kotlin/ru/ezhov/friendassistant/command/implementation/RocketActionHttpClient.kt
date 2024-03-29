package ru.ezhov.friendassistant.command.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader

private const val ROCKET_ACTION_PORT = "rocket.action.port"
private val logger = KotlinLogging.logger {}

class RocketActionHttpClient {
    private val port: String = System.getProperty(ROCKET_ACTION_PORT) ?: "4567"

    fun executeAction(id: String, text: String) {
        HttpClients.createDefault().use { client ->
            val httpPost = HttpPost(
                "http://localhost:$port/api/v1/handlers/48344b6d-cea7-4460-8c0e-7824d661d10e/execute-action"
            )
            httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

            val body = mapOf("_id" to id, "text" to text)

            val json = ObjectMapper()
                .let { mapper ->
                    mapper.writeValueAsString(body)
                }

            httpPost.entity = StringEntity(json, Charsets.UTF_8)
            httpPost.addHeader("Accept", "application/json;charset=UTF-8")
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
            client.execute(httpPost).use { response ->
                logger.debug {
                    "url='${httpPost.uri}' " +
                        "id='$id' " +
                        "commandName='execute-action' " +
                        "arguments='$body' " +
                        "status='${response.statusLine.statusCode}' " +
                        "body='${response.entity.content.readBytes().toString(Charsets.UTF_8)}'"
                }
            }
        }
    }

    fun send(id: String, commandName: String, arguments: Map<String, String>) {
        HttpClients.createDefault().use { client ->
            val httpPost = HttpPost(
                "http://localhost:$port/api/v1/handlers/$id/$commandName"
            )
            httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

            val json = ObjectMapper()
                .let { mapper ->
                    mapper.writeValueAsString(arguments)
                }

            httpPost.entity = StringEntity(json, Charsets.UTF_8)
            httpPost.addHeader("Accept", "application/json;charset=UTF-8")
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
            client.execute(httpPost).use { response ->
                logger.debug {
                    "url='${httpPost.uri}' " +
                        "id='$id' " +
                        "commandName='$commandName' " +
                        "arguments='$arguments' " +
                        "status='${response.statusLine.statusCode}"
                }
            }
        }
    }
}
