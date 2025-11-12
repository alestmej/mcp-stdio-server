package com.ales.mcp

import io.modelcontextprotocol.client.McpClient
import io.modelcontextprotocol.client.transport.ServerParameters
import io.modelcontextprotocol.client.transport.StdioClientTransport
import io.modelcontextprotocol.json.McpJsonMapper
import io.modelcontextprotocol.spec.McpSchema
import java.util.Map
import kotlin.system.exitProcess

/**
 * To start this client you must build the server jar first:
 * <pre>
 * ./mvnw clean install -DskipTests
 * </pre>
 */
fun main(args: Array<String>) {
    val (apiKey, jwtToken) = parseApiKeyAndJwtTokenOrExit(args)
    val stdioParams = ServerParameters.builder("java")
        .args(
            "-Dspring.ai.mcp.server.transport=STDIO",
            "-Dspring.main.web-application-type=none",
            "-jar",
            "target/mcp-stdio-server-0.0.1-SNAPSHOT.jar"
        )
        .build()

    val transport = StdioClientTransport(stdioParams, McpJsonMapper.createDefault())
    val client = McpClient.sync(transport).build()

    client.initialize()

    // List and demonstrate tools
    val toolsList = client.listTools()
    println("Available Tools = $toolsList\n")

    val appliances = client.callTool(
        McpSchema.CallToolRequest(
            "getElectroluxAppliances",
            Map.of<String?, Any?>("apiKey", apiKey, "jwtToken", jwtToken)
        )
    )
    println("My appliances: $appliances\n")

    val applianceId = "950011383142014705087076"
    val info = McpSchema.CallToolRequest(
        "getElectroluxApplianceInfo",
        Map.of<String?, Any?>("apiKey", apiKey, "jwtToken", jwtToken, "applianceId", applianceId)
    )

    println("State of appliance:  $applianceId is: $info")

    client.closeGracefully()
}

fun parseApiKeyAndJwtTokenOrExit(args: Array<String>): Pair<String, String> {
    val argMap = args.mapNotNull { arg ->
        val split = arg.split("=", limit = 2)
        if (split.size == 2) split[0].removePrefix("--") to split[1] else null
    }.toMap()

    val apiKey = argMap["apiKey"]
    val jwtToken = argMap["jwtToken"]

    return if (!jwtToken.isNullOrBlank() && !apiKey.isNullOrBlank()) {
        apiKey to jwtToken
    } else {
        System.err.println("Usage: <program> --apiKey=<key> --jwtToken=<token>")
        System.err.println("To get the credentials login to https://developer.electrolux.one/dashboard")
        exitProcess(1)
    }
}