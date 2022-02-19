package io.github.brunoribeiro.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
class WeatherApiApplication

fun main(args: Array<String>) {
    runApplication<WeatherApiApplication>(*args)
}

@RestController
class WeatherController(val restConfig: RestConfig) {
    val restTemplate = CustomRestTemplate()

    @GetMapping("/weather")
    fun weather(@RequestParam("location", required = true) location: String): WeatherInfoResponse? {
        val geoUrl = "${restConfig.geo?.url}?address=$location&key=${restConfig.geo?.key?.trim()}"

        return restTemplate
            .getForObject(geoUrl, GeoResponse::class.java)
            ?.results
            ?.firstOrNull()
            ?.let { g ->
                val weatherUrl =
                    "${restConfig.weather?.url}?lat=${g.geometry.location.lat}&lon=${g.geometry.location.lng}&key=${restConfig.weather?.key?.trim()}"
                restTemplate
                    .getForObject(weatherUrl, WeatherResponse::class.java)
                    ?.data
                    ?.random()
                    ?.let { w ->
                        val giphyUrl =
                            "${restConfig.giphy?.url}?q=weather%20${w.weather.description}&api_key=${restConfig.giphy?.key?.trim()}"
                        restTemplate
                            .getForObject(giphyUrl, GiphyResponse::class.java)
                            ?.data
                            ?.random()
                            ?.images
                            ?.get("original_mp4")
                            ?.let { gh ->
                                WeatherInfoResponse(g.geometry.location, w.temp, w.weather.description, gh["mp4"])
                            }


                    }

            }

    }
}

@Configuration
class WebConfiguration : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("*")
    }
}

class CustomRestTemplate : RestTemplate() {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(kotlinModule())

    override fun <T : Any> getForEntity(url: String, responseType: Class<T>, vararg uriVariables: Any): ResponseEntity<T> {
        return ResponseEntity.ok().body(
            objectMapper.readValue(super.getForObject(url, String::class.java), responseType)
        )
    }
}

@Configuration
@ConfigurationProperties(prefix = "api.keys")
class RestConfig {
    var geo: RestClientConfig? = null
    var weather: RestClientConfig? = null
    var giphy: RestClientConfig? = null
}

class RestClientConfig {
    var url: String? = null
    var key: String? = null
}

data class WeatherInfoResponse(val location: Location, val temp: String, val description: String, val image: String?)

data class GeoResponse(val results: List<GeoResult>)
data class GeoResult(val geometry: Geometry)
data class Geometry(val location: Location)
data class Location(val lat: String, val lng: String)

data class WeatherResponse(val data: List<WeatherResult>)
data class WeatherResult(val temp: String, val weather: Weather)
data class Weather(val description: String)

data class GiphyResponse(val data: List<GiphyResult>)
data class GiphyResult(val type: String, val images: Map<String, Map<String, String>>)
