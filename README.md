# 🎬 Mini YouTube Backend

Backend-focused video streaming platform built with two independent Spring Boot applications.

> **Current scope:** Backend only. There is no frontend. APIs are tested with Swagger UI and Postman.

## Architecture

```text
Swagger / Postman
       |
       v
Video API Service (Spring Boot)
       |
   +---+---+---+
   |       |   |
MongoDB  Redis Kafka
               |
               v
      Video Worker (Spring Boot)
               |
             FFmpeg
               |
        +------+------+------+
        |      |      |
      360p   720p  1080p
        +------+------+------+
               |
              HLS
               |
               v
          Cloudinary
```

## End-to-End Workflow

1. Client uploads a video to `POST /api/videos`.
2. API validates the file.
3. API uploads the original to Cloudinary.
4. API stores metadata in MongoDB with `PROCESSING`.
5. API publishes a small processing message to Kafka.
6. API returns the video ID without waiting for transcoding.
7. Worker consumes the Kafka message.
8. Worker downloads the original temporarily.
9. FFmpeg creates multiple qualities, HLS assets and a thumbnail.
10. Worker uploads processed assets to Cloudinary.
11. Worker updates MongoDB to `READY` or `FAILED`.
12. API returns the HLS URL and video metadata.

The actual video binary is **not sent through Kafka**.

## Technologies

| Technology | Purpose |
|---|---|
| Java | Primary language |
| Spring Boot | Backend framework |
| MongoDB | Video metadata |
| Redis | Caching |
| Apache Kafka | Asynchronous processing |
| FFmpeg | Video processing |
| HLS | Streaming format |
| Cloudinary | Persistent media storage/delivery |
| Swagger/OpenAPI | API testing/documentation |
| JUnit + Mockito | Testing |
| Docker | Containerization |

## Repository Structure

```text
mini-youtube-backend/
├── video-api/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── video-worker/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── docs/
├── docker-compose.yml
├── .gitignore
└── README.md
```

## Main API

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/videos` | Upload video |
| GET | `/api/videos` | Get videos |
| GET | `/api/videos/{id}` | Get video details |
| GET | `/api/videos/{id}/status` | Get processing status |
| DELETE | `/api/videos/{id}` | Delete video |

## Redis Flow

```text
GET /api/videos/{id}
        |
        v
      Redis
      /       HIT   MISS
     |      |
  return  MongoDB
            |
            v
          Redis
            |
            v
          return
```

## Environment Variables

Never commit secrets.

```env
MONGODB_URI=
REDIS_HOST=
REDIS_PORT=
KAFKA_BOOTSTRAP_SERVERS=
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
```

## Testing Without a Frontend

```text
Swagger / Postman
       |
POST /api/videos
       |
       v
Video API
  |    |    |
  |    |    +--> Kafka
  |    +-------> MongoDB
  +------------> Cloudinary
                   |
                   v
              Video Worker
                   |
                 FFmpeg
                   |
                   v
                  HLS
                   |
                   v
              Cloudinary
```

## Docker

The API and worker are independently deployable. The worker should contain FFmpeg in its runtime image.

## Future Improvements
- Search and pagination
- Likes/comments/subscriptions
- View counting
- Kafka retries and Dead Letter Topic
- Rate limiting
- Monitoring
- Distributed tracing
- Recommendations

## Project Goal

Demonstrate backend engineering concepts including REST APIs, asynchronous processing, Kafka, MongoDB, Redis, FFmpeg, HLS, cloud media storage, testing and service separation.
