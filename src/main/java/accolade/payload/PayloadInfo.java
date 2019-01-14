package accolade.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayloadInfo {
	private RecordStatus recordStatus;
	private Date created;
	private List<Date> updated;
	private Date deleted;
	private String recordData;

	public PayloadInfo() {
		recordStatus = RecordStatus.NEW;
		updated = new ArrayList<>();
		created = new Date();
	}

	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Date getCreated() {
		return created;
	}

	public List<Date> getUpdated() {
		return updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public String getRecordData() {
		return recordData;
	}

	public void setRecodData(String recordData) {
		this.recordData = recordData;
	}
}
