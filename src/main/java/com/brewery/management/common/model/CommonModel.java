package com.brewery.management.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * This class is base model that must be extended by every model in the project;
 * it contains the frequently used fields by the project beans.
 * 
 * @author Wadah Abushora
 * 
 * 
 */

@MappedSuperclass
public abstract class CommonModel implements Serializable {

	private static final long serialVersionUID = 7238055550254241600L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long systemId;
	@Version
	@Column(name = "version")
	private int version;
	private String status;
	private String notes;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date dateCreated;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date dateUpdated;

	protected CommonModel() {
		super();
		init();
	}

	@PrePersist
	protected void onCreate() {
		dateCreated = new Date();

	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = new Date();
	}

	protected abstract void init();

	protected abstract boolean isValid();

	protected abstract void clean();

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonModel other = (CommonModel) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateUpdated == null) {
			if (other.dateUpdated != null)
				return false;
		} else if (!dateUpdated.equals(other.dateUpdated))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (systemId == null) {
			if (other.systemId != null)
				return false;
		} else if (!systemId.equals(other.systemId))
			return false;
		if (version != other.version)
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "CommonModel [systemId=" + systemId + ", version=" + version + ", status=" + status + ", notes=" + notes
				+ ", dateCreated=" + dateCreated + ", dateUpdated=" + dateUpdated + "]";
	}

}
