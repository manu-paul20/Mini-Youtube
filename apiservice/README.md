# 🎬 Video API Service

The Video API Service is the REST-facing Spring Boot application.

## Responsibilities

- REST APIs
- Video upload and validation
- Cloudinary upload of original media
- MongoDB metadata
- Redis caching
- Kafka producer
- Swagger/OpenAPI
- Exception handling

## Upload Flow

```text
Client
  |
  | POST /api/videos
  v
Controller
  |
  v
Service
  |
  +--> Validate
  +--> Cloudinary
  +--> MongoDB
  +--> Kafka
  |
  v
202 Accepted
```

## Endpoints

### Upload

```http
POST /api/videos
Content-Type: multipart/form-data
```

Fields:

```text
file
title
description
```

Response:

```json
{
  "videoId": "abc123",
  "status": "PROCESSING"
}
```

### List Videos

```http
GET /api/videos
```

### Get Video

```http
GET /api/videos/{id}
```

Example:

```json
{
  "id": "abc123",
  "title": "My Video",
  "status": "READY",
  "hlsUrl": "https://...",
  "thumbnailUrl": "https://...",
  "qualities": ["360p", "720p", "1080p"]
}
```

### Processing Status

```http
GET /api/videos/{id}/status
```

### Delete

```http
DELETE /api/videos/{id}
```

## MongoDB Document

```json
{
  "_id": "abc123",
  "title": "My Video",
  "description": "Test video",
  "status": "PROCESSING",
  "originalPublicId": "mini-youtube/videos/abc123",
  "hlsPublicId": null,
  "thumbnailUrl": null,
  "qualities": [],
  "views": 0,
  "createdAt": "...",
  "updatedAt": "..."
}
```

## Redis

Example cache key:

```text
video:abc123
```

Cache frequently requested video details and invalidate/update entries when the underlying metadata changes.

## Kafka Producer

Topic:

```text
video-processing
```

Message:

```json
{
  "videoId": "abc123",
  "originalPublicId": "mini-youtube/videos/abc123"
}
```

The API never puts the large video binary into Kafka.

## Package Structure

```text
src/main/java/com/example/videoapi/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── kafka/
├── config/
└── exception/
```

Suggested classes:

```text
VideoController
VideoService
StorageService
CacheService
KafkaProducerService
VideoRepository
Video
UploadResponse
VideoResponse
ProcessingStatusResponse
VideoProcessingMessage
GlobalExceptionHandler
```

## Configuration

```env
MONGODB_URI=
REDIS_HOST=
REDIS_PORT=
KAFKA_BOOTSTRAP_SERVERS=
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
```

## Running

```bash
mvn spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Docker

```bash
docker build -t video-api .
docker run video-api
```

## Design Rule

The API accepts and queues work. It does **not** wait for FFmpeg.

```text
API
 ↓
Cloudinary
 ↓
MongoDB
 ↓
Kafka
 ↓
202 Accepted

Worker
 ↓
FFmpeg
 ↓
READY
```
