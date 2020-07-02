package com.wequan.bu.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.WebhookEndpoint;
import com.wequan.bu.repository.model.Appointment;
import com.wequan.bu.repository.model.TutorStripe;

public interface StripeService extends Service<TutorStripe> {
    public void storeConnectedId(String code, Integer tutorId);

    public PaymentIntent createPaymentIntent(Integer appointmentId) throws StripeException;

    public String retrieveClientSecret(Integer appointmentId) throws StripeException, Exception;

    public void handlePaymentIntent(String sigHeader, String webhookEndpoint) throws Exception;

    public PaymentIntent cancelPaymentIntent(String paymentIntentId) throws StripeException;

    public PaymentIntent updatePaymentIntent(Integer appointmentId) throws StripeException;

    public Refund createRefund(String transactionId, Integer refundAmount) throws StripeException;

    public void handleRefund(String sigHeader, String webhookEndpoint) throws Exception;
}
