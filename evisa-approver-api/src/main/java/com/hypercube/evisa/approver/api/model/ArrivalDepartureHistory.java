package com.hypercube.evisa.approver.api.model;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "tevi_arr_dep_history")
public class ArrivalDepartureHistory {
    @Id

    @Column(name = "id")
    private Long id;

    @Column(name = "arr_dep_id")
    private Integer arr_dep_id;

    @Column(name = "action_date")
    private Timestamp action_date;

    @Column(name = "application_number")
    private String application_number;

    @Column(name = "approver")
    private String approver;

    @Column(name = "approver_role")
    private String approver_role;

    @Column(name = "opr_type")
    private String opr_type;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private String status;


    public ArrivalDepartureHistory() {
    }

    public ArrivalDepartureHistory(Long id, Integer arr_dep_id, Timestamp action_date, String application_number, String approver, String approver_role, String opr_type, String remarks, String status) {
        this.id = id;
        this.arr_dep_id = arr_dep_id;
        this.action_date = action_date;
        this.application_number = application_number;
        this.approver = approver;
        this.approver_role = approver_role;
        this.opr_type = opr_type;
        this.remarks = remarks;
        this.status = status;
    }

    public ArrivalDepartureHistory(Integer arr_dep_id, Timestamp action_date, String application_number, String approver, String approver_role, String opr_type, String remarks, String status) {
        this.arr_dep_id = arr_dep_id;
        this.action_date = action_date;
        this.application_number = application_number;
        this.approver = approver;
        this.approver_role = approver_role;
        this.opr_type = opr_type;
        this.remarks = remarks;
        this.status = status;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getArr_dep_id() {
        return this.arr_dep_id;
    }

    public void setArr_dep_id(Integer arr_dep_id) {
        this.arr_dep_id = arr_dep_id;
    }

    public Timestamp getAction_date() {
        return this.action_date;
    }

    public void setAction_date(Timestamp action_date) {
        this.action_date = action_date;
    }

    public String getApplication_number() {
        return this.application_number;
    }

    public void setApplication_number(String application_number) {
        this.application_number = application_number;
    }

    public String getApprover() {
        return this.approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApprover_role() {
        return this.approver_role;
    }

    public void setApprover_role(String approver_role) {
        this.approver_role = approver_role;
    }

    public String getOpr_type() {
        return this.opr_type;
    }

    public void setOpr_type(String opr_type) {
        this.opr_type = opr_type;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

    @Override
    public String toString() {
        return "{" +
            ", arr_dep_id='" + getArr_dep_id() + "'" +
            ", action_date='" + getAction_date() + "'" +
            ", status='" + getStatus() + "'" +
            ", approver='" + getStatus() + "'" +
            "}";
    }

}
