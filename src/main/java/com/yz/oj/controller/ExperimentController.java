package com.yz.oj.controller;


import com.yz.oj.entity.Experiment;
import com.yz.oj.entity.ExperimentMedia;
import com.yz.oj.service.ExperimentService;
import com.yz.oj.service.ExperimentSubmissionService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private ExperimentSubmissionService submissionService;

    @PostMapping
    public ResponseEntity<?> createExperiment(@RequestBody ExperimentRequest experimentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String username = authentication.getName();
        long teacherId = 0;
        try{
            teacherId = experimentService.getTeacherIdByUsername(username);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("非教师用户无法创建实验");
        }

        Experiment experiment = new Experiment();
        experiment.setTeacherId(teacherId);
        experiment.setTitle(experimentRequest.getTitle());
        experiment.setContent(experimentRequest.getContent());
        experiment.setStatus(experimentRequest.getStatus());

        experiment = experimentService.createExperiment(experiment);
        // 返回的响应为json格式
        return ResponseEntity.ok(experiment);
    }

    @GetMapping
    public ResponseEntity<List<Experiment>> getExperiments() {
        List<Experiment> experiments = experimentService.findAll();
        return ResponseEntity.ok(experiments);
    }

    // 某用户已完成的实验
    @GetMapping("/ok/{userid}")
    public ResponseEntity<List<Experiment>> getOkExperiments(@PathVariable long userid) {
        List<Experiment> experiments = experimentService.findokAll(userid);
        return ResponseEntity.ok(experiments);
    }

    // 某用户未完成的实验
    @GetMapping("/no/{userid}")
    public ResponseEntity<List<Experiment>> getNoExperiments(@PathVariable long userid) {
        List<Experiment> experiments = experimentService.findnoAll(userid);
        return ResponseEntity.ok(experiments);
    }

    @PutMapping("/{id}")
    public void updateExperiment(@PathVariable long id, @RequestBody Experiment experiment) {
        experiment.setId(id);
        experimentService.updateExperiment(experiment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExperiment(@PathVariable long id) {
        experimentService.deleteExperiment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Experiment getExperiment(@PathVariable long id) {
        return experimentService.getExperimentById(id);
    }

    @PostMapping("/{id}/media")
    public void addMediaToExperiment(@PathVariable long id, @RequestBody ExperimentMediaRequest media) {
        ExperimentMedia experimentMedia = new ExperimentMedia();
        experimentMedia.setExperimentId(id);
        experimentMedia.setMediaUrl(media.getUrl());
        experimentMedia.setMediaType(media.getType());
        experimentService.addMediaToExperiment(experimentMedia);
    }

    @DeleteMapping("/{id}/media")
    public void deleteMediaFromExperiment(@PathVariable long id) {
        experimentService.deleteMediaFromExperiment(id);
    }

    @GetMapping("/{id}/media")
    public List<ExperimentMedia> getMediaByExperimentId(@PathVariable long id) {
        return experimentService.getMediaByExperimentId(id);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return experimentService.uploadMediaFile(file);
    }

    @Data
    public static class ExperimentRequest {
        private String title;
        private String content;
        private String status;
    }

    @Data
    public static class ExperimentMediaRequest {
        private String url;
        private String type;
    }

}
