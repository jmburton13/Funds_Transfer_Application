package com.techelevator.tenmo.jdbc.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.TransferHistoryDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferHistory;

@Component
public class JDBCTransferHistoryDAO implements TransferHistoryDAO {
	
private JdbcTemplate jt;
	
	public JDBCTransferHistoryDAO(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public List<TransferHistory> list(int id) {
		List<TransferHistory> list = new ArrayList<TransferHistory>();
		//get transfers where we received
		String sqlString = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_from JOIN users USING(user_id) JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses USING(transfer_status_id) WHERE transfers.account_to = ?;";
		
		SqlRowSet results = jt.queryForRowSet(sqlString, id);
		
		while(results.next()) {
			TransferHistory th = mapTransferHistoryFromRowSet(results);
			th.setUsername(results.getString("username"));
			list.add(th);
		}
		
		//get transfers where we sent
		sqlString = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_to JOIN users USING(user_id) JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses USING(transfer_status_id) WHERE transfers.account_from = ?";
		
		results = jt.queryForRowSet(sqlString, id);
		
		while(results.next()) {
			TransferHistory th = mapTransferHistoryFromRowSet(results);
			th.setUsername(results.getString("username"));
			list.add(th);
		}
		
		
		return list;
	}

	@Override
	public TransferHistory getById(int transferId) {
		TransferHistory th = new TransferHistory();
		
		String sqlString = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_from JOIN users USING(user_id) JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses USING(transfer_status_id) WHERE transfer_id = ?;";
		
		SqlRowSet results = jt.queryForRowSet(sqlString, transferId);
		
		while (results.next()) {
			th = mapTransferHistoryFromRowSet(results);
			th.setAccountFrom(results.getString("username"));
		}
		
		
		sqlString = "SELECT * FROM transfers JOIN accounts ON accounts.account_id = transfers.account_to JOIN users USING(user_id) JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses USING(transfer_status_id) WHERE transfer_id = ?;";
		results = jt.queryForRowSet(sqlString, transferId);
		
		while (results.next()) {
			th.setAccountTo(results.getString("username"));
		}
		return th;
	}
	
	private TransferHistory mapTransferHistoryFromRowSet(SqlRowSet result) {
		TransferHistory transferHistory = new TransferHistory();
		
		transferHistory.setTransferId(result.getInt("transfer_id"));
		transferHistory.setAmount(result.getDouble("amount"));
		transferHistory.setTransferType(result.getString("transfer_type_desc"));
		transferHistory.setTransferStatus(result.getString("transfer_status_desc"));
		
		return transferHistory;
	}

}
