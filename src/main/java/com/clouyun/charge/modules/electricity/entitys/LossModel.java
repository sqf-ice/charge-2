package com.clouyun.charge.modules.electricity.entitys;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


public class LossModel implements Serializable {


	public Integer getLmId() {
		return lmId;
	}

	public void setLmId(Integer lmId) {
		this.lmId = lmId;
	}

	public String getLmName() {
		return lmName;
	}

	public void setLmName(String lmName) {
		this.lmName = lmName;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getLmType() {
		return lmType;
	}

	public void setLmType(Integer lmType) {
		this.lmType = lmType;
	}

	public String getLmNo() {
		return lmNo;
	}

	public void setLmNo(String lmNo) {
		this.lmNo = lmNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public String getLmCzzb() {
		return lmCzzb;
	}

	public void setLmCzzb(String lmCzzb) {
		this.lmCzzb = lmCzzb;
	}

	public String getLmCdss() {
		return lmCdss;
	}

	public void setLmCdss(String lmCdss) {
		this.lmCdss = lmCdss;
	}

	public String getLmSjcd() {
		return lmSjcd;
	}

	public void setLmSjcd(String lmSjcd) {
		this.lmSjcd = lmSjcd;
	}

	public String getLmQtyd() {
		return lmQtyd;
	}

	public void setLmQtyd(String lmQtyd) {
		this.lmQtyd = lmQtyd;
	}


	public LossModel(Integer lmId, String lmName, Integer orgId,
			Integer stationId, Integer lmType, String lmNo,
			Timestamp createDate, Timestamp updateDate) {
		this.lmId = lmId;
		this.lmName = lmName;
		this.orgId = orgId;
		this.stationId = stationId;
		this.lmType = lmType;
		this.lmNo = lmNo;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public LossModel() {
	}

	private static final long serialVersionUID = 1L;
	private Integer lmId;
	private String lmName;
	private Integer orgId;
	private Integer stationId;
	private Integer lmType;
	private String lmNo;
	private Date createDate;
	private Date updateDate;
	private String lmCzzb;
	private String lmCdss;
	private String lmSjcd;
	private String lmQtyd;

}
