package com.khpi.farmacy.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
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
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

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
}
