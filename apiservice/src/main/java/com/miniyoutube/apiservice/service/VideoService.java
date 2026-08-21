package com.miniyoutube.apiservice.service;

import com.miniyoutube.apiservice.entity.KafkaData;
import com.miniyoutube.apiservice.entity.User;
import com.miniyoutube.apiservice.entity.Video;
import com.miniyoutube.apiservice.enums.VideoStatus;
import com.miniyoutube.apiservice.repository.VideoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class VideoService {


    private UserService userService;
    private VideoRepo videoRepo;
    private KafkaService kafkaService;

    public VideoService(UserService userService, VideoRepo videoRepo, KafkaService kafkaService) {
        this.userService = userService;
        this.videoRepo = videoRepo;
        this.kafkaService = kafkaService;
    }

    @Transactional
    public void saveVideo(
            MultipartFile file,
            String title,
            String description,
            String userName
    ) throws Exception {
        String UPLOAD_DIR = "D:/SpringBoot/Mini-Youtube/videos";

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetDir = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), targetDir, StandardCopyOption.REPLACE_EXISTING);

            Video newVideo = Video.builder()
                    .title(title)
                    .description(description)
                    .build();
            newVideo.setStatus(VideoStatus.PROCESSING);

            Video video = videoRepo.saveVideo(newVideo);
            User user = userService.getUser(userName);
            user.getVideos().add(video);
            userService.updateUser(user);
            kafkaService.send(
                    KafkaData.builder()
                            .videoId(video.getId())
                            .path(targetDir.toString())
                            .build()
            );
        } catch (Exception e) {
            log.error("Error while uploading video -> ", e);
            throw new Exception();
        }
    }

    public List<Video> getAllVideos(String userName) {
        return userService.getUser(userName).getVideos();
    }

}
