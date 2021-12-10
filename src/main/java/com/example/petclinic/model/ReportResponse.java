package com.example.petclinic.model;

import java.math.BigDecimal;
import java.util.List;

public class ReportResponse {

    List<TreatmentResponse> treatmentResponseList;
    BigDecimal totalCost;

    public List<TreatmentResponse> getTreatmentResponseList() {
        return treatmentResponseList;
    }

    public void setTreatmentResponseList(List<TreatmentResponse> treatmentResponseList) {
        this.treatmentResponseList = treatmentResponseList;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
