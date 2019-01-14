package accolade.payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PayloadInfo {

	final String dateFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

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

	/** for testing */
	public PayloadInfo(RecordStatus recordStatus, Date created, List<Date> updated, Date deleted, String recordData) {
		this.recordStatus = recordStatus;
		this.created = created;
		this.updated = updated;
		this.deleted = deleted;
		this.recordData = recordData;
	}

	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = dateFormatPattern)
	public Date getCreated() {
		return created;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = dateFormatPattern)
	public List<Date> getUpdated() {
		return updated;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = dateFormatPattern)
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
