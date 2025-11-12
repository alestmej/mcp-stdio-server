package com.ales.mcp.electrolux

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class ElectroluxService {

    private val restClient: RestClient = RestClient.builder()
        .baseUrl("https://api.developer.electrolux.one/api/v1")
        .defaultHeader("User-Agent", "McpServerElectrolux/1.0 Ales Tmej")
        .build()

    @Tool(description = "Get list of my Electrolux appliances using jwtToken and apiKey.")
    fun getElectroluxAppliances(apiKey: String, jwtToken: String) =
        restClient.get()
            .uri("/appliances")
            .header("x-api-key", apiKey)
            .header("Authorization", "Bearer $jwtToken")
            .retrieve()
            .body(String::class.java)
            ?: "Error"

    @Tool(description = "Get info of my Electrolux appliance with applianceId using jwtToken and apiKey.")
    fun getElectroluxApplianceInfo(apiKey: String, jwtToken: String, applianceId: String) =
        restClient.get()
            .uri("/appliances/$applianceId/info")
            .header("x-api-key", apiKey)
            .header("Authorization", "Bearer $jwtToken")
            .retrieve()
            .body(String::class.java)
            ?: "Error"

}