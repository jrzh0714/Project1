package com.ex.ers.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.sql.Timestamp;

@Entity
@Table(name = "ers_requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="req_id")
    protected int req_id;

    private Integer requestor_id;

    @Column(columnDefinition = "DOUBLE")
    protected float amount;

    @Column(columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    protected Timestamp submitted_date;

    protected String status;

    protected int resolver_id;

    @Column(columnDefinition = "TEXT")
    protected String description;

    public Integer getRequestor_id() {
        return requestor_id;
    }

    public void setRequestor_id(Integer requestor_id) {
        this.requestor_id = requestor_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReq_id() {
        return req_id;
    }

    public void setReq_id(int req_id) {
        this.req_id = req_id;
    }



    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Timestamp getSubmitted_date() {
        return submitted_date;
    }

    public void setSubmitted_date(Timestamp submitted_date) {
        this.submitted_date = submitted_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(int resolver_id) {
        this.resolver_id = resolver_id;
    }

    @Override
    public String toString() {
        return "Request{" +
                "req_id=" + req_id +
                ", amount=" + amount +
                ", submitted_date=" + submitted_date +
                ", status='" + status + '\'' +
                ", resolver_id=" + resolver_id +
                ", description='" + description + '\'' +
                '}';
    }


    @Converter(autoApply = true)
    public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

        @Override
        public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
            return locDateTime == null ? null : Timestamp.valueOf(locDateTime);
        }

        @Override
        public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
            return sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime();
        }
    }
}
