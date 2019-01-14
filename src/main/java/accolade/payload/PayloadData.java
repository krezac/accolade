package accolade.payload;

import java.util.UUID;

/** response data for the endpoints */
public class PayloadData {
	private UUID recordId;
	private PayloadInfo info;

	/** to create new item in live code */
	public PayloadData() {
		this.recordId = UUID.randomUUID();
		this.info = new PayloadInfo();
	}

	/** for testing */
	public PayloadData(UUID recordId, PayloadInfo info) {
		this.recordId = recordId;
		this.info = info;
	}

	public UUID getRecordId() {
		return recordId;
	}

	public PayloadInfo getInfo() {
		return info;
	}
}