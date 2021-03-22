package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.jdbc.dao.JDBCAccountDAO;
import com.techelevator.tenmo.jdbc.dao.JDBCTransferDAO;
import com.techelevator.tenmo.jdbc.dao.JDBCTransferHistoryDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistory;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
public class ServiceController {
	
	private JDBCAccountDAO accountDAO;
	private JDBCTransferDAO transferDAO;
	private UserSqlDAO userDAO;
	private JDBCTransferHistoryDAO transferHistoryDAO;
	
	public ServiceController(JDBCAccountDAO accountDAO, JDBCTransferDAO transferDAO, UserSqlDAO userDAO, JDBCTransferHistoryDAO transferHistoryDAO) {
		this.accountDAO = accountDAO;
		this.transferDAO = transferDAO;
		this.userDAO = userDAO;
		this.transferHistoryDAO = transferHistoryDAO;
	}
	
	
	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
	public Account get(@PathVariable int id) {
		return accountDAO.get(id);
	}
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> list() {
		return userDAO.findAll();
	}
	
	@RequestMapping(path = "/accounts/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody Account account) {
		accountDAO.update(id, account);
	}
	
	@RequestMapping(path = "/transfers", method = RequestMethod.POST)
	public Transfer createTransfer(@RequestBody Transfer transfer) {
		return transferDAO.create(transfer);
	}
	
	@RequestMapping(path = "/accounts/{id}/transfers", method = RequestMethod.GET)
	public List<TransferHistory> getTransfers(@PathVariable int id){
		return transferHistoryDAO.list(id);
	}
	
	@RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
	public TransferHistory getTransferById(@PathVariable int transferId) {
		return transferHistoryDAO.getById(transferId);
	}

}
