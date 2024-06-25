package com.yz.oj.controller;

import com.yz.oj.entity.ExperimentSubmission;
import com.yz.oj.entity.SubmissionMedia;
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
@RequestMapping("/api/submissions")
public class ExperimentSubmissionController {

    @Autowired
    private ExperimentSubmissionService submissionService;

    @PostMapping
    public ResponseEntity<?> submitExperiment(@RequestBody ExperimentSubmissionRequest submissionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String username = authentication.getName();
        long studentId = submissionService.getStudentIdByUsername(username);

        ExperimentSubmission submission = new ExperimentSubmission();
        submission.setStudentId(studentId);
        submission.setExperimentId(submissionRequest.getExperimentId());
        submission.setAnswer(submissionRequest.getAnswer());

        submission = submissionService.submitExperiment(submission);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperimentSubmission> getSubmissionById(@PathVariable long id) {
        ExperimentSubmission submission = submissionService.getSubmissionById(id);
        System.out.println(submission);
        return ResponseEntity.ok(submission);
    }

    @PutMapping("/{id}")
    public void updateSubmission(@PathVariable long id, @RequestBody ExperimentSubmission submission) {
        submission.setId(id);
        submissionService.updateSubmission(submission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubmission(@PathVariable long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId}")
    public List<ExperimentSubmission> getSubmissionsByStudentId(@PathVariable long studentId) {
        return submissionService.getSubmissionsByStudentId(studentId);
    }

    @GetMapping("/experiment/{experimentId}")
    public List<ExperimentSubmission> getSubmissionsByExperimentId(@PathVariable long experimentId) {
        return submissionService.getSubmissionsByExperimentId(experimentId);
    }

    @PostMapping("/{submissionId}/media")
    public void addMediaToSubmission(@PathVariable long submissionId, @RequestBody SubmissionMediaRequest mediaRequest) {

        SubmissionMedia media = new SubmissionMedia();
        media.setSubmissionId(submissionId);
        media.setMediaUrl(mediaRequest.getUrl());
        media.setMediaType(mediaRequest.getType());

        submissionService.addMediaToSubmission(media);
    }

    @GetMapping("/{submissionId}/media")
    public List<SubmissionMedia> getMediaBySubmissionId(@PathVariable long submissionId) {
        return submissionService.getMediaBySubmissionId(submissionId);
    }

    @DeleteMapping("/media/{mediaId}")
    public void deleteSubmissionMedia(@PathVariable long mediaId) {
        submissionService.deleteSubmissionMediaByMediaId(mediaId);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return submissionService.uploadMediaFile(file);
    }

    @Data
    public static class ExperimentSubmissionRequest {
        private long studentId;
        private long experimentId;
        private String answer;
    }
    @Data
    public static class SubmissionMediaRequest {
        private String url;
        private String type;
    }


}
