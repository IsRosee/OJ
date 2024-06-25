package com.yz.oj.service;

import com.yz.oj.entity.ExperimentSubmission;
import com.yz.oj.mapper.ExperimentMapper;
import com.yz.oj.mapper.ExperimentMediaMapper;
import com.yz.oj.entity.Experiment;
import com.yz.oj.entity.ExperimentMedia;
import com.yz.oj.mapper.ExperimentSubmissionMapper;
import com.yz.oj.mapper.TeacherAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExperimentService {

    @Autowired
    private ExperimentMapper experimentMapper;

    @Autowired
    private ExperimentSubmissionMapper submissionMapper;

    @Autowired
    private ExperimentMediaMapper experimentMediaMapper;

    @Autowired
    private ExperimentSubmissionService submissionService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private TeacherAccountMapper teacherAccountMapper;

    // 返回的响应包含实验ID
    public Experiment createExperiment(Experiment experiment) {
        experiment.setCreatedAt(java.time.LocalDateTime.now());
        experiment.setUpdatedAt(java.time.LocalDateTime.now());
        experimentMapper.insertExperiment(experiment);
        return experiment;
    }

    public void updateExperiment(Experiment experiment) {
        experiment.setUpdatedAt(java.time.LocalDateTime.now());
        experimentMapper.updateExperiment(experiment);
    }

    public void deleteExperiment(long id) {
        // 先通过实验ID删除提交记录和提交的媒体文件
        submissionService.deleteSubmissionByExperimentId(id);
        // 再删除实验媒体
        experimentMediaMapper.deleteMediaByExperimentId(id);
        // 最后删除实验
        experimentMapper.deleteExperiment(id);
    }
    public Experiment getExperimentById(long id) {
        return experimentMapper.selectExperimentById(id);
    }

    public void addMediaToExperiment(ExperimentMedia media) {
        experimentMediaMapper.insertExperimentMedia(media);
    }

    public List<ExperimentMedia> getMediaByExperimentId(long experimentId) {
        return experimentMediaMapper.selectMediaByExperimentId(experimentId);
    }

    public String uploadMediaFile(MultipartFile file) throws IOException {
        // 返回的是file/开头的可获取URL，不是在后端服务器上的文件路径
        return fileStorageService.storeFile(file);
    }

    public List<Experiment> findAll() {
        return experimentMapper.selectAll();
    }


    public Long getTeacherIdByUsername(String username) {
        return teacherAccountMapper.findTeacherIdByUsername(username);
    }

    public List<Experiment> findnoAll(long userId) {
        return experimentMapper.selectUnsubmittedExperimentsByUserId(userId);
    }

    public List<Experiment> findokAll(long userId) {
        return experimentMapper.selectSubmittedExperimentsByUserId(userId);

    }

    public void deleteMediaFromExperiment(long id) {
        experimentMediaMapper.deleteMediaByExperimentId(id);
    }
}
