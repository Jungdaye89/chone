package com.chone.server.domains.payment.service;

import com.chone.server.domains.payment.domain.Payment;

public class PaymentCancelledEvent {
    private final Payment payment;

    public PaymentCancelledEvent(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}