# Chat Storage Microservice

Spring Boot microservice for storing and managing chat messages with ELK stack integration.

## Project Structureadd


- **Controllers**: `ChatMessageController`, `ChatSessionController`
- **Services**: `ChatMessageService`, `ChatSessionService`, `OpenAIService`
- **Entities**: `ChatMessage`, `ChatSession`, `User`
- **DTOs**: Request/Response objects for API operations

## Technology Stack

- Java 17
- Spring Boot 3.x
- MySQL 8.0 (database)
- Elasticsearch (search/analytics)
- Logstash + Kibana (logging)
- Docker + Docker Compose

## Docker Services

- `chat-storage-ms`: Spring Boot application (port 8080)
- `mysql`: MySQL database (port 3306)
- `elasticsearch`: Elasticsearch (port 9200)
- `logstash`: Logstash (port 5044, 9600)
- `kibana`: Kibana (port 5601)

## Running the Application

```bash
docker compose up -d
```

## API Endpoints

- `POST /api/chat-messages` - Send a chat message
- `GET /api/chat-messages/{sessionId}` - Get messages by session
- `GET /api/chat-messages/{sessionId}/search` - Search messages
- `POST /api/chat-sessions` - Create a chat session
- `GET /api/chat-sessions/{id}` - Get a chat session
