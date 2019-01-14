package accolade.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import accolade.crud.PayloadFileStorage;
import accolade.exception.RecordNotFoundException;
import accolade.payload.PatchItem;
import accolade.payload.PayloadData;

@RestController
public class AccoladeController {

	private static final Logger logger = LoggerFactory.getLogger(AccoladeController.class);

	@Autowired
	private PayloadFileStorage storage;

	@RequestMapping(method = RequestMethod.POST, value="/record")
	@ResponseBody
	public PayloadData createRecord() {
		logger.debug("create endpoint called");
		PayloadData payload = storage.create();
		return payload;
	}

	@RequestMapping(method = RequestMethod.GET, value="/record/{recordId}")
	@ResponseBody
	public PayloadData getRecord(@PathVariable("recordId") UUID recordId) {
		logger.debug("get endpoint called");
		try {
			PayloadData payload = storage.get(recordId);
			return payload;
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		}
	}

	@RequestMapping(method = RequestMethod.PATCH, value="/record/{recordId}")
	@ResponseBody
	public PayloadData updateRecord(@PathVariable("recordId") UUID recordId, @RequestBody List<PatchItem> patchItems) {
		logger.debug("update endpoint called");
		try {
			PayloadData payload = storage.update(recordId, patchItems);
			return payload;
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value="/record/{recordId}")
	@ResponseBody
	public PayloadData deleteRecord(@PathVariable("recordId") UUID recordId) {
		logger.debug("delete endpoint called");
		try {
			PayloadData payload = storage.delete(recordId);
			return payload;
		} catch (RecordNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Record Not Found", e);
		}
	}
}