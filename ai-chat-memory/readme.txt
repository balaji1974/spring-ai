docker pull postgres
docker run --name postgres-chat-memory -e POSTGRES_PASSWORD=secret -e POSTGRES_USER=myuser -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres
