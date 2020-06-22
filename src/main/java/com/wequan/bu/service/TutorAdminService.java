package com.wequan.bu.service;

import com.wequan.bu.controller.vo.TutorApplicationVo;
import com.wequan.bu.controller.vo.UploadFileWrapper;
import com.wequan.bu.repository.model.TutorApplication;
import com.wequan.bu.repository.model.extend.TutorApplicationFullInfo;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhaochao Huang
 */
public interface TutorAdminService extends Service<TutorApplication> {

    public void apply(TutorApplicationVo tutorApplicationVo, List<UploadFileWrapper> uploadFileWrapperList) throws IOException;

    public List<UploadFileWrapper> bufferUploadFile(TutorApplicationVo tutorApplicationVo) throws IOException;

    public List<TutorApplicationFullInfo> findByUserId(Integer userId);

    public void update(TutorApplicationVo tutorApplicationVo, List<UploadFileWrapper> uploadFileWrapperList) throws IOException;

    public List<TutorApplication> findStatusByUserId(Integer userId);
}
