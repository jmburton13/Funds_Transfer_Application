package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	List<Account> list();
	
	Account get(int id);
	
	void create();
	
	void update(int id, Account account);
	
	void delete(int id);

	
	
	

}
