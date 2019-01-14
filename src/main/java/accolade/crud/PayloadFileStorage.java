package accolade.crud;

import java.awt.dnd.InvalidDnDOperationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import accolade.exception.RecordNotFoundException;
import accolade.payload.PatchItem;
import accolade.payload.PatchOperation;
import accolade.payload.PayloadData;
import accolade.payload.RecordStatus;

@Service
public class PayloadFileStorage implements PayloadInterface {
	
	// TODO doesn't seem to work @Value("${dataFile}")
	private String dataFile = "data.dat";
	private HashMap<UUID, PayloadData> storage = new HashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(PayloadFileStorage.class);

	public PayloadFileStorage() throws IOException {
		loadData();
	}

	private void loadData() {
		logger.info("reading from " + dataFile);
		int loadedCount = 0;

		try {
			FileReader fr = new FileReader(dataFile);
			try (BufferedReader br = new BufferedReader(fr)) {
				String line;
				ObjectMapper objectMapper = new ObjectMapper();
				while ((line = br.readLine()) != null) {
					PayloadData data = objectMapper.readValue(line, PayloadData.class);
					storage.put(data.getRecordId(), data);	// TODO handle duplicate id
					loadedCount++;
				}
			}	
		} catch(FileNotFoundException e) {
			logger.info("data file not found", e.getMessage());
		} catch (IOException e) {
			logger.error("data read error", e);
		}
		logger.info("loaded records: " + loadedCount);
	}

	private void saveData() {
		logger.info("saving to " + dataFile);
		try {
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(dataFile), 
			StandardCharsets.UTF_8)) {
				ObjectMapper objectMapper = new ObjectMapper();
				for (PayloadData pd : storage.values()) {
					String line = objectMapper.writeValueAsString(pd);
					writer.append(line);
					writer.append("\n");
				}
			}
		} catch (Exception e) {
			logger.error("save failed:" + e.getMessage(), e);
		}
	}

	private void recordUpdate(PayloadData data) {
		data.getInfo().getUpdated().add(new Date());
		data.getInfo().setRecordStatus(RecordStatus.UPDATED);
	}

	private void recordDelete(PayloadData data) {
		data.getInfo().setDeleted(new Date());
		data.getInfo().setRecordStatus(RecordStatus.DELETED);
	}

	private void applyPatch(PayloadData data, PatchItem patch) {
		if (patch.getOp() != PatchOperation.replace || !"/info/recordData".equals(patch.getPath()) ) {
			throw new InvalidDnDOperationException();
		}

		data.getInfo().setRecodData(patch.getValue());
	}

	@Override
	public PayloadData create() {
		PayloadData data = new PayloadData();
		storage.put(data.getRecordId(), data);
		saveData();
		return data;
	}

	@Override
	public PayloadData get(UUID recordId)  throws RecordNotFoundException {
		PayloadData data = storage.get(recordId);
		if (data == null || data.getInfo().getRecordStatus() == RecordStatus.DELETED) { // TODO option to retrieve deleted?
			throw new RecordNotFoundException();
		}
		return data;
	}

	@Override
	public PayloadData update(UUID recordId, List<PatchItem> patchItems) throws RecordNotFoundException {
		PayloadData data = storage.get(recordId);
		if (data == null) {
			throw new RecordNotFoundException();
		}

		logger.info ("applying patches: " + patchItems.size());
		for (PatchItem item : patchItems) {
			applyPatch(data, item);
		}
		recordUpdate(data);
		saveData();
		return data;
	}

	@Override
	public PayloadData delete(UUID recordId)  throws RecordNotFoundException {
		PayloadData data = storage.get(recordId);
		if (data == null) {
			throw new RecordNotFoundException();
		}

		recordDelete(data);
		saveData();
		return data;
	}
}