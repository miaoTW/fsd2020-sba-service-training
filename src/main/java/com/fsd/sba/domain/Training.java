package com.fsd.sba.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * A Training.
 */
@Entity
@Table(name = "training")
public class Training implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "progress")
    private Integer progress;

    @Column(name = "fees")
    private Float fees;

    @Column(name = "commission_amount")
    private Float commissionAmount;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "amount_received")
    private Float amountReceived;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "mentor_id")
    private Long mentorId;

    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Training status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public Training progress(Integer progress) {
        this.progress = progress;
        return this;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Float getFees() {
        return fees;
    }

    public Training fees(Float fees) {
        this.fees = fees;
        return this;
    }

    public void setFees(Float fees) {
        this.fees = fees;
    }

    public Float getCommissionAmount() {
        return commissionAmount;
    }

    public Training commissionAmount(Float commissionAmount) {
        this.commissionAmount = commissionAmount;
        return this;
    }

    public void setCommissionAmount(Float commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getRating() {
        return rating;
    }

    public Training rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Training startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Training endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public Training startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public Training endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Float getAmountReceived() {
        return amountReceived;
    }

    public Training amountReceived(Float amountReceived) {
        this.amountReceived = amountReceived;
        return this;
    }

    public void setAmountReceived(Float amountReceived) {
        this.amountReceived = amountReceived;
    }

    public Long getUserId() {
        return userId;
    }

    public Training userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public Training mentorId(Long mentorId) {
        this.mentorId = mentorId;
        return this;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public Training skillId(Long skillId) {
        this.skillId = skillId;
        return this;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public Training razorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
        return this;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Training)) {
            return false;
        }
        return id != null && id.equals(((Training) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Training{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", progress=" + getProgress() +
            ", fees=" + getFees() +
            ", commissionAmount=" + getCommissionAmount() +
            ", rating=" + getRating() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", amountReceived=" + getAmountReceived() +
            ", userId=" + getUserId() +
            ", mentorId=" + getMentorId() +
            ", skillId=" + getSkillId() +
            ", razorpayPaymentId='" + getRazorpayPaymentId() + "'" +
            "}";
    }
}
