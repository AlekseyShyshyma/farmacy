package com.khpi.farmacy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sold_in_period")
public class SoldInPeriod {

    @Id
    @GeneratedValue
    @Column(name = "sold_id")
    private long soldId;

    @NonNull
    @Column(name = "period_start")
    private Date periodStart;
    @Column(name = "period_end")
    private Date periodEnd;
    private double sum;
    private int amount;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drugstore_code", nullable = false)
    private Drugstore drugstore;

    //@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicine_code", nullable = false)
    private Medicine medicine;

    public SoldInPeriod(Long soldId, Date periodStart, Date periodEnd, double sum, int amount) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.sum = sum;
        this.amount = amount;
    }
}
