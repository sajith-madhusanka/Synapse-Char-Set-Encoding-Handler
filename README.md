# CharacterSetEncodingHandler

The `CharacterSetEncodingHandler` is a custom mediator designed to ensure the correct character set encoding is determined and set in the Axis2 message context for incoming messages. It includes a fallback mechanism to handle cases where the encoding is not explicitly provided in the message.

## Purpose

The `CharacterSetEncodingHandler` retrieves and sets the character set encoding (`CHARACTER_SET_ENCODING`) in the Axis2 message context. This is crucial for ensuring proper processing of messages based on their encoding.

### How It Works

The Synapse handler:

1. Checks the `CHARACTER_SET_ENCODING` property in the Axis2 message context.
2. If not found, attempts to retrieve it from the operation context.
3. If still not found, retrieves it from the `XML_DECLARATION_ENCODING` property.
4. Defaults to the Axis2 `DEFAULT_CHAR_SET_ENCODING` if no other encoding is found.

---

## Engaging the Synapse Handler

1. Add the following configuration to the `deployment.toml` file:
   ```toml
   [synapse_handlers.CharacterSetEncodingHandler]
   enabled = true
   class = "org.wso2.com.sample.CharacterSetEncodingHandler"
   ```

   ## Steps to Engage

1. **Build the Synapse Handler**  
   Execute the `mvn clean install` command to build the Synapse handler.

2. **Deploy the JAR File**  
   Deploy the built JAR file (`CharSetEncodingSynapseHandler-1.0.0-SNAPSHOT.jar`) to the `MI_HOME/lib` directory.

3. **Restart the Server**  
   Restart the server to apply the changes.

---

## Configuring Debug Logs

1. **Add Logger Configuration**  
   Add a logger configuration for `CharacterSetEncodingHandler` in the `<APIM_HOME>/repository/conf/log4j2.properties` file:
   ```
   logger.CharacterSetEncodingHandler.name = org.wso2.com.sample.CharacterSetEncodingHandler
   logger.CharacterSetEncodingHandler.level = DEBUG
   ```

2. **Update the Loggers Section**  
   Append the logger name `CharacterSetEncodingHandler` to the loggers section in the configuration file.
   ```
   loggers = CharacterSetEncodingHandler, ...
   ```

---

## Sample Debug Log Lines

When debug logging is enabled, detailed logs provide insights into how the character set encoding is determined and set. Example logs:

```
TID: [-1234] [] [2025-01-12 10:15:30,456] DEBUG {org.wso2.com.sample.CharacterSetEncodingHandler} - Starting handleRequestInFlow for message context: urn:uuid:12345
TID: [-1234] [] [2025-01-12 10:15:30,457] DEBUG {org.wso2.com.sample.CharacterSetEncodingHandler} - CHARACTER_SET_ENCODING not found in the Axis2 Message Context
TID: [-1234] [] [2025-01-12 10:15:30,458] DEBUG {org.wso2.com.sample.CharacterSetEncodingHandler} - CHARACTER_SET_ENCODING retrieved from operation context: UTF-8
TID: [-1234] [] [2025-01-12 10:15:30,459] DEBUG {org.wso2.com.sample.CharacterSetEncodingHandler} - CHARACTER_SET_ENCODING set in Axis2 message context: UTF-8
TID: [-1234] [] [2025-01-12 10:15:30,460] DEBUG {org.wso2.com.sample.CharacterSetEncodingHandler} - Completed handleRequestInFlow for message context: urn:uuid:12345

```

---

## Key Benefits

- **Accurate Encoding Processing**: Ensures that messages are processed with the correct encoding.
- **Robust Fallback Mechanisms**: Provides multiple methods to detect and set encoding.
- **Detailed Debug Logs**: Facilitates easy troubleshooting and debugging.

This Synapse Handler is particularly useful in scenarios with diverse message payloads, where encoding specifications may vary or be missing.

