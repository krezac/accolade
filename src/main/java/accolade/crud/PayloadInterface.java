package accolade.crud;

import java.util.List;
import java.util.UUID;

import accolade.exception.RecordNotFoundException;
import accolade.payload.PatchItem;
import accolade.payload.PayloadData;

interface PayloadInterface {
	
	PayloadData create();
	PayloadData get(UUID recordId) throws RecordNotFoundException;
	PayloadData update(UUID recordId, List<PatchItem> patchItems) throws RecordNotFoundException;
	PayloadData delete(UUID recordId) throws RecordNotFoundException;
}