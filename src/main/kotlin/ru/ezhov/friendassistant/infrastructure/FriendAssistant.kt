package ru.ezhov.friendassistant.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicHeader
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import ru.ezhov.friendassistant.event.ObserverFactory
import ru.ezhov.friendassistant.event.SayNameEvent
import ru.ezhov.friendassistant.event.SayTextEvent
import ru.ezhov.friendassistant.event.StopSayNameEvent
import java.util.stream.Collectors
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

private val logger = KotlinLogging.logger {}
private const val PROPERTY_PATH_FILE = "vosk.model.folder.path"

class FriendAssistant {
    fun start() {
        LibVosk.setLogLevel(LogLevel.DEBUG)

        val format = AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            60000f,
            16,
            2,
            4,
            44100f,
            false
        )
        val info = DataLine.Info(TargetDataLine::class.java, format)
        var microphone: TargetDataLine

        try {
            Model(System.getProperty(PROPERTY_PATH_FILE)).use { model ->
                Recognizer(model, 120000F).use { recognizer ->
                    microphone = AudioSystem.getLine(info) as TargetDataLine
                    microphone.open(format)
                    microphone.start()

                    var numBytesRead: Int
                    val CHUNK_SIZE = 1024
                    var bytesRead = 0
                    val b = ByteArray(4096)
                    val friendName = "джарвис"
                    while (true) {
                        numBytesRead = microphone.read(b, 0, CHUNK_SIZE)
                        bytesRead += numBytesRead
                        var lastValue = ""
                        if (recognizer.acceptWaveForm(b, numBytesRead)) {
                            val result: String = recognizer.getResult()
                            if ("" != result) {
                                val mapper = ObjectMapper()
                                lastValue = mapper.readTree(result).get("text").asText()
                            }
                        }
                        if ("" != lastValue) {
                            logger.debug { "text='$lastValue'" }
                        }
                        if (lastValue.startsWith(friendName)) {
                            logger.debug { "catch name '$friendName'" }

                            ObserverFactory.observer.fire(SayNameEvent())
                            logger.debug { "fire event 'say name'" }

                            ObserverFactory.observer.fire(SayTextEvent(lastValue))
                            logger.debug { "fire event 'say text'" }

                            when {
                                (lastValue.startsWith("$friendName задача")) -> {
                                    val list = lastValue.split(" ".toRegex())
                                    if (list.size > 2) {
                                        val words: List<String> = list.subList(2, list.size)
                                        val numbers: Map<String, String> = mapOf(
                                            "один" to "1",
                                            "два" to "2",
                                            "три" to "3",
                                            "четыре" to "4",
                                            "пять" to "5",
                                            "шесть" to "6",
                                            "семь" to "7",
                                            "восемь" to "8",
                                            "девять" to "9",
                                            "ноль" to "0",

                                            )
                                        val number = words.stream().map { key: String -> numbers[key] }.collect(Collectors.joining(""))
                                        println("number $number")
                                        HttpClients.createDefault().use { client ->
                                            val httpPost = HttpPost(
                                                "http://localhost:4567/api/v1/handlers/64a9570c-6600-4635-99a5-425946f87424/openUrl"
                                            )
                                            httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))
                                            httpPost.setEntity(StringEntity("{\"text\":\"$number\"}"))
                                            httpPost.setHeader("Accept", "application/json")
                                            httpPost.setHeader("Content-type", "application/json")
                                            client.execute(httpPost).use { response -> System.out.println("status: " + response.getStatusLine().getStatusCode()) }
                                        }
                                    }
                                }

                                (lastValue.startsWith("$friendName поиск")) -> {
                                    val text = lastValue.split(" ").let {
                                        it.subList(2, it.size)
                                    }.joinToString(separator = " ")


                                    HttpClients.createDefault().use { client ->
                                        val httpPost = HttpPost(
                                            "http://localhost:4567/api/v1/handlers/87171b26-1796-432e-a9bc-82224a9b3686/openUrl"
                                        )
                                        httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

                                        val json = ObjectMapper()
                                            .let { mapper ->
                                                mapper.writeValueAsString(mutableMapOf("text" to text))
                                            }

                                        httpPost.entity = StringEntity(json, Charsets.UTF_8)
                                        httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                                        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
                                        client.execute(httpPost).use { response ->
                                            logger.debug { "status: " + response.statusLine.statusCode }
                                        }
                                    }
                                }

                                (lastValue.startsWith("$friendName бэкап паролей")) -> {
                                    HttpClients.createDefault().use { client ->
                                        val httpPost = HttpPost(
                                            "http://localhost:4567/api/v1/handlers/95629ac7-c988-4799-9d0c-380eca2cae16/executeScript"
                                        )
                                        httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

                                        val json = ObjectMapper()
                                            .let { mapper ->
                                                mapper.writeValueAsString(emptyMap<String, String>())
                                            }

                                        httpPost.entity = StringEntity(json, Charsets.UTF_8)
                                        httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                                        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
                                        client.execute(httpPost).use { response ->
                                            logger.debug { "status: " + response.statusLine.statusCode }
                                        }
                                    }
                                }

                                (lastValue.startsWith("$friendName бэкап действий")) -> {
                                    HttpClients.createDefault().use { client ->
                                        val httpPost = HttpPost(
                                            "http://localhost:4567/api/v1/handlers/2f359ee7-6bfb-4adf-84ab-cfaef73b8cff/executeScript"
                                        )
                                        httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

                                        val json = ObjectMapper()
                                            .let { mapper ->
                                                mapper.writeValueAsString(emptyMap<String, String>())
                                            }

                                        httpPost.entity = StringEntity(json, Charsets.UTF_8)
                                        httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                                        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
                                        client.execute(httpPost).use { response ->
                                            logger.debug { "status: " + response.statusLine.statusCode }
                                        }
                                    }
                                }

                                (lastValue.startsWith("$friendName покажи ошибки")) -> {
                                    HttpClients.createDefault().use { client ->
                                        val httpPost = HttpPost(
                                            "http://localhost:4567/api/v1/handlers/0d366248-7f72-4d40-b096-a6185fa9ead8/openUrl"
                                        )
                                        httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

                                        val json = ObjectMapper()
                                            .let { mapper ->
                                                mapper.writeValueAsString(emptyMap<String, String>())
                                            }

                                        httpPost.entity = StringEntity(json, Charsets.UTF_8)
                                        httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                                        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
                                        client.execute(httpPost).use { response ->
                                            logger.debug { "status: " + response.statusLine.statusCode }
                                        }
                                    }
                                }

                                (lastValue.startsWith("$friendName доска")) -> {
                                    HttpClients.createDefault().use { client ->
                                        val httpPost = HttpPost(
                                            "http://localhost:4567/api/v1/handlers/c35aa877-3a6a-4a30-8617-7c7c793db23a/openUrl"
                                        )
                                        httpPost.addHeader(BasicHeader("X-Rocket-Action-Handler-Key", "1234"))

                                        val json = ObjectMapper()
                                            .let { mapper ->
                                                mapper.writeValueAsString(emptyMap<String, String>())
                                            }

                                        httpPost.entity = StringEntity(json, Charsets.UTF_8)
                                        httpPost.addHeader("Accept", "application/json;charset=UTF-8")
                                        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8")
                                        client.execute(httpPost).use { response ->
                                            logger.debug { "status: " + response.statusLine.statusCode }
                                        }
                                    }
                                }
                            }

                            ObserverFactory.observer.fire(StopSayNameEvent())

                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

