package com.yz.oj.service;

import com.yz.oj.mapper.ExperimentSubmissionMapper;
import com.yz.oj.mapper.StudentAccountMapper;
import com.yz.oj.mapper.SubmissionMediaMapper;
import com.yz.oj.entity.ExperimentSubmission;
import com.yz.oj.entity.SubmissionMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExperimentSubmissionService {

    @Autowired
    private ExperimentSubmissionMapper submissionMapper;

    @Autowired
    private SubmissionMediaMapper mediaMapper;

    @Autowired
    private StudentAccountMapper studentAccountMapper;

    @Autowired
    private FileStorageService fileStorageService;

    public ExperimentSubmission submitExperiment(ExperimentSubmission submission) {

        submission.setSubmittedAt(java.time.LocalDateTime.now());
        submissionMapper.insertExperimentSubmission(submission);
        return submission;
    }

    public void updateSubmission(ExperimentSubmission submission) {
        submissionMapper.updateExperimentSubmission(submission);
    }

    // 删除某个提交记录
    public void deleteSubmission(long id) {
        // 先删除提交的媒体文件
        mediaMapper.deleteMediaBySubmissionId(id);
        // 再删除提交记录
        submissionMapper.deleteExperimentSubmission(id);
    }

    // 删除某个实验的所有提交记录
    public void deleteSubmissionByExperimentId(long id) {
        mediaMapper.deleteMediaByExperimentId(id);
        submissionMapper.deleteSubmissionsByExperimentId(id);
    }


    public List<ExperimentSubmission> getSubmissionsByStudentId(long studentId) {
        return submissionMapper.selectSubmissionsByStudentId(studentId);
    }

    public List<ExperimentSubmission> getSubmissionsByExperimentId(long experimentId) {
        return submissionMapper.selectSubmissionsByExperimentId(experimentId);
    }

    public ExperimentSubmission getSubmissionById(long id) {
        return submissionMapper.selectSubmissionById(id);
    }

    public void addMediaToSubmission(SubmissionMedia media) {
        mediaMapper.insertSubmissionMedia(media);
    }

    public List<SubmissionMedia> getMediaBySubmissionId(long submissionId) {
        return mediaMapper.selectMediaBySubmissionId(submissionId);
    }

    public String uploadMediaFile(MultipartFile file) throws IOException {
        return fileStorageService.storeFile(file);
    }

    public long getStudentIdByUsername(String username) {
        return studentAccountMapper.selectStudentIdByUsername(username);
    }

    public void deleteSubmissionMediaByMediaId(long mediaId) {
        mediaMapper.deleteSubmissionMedia(mediaId);
    }


}
