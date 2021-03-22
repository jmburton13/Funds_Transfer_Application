package com.techelevator.tenmo.jdbc.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO {
	
	private JdbcTemplate jt;
	
	public JDBCTransferDAO(JdbcTemplate jt) {
		this.jt = jt;
	}

	public Transfer create(Transfer transfer) {
		Transfer returnTransfer = new Transfer();
		
		String sqlString = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?);";
		
		jt.update(sqlString, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
		
		returnTransfer.setAccountFrom(transfer.getAccountFrom());
		returnTransfer.setAccountTo(transfer.getAccountTo());
		returnTransfer.setAmount(transfer.getAmount());
		returnTransfer.setTransferTypeId(transfer.getTransferTypeId());
		returnTransfer.setTransferId(transfer.getTransferId());
		returnTransfer.setTransferStatusId(transfer.getTransferStatusId());
		
		return returnTransfer;
	}

}
