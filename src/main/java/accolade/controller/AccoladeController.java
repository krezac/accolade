package accolade.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.dnd.InvalidDnDOperationException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import accolade.dao.PayloadDao;
import accolade.exception.InvalidDataException;
import accolade.exception.RecordNotFoundException;
import accolade.payload.PatchItem;
import accolade.payload.PatchOperation;
import accolade.payload.PayloadData;
import accolade.payload.RecordStatus;

/** Endpoints class */
@RestController
public class AccoladeController {

	private static final Logger logger = LoggerFactory.getLogger(AccoladeController.class);

	@Autowired
	private PayloadDao dao;

	/** Create new record */
	@RequestMapping(method = RequestMethod.POST, value="/record")
	@ResponseBody
	public PayloadData createRecord() {
		logger.info("create endpoint called");
		PayloadData data = new PayloadData();
		try {
			dao.save(data);
		} catch (InvalidDataException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create new record", e);
		}
		return data;
	}

	/** get record by id */
	@RequestMapping(method = RequestMethod.GET, value="/record/{recordId}")
	@ResponseBody
	public PayloadData getRecord(@PathVariable("recordId") UUID recordId) {
		logger.info("get endpoint called");
		try {
			PayloadData data = dao.get(recordId);
			return data;	// TODO return deleted data?
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		}
	}

	/** Update record */
	@RequestMapping(method = RequestMethod.PATCH, value="/record/{recordId}")
	@ResponseBody
	public PayloadData updateRecord(@PathVariable("recordId") UUID recordId, @RequestBody List<PatchItem> patchItems) {
		logger.info("update endpoint called");
		try {
			PayloadData data = dao.get(recordId);
			logger.info ("applying patches: " + patchItems.size());
			for (PatchItem item : patchItems) {
				applyPatch(data, item);
			}	
			recordUpdate(data);
			dao.save(data);
			return data;
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		} catch (InvalidDataException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to save updated record", e);
		}
	}

	/** mark record as deleted */
	@RequestMapping(method = RequestMethod.DELETE, value="/record/{recordId}")
	@ResponseBody
	public PayloadData deleteRecord(@PathVariable("recordId") UUID recordId) {
		logger.info("delete endpoint called");
		try {
			PayloadData data = dao.get(recordId);
			recordDelete(data);
			dao.save(data);
			return data;
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		} catch (InvalidDataException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to save deleted record", e);
		}
	}

	// helpers
	private void recordUpdate(PayloadData data) {
		data.getInfo().getUpdated().add(new Date());
		data.getInfo().setRecordStatus(RecordStatus.UPDATED);
	}

	private void recordDelete(PayloadData data) {
		data.getInfo().setDeleted(new Date());
		data.getInfo().setRecordStatus(RecordStatus.DELETED);
	}

	private void applyPatch(PayloadData data, PatchItem patch) {
		// TODO this is shortcut as patching another items doesn't make much sense
		if (patch.getOp() != PatchOperation.replace || !"/info/recordData".equals(patch.getPath()) ) {
			throw new InvalidDnDOperationException();
		}

		data.getInfo().setRecodData(patch.getValue());
	}

}