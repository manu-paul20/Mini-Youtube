# 🎞️ Video Processing Worker

The Video Processing Worker is the second Spring Boot application. It consumes Kafka jobs and performs CPU-intensive video processing with FFmpeg.

## Responsibilities

- Kafka consumer
- Temporary local processing files
- FFmpeg transcoding
- 360p / 720p / 1080p generation
- HLS generation
- Thumbnail generation
- Cloudinary upload
- MongoDB status updates
- Failure handling

## Architecture

```text
Kafka
  |
  v
Worker
  |
  v
Cloudinary
  |
  | download original
  v
Temporary local file
  |
  v
FFmpeg
  |
  +--> 360p
  +--> 720p
  +--> 1080p
  +--> HLS
  +--> Thumbnail
  |
  v
Cloudinary
  |
  v
MongoDB
READY / FAILED
```

## Kafka Consumer

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

## Processing Flow

```text
Kafka message
      |
      v
Read videoId
      |
      v
Find MongoDB document
      |
      v
Download original
      |
      v
Temporary local directory
      |
      v
FFmpeg
      |
      +--> 360p
      +--> 720p
      +--> 1080p
      +--> HLS
      +--> Thumbnail
      |
      v
Upload outputs to Cloudinary
      |
      v
Update MongoDB
      |
      v
READY
```

## Temporary Storage

Local disk is only a processing workspace:

```text
/tmp/video-processing/
└── abc123/
    ├── original.mp4
    ├── 360p.mp4
    ├── 720p.mp4
    ├── 1080p.mp4
    └── hls/
```

After processing, temporary files should be deleted.

**Permanent media storage:** Cloudinary.

## HLS Output

```text
videos/abc123/
├── master.m3u8
├── 360p/
│   ├── playlist.m3u8
│   └── segments
├── 720p/
│   ├── playlist.m3u8
│   └── segments
└── 1080p/
    ├── playlist.m3u8
    └── segments
```

## MongoDB Status

Processing starts:

```json
{ "status": "PROCESSING" }
```

Success:

```json
{
  "status": "READY",
  "hlsPublicId": "...",
  "thumbnailUrl": "...",
  "qualities": ["360p", "720p", "1080p"]
}
```

Failure:

```json
{ "status": "FAILED" }
```

## Package Structure

```text
src/main/java/com/example/videoworker/
├── kafka/
├── service/
├── ffmpeg/
├── repository/
├── entity/
└── config/
```

Suggested classes:

```text
VideoKafkaConsumer
VideoProcessingMessage
VideoProcessingService
StorageService
FFmpegService
VideoRepository
Video
KafkaConfig
MongoConfig
CloudinaryConfig
```

## Failure Handling

```text
Kafka
  |
  v
Worker
  |
FFmpeg
  |
  X
Error
  |
  v
MongoDB → FAILED
```

Future improvements:

- Kafka retries
- Exponential backoff
- Dead Letter Topic
- Retry counter
- Processing timeout

## Testing

A Kafka message can be published manually:

```json
{
  "videoId": "abc123",
  "originalPublicId": "mini-youtube/videos/abc123"
}
```

Expected logs:

```text
Received processing job: abc123
Downloading video...
Starting FFmpeg...
Generating 360p...
Generating 720p...
Generating 1080p...
Generating HLS...
Uploading output...
Processing completed.
```

Then verify:

```text
MongoDB → status = READY
Cloudinary → processed assets exist
```

## Configuration

```env
MONGODB_URI=
KAFKA_BOOTSTRAP_SERVERS=
KAFKA_CONSUMER_GROUP=
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=
```

## Running

```bash
mvn spring-boot:run
```

## Docker

The worker is a strong candidate for Docker because FFmpeg must exist inside its runtime environment.

The image should contain:

```text
Java
Spring Boot application
FFmpeg
```

Build:

```bash
docker build -t video-worker .
```

Run:

```bash
docker run video-worker
```

## Design Rule

The worker performs processing asynchronously. The API never waits for FFmpeg.

```text
API → Kafka → 202 Accepted

Worker → FFmpeg → READY
```
