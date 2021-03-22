package com.techelevator.tenmo.dao;

import java.util.List;
import com.techelevator.tenmo.model.TransferHistory;

public interface TransferHistoryDAO {
	
	List<TransferHistory> list(int id);
	
	TransferHistory getById(int transferId);
	

}
