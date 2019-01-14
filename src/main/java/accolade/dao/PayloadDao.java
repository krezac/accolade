package accolade.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import accolade.exception.InvalidDataException;
import accolade.exception.RecordNotFoundException;
import accolade.payload.PayloadData;

/** data access layer */
public class PayloadDao {

	private String fileName;
	private Map<UUID, PayloadData> storage;

	private static final Logger logger = LoggerFactory.getLogger(PayloadDao.class);


	public PayloadDao(String fileName) {
		logger.info("creating dao for data file " + fileName);
		this.fileName = fileName;
		this.storage = new HashMap<>();

		loadData();
	}

	public PayloadData get(UUID id) throws RecordNotFoundException {
		logger.info("getting record for id " + id.toString());
		PayloadData data = storage.get(id);
		if (data == null) {
			logger.error("record not found for id ", id.toString());
			throw new RecordNotFoundException(id);
		}
		return data;
	}

	public void save(PayloadData data) throws InvalidDataException {
		logger.info("saving data");
		if (data == null) {
			logger.error("null data");
			throw new InvalidDataException("null data");
		} else if (data.getRecordId() == null) {
			logger.error("null recordId");
			throw new InvalidDataException("null recordId");
		}

		storage.put(data.getRecordId(), data);
		saveData();
	}


	// helpers

	private void loadData() {
		logger.info("reading from " + fileName);
		int loadedCount = 0;

		try {
			try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8)) {
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
		logger.info("saving to " + fileName);
		int savedCount = 0;
		try {
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardCharsets.UTF_8)) {
				ObjectMapper objectMapper = new ObjectMapper();
				for (PayloadData pd : storage.values()) {
					String line = objectMapper.writeValueAsString(pd);
					writer.append(line);
					writer.append("\n");
					savedCount++;
				}
			}
		} catch (Exception e) {
			logger.error("save failed:" + e.getMessage(), e);
		}
		logger.info("saved records: " + savedCount);
	}

}