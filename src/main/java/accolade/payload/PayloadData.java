package accolade.payload;

import java.util.UUID;

public class PayloadData {
	private UUID recordId;
	private PayloadInfo info;

	public PayloadData() {
		this.recordId = UUID.randomUUID();
		this.info = new PayloadInfo();
	}

	public PayloadData(UUID recordId) {
		this.recordId = recordId;
		this.info = new PayloadInfo();
	}

	public UUID getRecordId() {
		return recordId;
	}

	public PayloadInfo getInfo() {
		return info;
	}
}