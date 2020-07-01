package com.wequan.bu.service.impl;

import com.wequan.bu.controller.vo.Transaction;
import com.wequan.bu.repository.dao.AppointmentChangeRecordMapper;
import com.wequan.bu.repository.model.Appointment;
import com.wequan.bu.repository.model.AppointmentChangeRecord;
import com.wequan.bu.service.AbstractService;
import com.wequan.bu.service.AppointmentChangeRecordService;
import com.wequan.bu.service.AppointmentService;
import com.wequan.bu.service.TransactionService;
import com.wequan.bu.util.ChangeType;
import com.wequan.bu.util.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * @author Zhaochao Huang
 */
@Service
public class AppointmentChangeRecordServiceImpl extends AbstractService<AppointmentChangeRecord> implements AppointmentChangeRecordService {
    @Autowired
    private AppointmentChangeRecordMapper appointmentChangeRecordMapper;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TransactionService transactionService;

    @PostConstruct
    public void postConstruct(){
        this.setMapper(appointmentChangeRecordMapper);
    }


    @Override
    public void addRecordByTutor(String transactionId, Integer tutorId) {
        Appointment appointment = appointmentService.findByTransactionId(transactionId);
        Transaction transaction = transactionService.findById(transactionId);

        if(appointment != null && transaction != null) {
            AppointmentChangeRecord record = new AppointmentChangeRecord();

            record.setAppointmentId(appointment.getId());
            record.setUserId(transaction.getPayTo());
            record.setIsTutor(true);
            record.setTransactionId(transactionId);
            record.setCreateTime(LocalDateTime.now());
            if(transaction.getStatus().equals(TransactionStatus.REQUIRES_PAYMENT_METHOD.getValue())){
                record.setChangeType(ChangeType.BEFORE_PAYMENT.getValue());
            }else if(transaction.getStatus().equals(TransactionStatus.SUCCEEDED.getValue())){
                if(appointment.getStartTime().isAfter(LocalDateTime.now())){
                    record.setChangeType(ChangeType.BEFORE_APPOINTMENT.getValue());
                }else{
                    record.setChangeType(ChangeType.AFTER_APPOINTMENT.getValue());
                }
                record.setRefundAmount(appointment.getFee());
            }
            appointmentChangeRecordMapper.insertSelective(record);
        }
    }

    @Override
    public void addRecordByUser(String transactionId, Integer userId, Integer refundAmount) {
        Appointment appointment = appointmentService.findByTransactionId(transactionId);
        Transaction transaction = transactionService.findById(transactionId);

        if(appointment != null && transaction != null) {
            AppointmentChangeRecord record = new AppointmentChangeRecord();

            record.setAppointmentId(appointment.getId());
            record.setUserId(userId);
            record.setIsTutor(false);
            record.setTransactionId(transactionId);
            record.setCreateTime(LocalDateTime.now());
            if(transaction.getStatus().equals(TransactionStatus.REQUIRES_PAYMENT_METHOD.getValue())){
                record.setChangeType(ChangeType.BEFORE_PAYMENT.getValue());
            }else if(transaction.getStatus().equals(TransactionStatus.SUCCEEDED.getValue())){
                record.setChangeType(ChangeType.BEFORE_APPOINTMENT.getValue());
                record.setRefundAmount(refundAmount);
            }
            appointmentChangeRecordMapper.insertSelective(record);
        }
    }
}
