package accolade.payload;

public class PatchItem {
	private PatchOperation op;
	private String path;
	private String value;

	public PatchOperation getOp() {
		return op;
	}

	public String getPath() {
		return path;
	}

	public String getValue() {
		return value;
	}
}