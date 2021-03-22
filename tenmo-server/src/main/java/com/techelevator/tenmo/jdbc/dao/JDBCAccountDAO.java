package com.techelevator.tenmo.jdbc.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;

@Component
public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jt;
	
	
	public JDBCAccountDAO(JdbcTemplate jt) {
		this.jt = jt;
	}

	@Override
	public List<Account> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account get(int id) {
		Account account = null;
		String sqlString = "SELECT * FROM accounts WHERE account_id = ?;";
		
		SqlRowSet result = jt.queryForRowSet(sqlString, id);
		
		if(result.next()) {
			account = mapAccountFromRowSet(result);
		}
		return account;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int id, Account account) {
		String sqlString = "UPDATE ACCOUNTS SET balance = ? WHERE account_id = ?;";
		
		jt.update(sqlString, account.getBalance(), account.getAccountId());
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		
	}
	
	private Account mapAccountFromRowSet(SqlRowSet result) {
		Account account = new Account();
		
		account.setAccountId(result.getInt("account_id"));
		account.setUserId(result.getInt("user_id"));
		account.setBalance(result.getDouble("balance"));
		
		return account;
	}

}
