package com.techelevator.tenmo.dao;

import java.util.List;


import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	
	List<Transfer> list();
	
	Transfer get();
	
	Transfer create(Transfer transfer);
	
	void update();
	
	void delete();
	
	
	

}
