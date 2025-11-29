# Simple MCP Server to Control Electrolux Appliances

## Motivation
Prompt AI chat with questions like:
* "*How many Electrolux Appliances do I have?*" or 
* "*What is the color of my Electrolux Air Purifier?*"

Although the LLM model was not trained for that, you get *correct* answers!

How? Run this code and get surprised.

## Architecture
```text
+------------------------+      MCP Protocol      +-----------------+      REST API       +--------------------+                    +-----------------------------------+
|     AI Chat (LLM)      | <--------------------> |   MCP Server    | <-----------------> |  Electrolux Cloud  |<-----------------> |          Home Appliance           |
| (Claude, ChatGPT, ...) |                        |   (This Code)   |                     |                    |                    | (Vacuum, Oven, Air Purifier, ...) |
+------------------------+                        +-----------------+                     +--------------------+                    +-----------------------------------+
```
```Mermaid
flowchart LR
    A["AI Chat (LLM)<br>(Claude, ChatGPT, ...)"]
    B["MCP Server<br>(This Code)"]
    C["Electrolux Cloud"]
    D["Home Appliance<br>(Vacuum, Oven, Air Purifier, ...)"]

    A <--- "MCP Protocol" ---> B
    B <--- "REST API" ---> C
    C <--- " " ---> D

    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:2px
    style C fill:#ccf,stroke:#333,stroke-width:2px
    style D fill:#ddf,stroke:#333,stroke-width:2px
```
### References
* [Model Context Protocol](https://modelcontextprotocol.io)
* [Electrolux API](https://developer.electrolux.one)


## How to run

### 1. Build project ###
```bash
 * ./mvnw clean install -DskipTests
```
### 2. Integrate to Claude Desktop ###

To integrate with Claude Desktop, add the following configuration to the file `claude_desktop_config.json`.

Typical locations of `claude_desktop_config.json`:
- Windows: *C:\Users\<YourUser>\AppData\Roaming\Claude\claude_desktop_config.json*
- macOS: *~/Library/Application Support/Claude/claude_desktop_config.json*
- Linux: *~/.config/Claude/claude_desktop_config.json*

```json
{
  "mcpServers": {
    "mcp-stdio-server": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dspring.main.web-application-type=none",
        "-Dlogging.pattern.console=",
        "-jar",
        "<ABSOLUTE-PATH-TO-PROJECT>\\target\\mcp-stdio-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```
For authentication Electrolux API requires an API key and JWT token. AI chat assistant will ask for them during the first interaction. 
Login to [Electrolux for Developers Portal](https://developer.electrolux.one/dashboard) to get them.

### 3. Test with provided McpClient instead of AI Assistant ###
- Run: [McpClient](src/test/kotlin/com/ales/mcp/McpClient.kt)

## TODO
- [ ] Implement commands such as “Turn on the air purifier,” “If my living room hasn’t been cleaned for two days, clean it now,” etc.
- [ ] Implement Streamable HTTP transport (currently only stdio is supported)
- [ ] Try more assistants, not only Claude Desktop