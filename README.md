Chat Storage Microservice (chat-storage-ms)

Chat Storage MS is a Spring Boot microservice to store chat sessions and messages for a RAG-based chatbot system. This microservice uses MySQL as the database and can be containerized using Docker for production-ready deployments.

## Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                              External Clients                                   │
│                    (Mobile App, Web App, Other Services)                        │
│                              Port: 8080                                         │
└────────────────────────────────────┬───────────────────────────────────────────┘
                                     │ HTTPS/REST
                                     ▼
┌────────────────────────────────────────────────────────────────────────────────┐
│                    chat-storage-ms (Spring Boot)                               │
│  ┌──────────────────────────────────────────────────────────────────────────┐  │
│  │ Controllers        Services           Entities          Config           │  │
│  │ - ChatMessage    - ChatMessage      - ChatMessage    - OpenAI Client    │  │
│  │ - ChatSession    - ChatSession      - ChatSession    - API Key Filter   │  │
│  │                  - OpenAI Service   - User           - MySQL Config     │  │
│  └──────────────────────────────────────────────────────────────────────────┘  │
│                                     │                                          │
│    ┌────────────────────────────────┼────────────────────────────────────┐   │
│    │                                ▼                                    │   │
│    │                     writes logs to /app/logs                        │   │
│    │                                │                                    │   │
│    │                                ▼ (Docker Volume: app-logs)        │   │
│    │                    ┌────────────────────────┐                       │   │
│    │                    │   Shared Log Volume    │                       │   │
│    │                    │   (app-logs)           │                       │   │
│    │                    └───────────┬────────────┘                       │   │
│    └────────────────────────────────┼────────────────────────────────────┘   │
└────────────────────────────────────┼───────────────────────────────────────────┘
                                     │
         ┌───────────────────────────┬┴───────────────────────────┐
         │                           │                           │
         ▼                           │                           ▼
┌─────────────────────┐              │              ┌─────────────────────────┐
│       MySQL         │              │              │       OpenAI API        │
│   (chat_db)         │              │              │   (GPT Models)          │
│   Port: 3306        │              │              │   https://api.openai.com│
│                     │              │              └─────────────────────────┘
└─────────────────────┘              │
                                     │ reads from volume
                                     ▼
                         ┌─────────────────────┐
                         │      Logstash      │
                         │  (Log Processor)   │
                         │    Port: 5044      │
                         └──────────┬──────────┘
                                    │
                                    │ writes indexed logs
                                    ▼
                         ┌─────────────────────┐
                         │   Elasticsearch     │
                         │  (Index/Store)     │
                         │    Port: 9200       │
                         └──────────┬──────────┘
                                    │
                                    │ reads data
                                    ▼
                         ┌─────────────────────┐
                         │      Kibana         │
                         │  (Visualize Logs)  │
                         │    Port: 5601       │
                         └─────────────────────┘
```

## Features 
- Store chat sessions and messages in a MySQL database
- Compatible with OpenAI GPT models (3.5 or 4 variants).
- Maintain conversation history with user and AI messages.
- Dockerized for easy deployment
- Configurable via application properties
- Basic error handling and logging

## Prerequisites
- Java 21
- Maven
- MySQL database
- Docker
- OpenAI API Key (if integrating with OpenAI models)

## Environment Variables in .env File
- `DB_HOST_NAME`: JDBC HOST for MySQL database (e.g., `jdbc:mysql://localhost:3306/chatdb`)
- `DB_PORT`: JDBC PORT for MySQL database (e.g., `3306`)
- `DB_NAME`: Name of the MySQL database (e.g., `chatdb`)
- `DB_USER_NAME`: MySQL database username
- `DB_PASSWORD`: MySQL database password
- `OPENAI_URL`: OpenAI API URL (e.g., `https://api.openai.com/v1/`)
- `OPENAI_API_KEY`: OpenAI API Key for authentication
- `API_KEY`: API Key for securing endpoints

## Getting Started
### Clone the Repository
git clone https://github.com/Dhanushma/chat-storage-ms.git 
### Build the application :
cd chat-storage-ms
mvn clean install
### Run the application locally :
java -jar target/chat-storage-ms-0.0.1-SNAPSHOT.jar
### Dockerize the application :
docker build -t <your-username>/chat-storage-ms:latest .
OR
### Run the Docker container :
docker pull <your-username>/chat-storage-ms:latest 
docker run --env-file .env -p 8080:8080 kddhan/chat-storage-ms:latest


