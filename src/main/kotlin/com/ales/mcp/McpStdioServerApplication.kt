package com.ales.mcp

import com.ales.mcp.electrolux.ElectroluxService
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class McpStdioServerApplication {
    @Bean
    fun electroluxTools(electroluxService: ElectroluxService): ToolCallbackProvider {
        return MethodToolCallbackProvider.builder().toolObjects(electroluxService).build()
    }
}

fun main(args: Array<String>) {
    runApplication<McpStdioServerApplication>(*args)
}