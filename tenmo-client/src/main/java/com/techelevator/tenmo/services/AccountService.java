package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferHistory;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class AccountService {

	private static String AUTH_TOKEN;
	private final String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	private AuthenticatedUser currentUser;
	private Scanner scanner = new Scanner(System.in);
	
	public AccountService(String url, AuthenticatedUser user){
		BASE_URL = url;
		currentUser = user;
		AUTH_TOKEN = user.getToken();
	}
	//end point baseurl/users
	public User[] listUsers(){
		User[] users = null;
		
		try {
			users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
			for(User user : users) {
				System.out.println("Username: " + user.getUsername() + " User Id: " + user.getId());
			
			
			}
			
		} catch (Exception e) {
			throw e;
		} return users;
	}
	
	//end point is BASE_URL/accounts/{id}
	public Account getAccount(int id) {
		try {
			return restTemplate.exchange(BASE_URL + "accounts/" + id, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
		} catch (Exception e) {
			throw e;
		}
	}
	
	//end point is BASE_URL/accounts/{id}
	public double viewCurrentBalance() throws Exception  {

		try {
			Account account = restTemplate.exchange(BASE_URL + "accounts/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
			double balance = account.getBalance();
			System.out.println("Your balance is: " + balance);
		
			return balance;
		} catch (Exception e) {
			throw e;
		}
	}
//		} catch (RestClientResponseException ex) {
//			throw new RestClientResponseException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
//		}

	//end point BASE_URL/accounts/{id}/transfers
	public void viewTransferHistory() {
		
		try {
			TransferHistory[] transfers = restTemplate.exchange(BASE_URL + "accounts/" + currentUser.getUser().getId() + "/transfers", HttpMethod.GET, makeAuthEntity(), TransferHistory[].class).getBody();
			
			for(TransferHistory transfer : transfers) {
				int id = transfer.getTransferId();
				String username = transfer.getUsername();
				double amount = transfer.getAmount();
				
				System.out.println("Transfer ID: " + id + " From/To: " + username + " Amount: " + amount);
				
			}
			System.out.println("\n");
			System.out.println("\n");
			System.out.println("Which Transfer ID would you like to inspect?: ");
			
			int transferId = scanner.nextInt();
			viewTransferHistoryById(transferId);
			
		} catch (Exception e) {
			System.out.println("*** Invalid Input ***");
			scanner.nextLine();
		}
	}
	
	
	//end point BASE_URL/transfers/{transferId}
		public void viewTransferHistoryById(int transferId) {
			try {
				TransferHistory transfer = restTemplate.exchange(BASE_URL + "transfers/" + transferId, HttpMethod.GET, makeAuthEntity(), TransferHistory.class).getBody();
								
					int id = transfer.getTransferId();
					String accountFrom = transfer.getAccountFrom();
					String accountTo = transfer.getAccountTo();
					String transferType = transfer.getTransferType();
					String transferStatus = transfer.getTransferStatus();
					double amount = transfer.getAmount();
					
					System.out.println("Transfer ID: " + id);
					System.out.println("From: " + accountFrom);
					System.out.println("To: " + accountTo);
					System.out.println("Type: " + transferType);
					System.out.println("Status: " + transferStatus);
					System.out.println("Amount: " + amount);
				
			} catch (Exception e) {
				throw e;
			}
			
			
			}
			
		
	
	//CHALLENGE QUESTION
	public void viewPendingRequests() {
		//GET transfers WHERE tranfer_status_id = 1
		
	}
	//end point is BASE_URL/accounts/{id}/send
	public void sendBucks() throws Exception {
		User[] users = null;
		
		users = listUsers();
		
		int targetId = -1;
		double amount = 0;
		boolean validId = false;
		boolean validAmount = false;
		
		while (validId == false) {
			System.out.println("Enter the Id of the User you would like to send to: ");
			
			try {
				
				targetId = scanner.nextInt();
				
			
				for(User user : users) {
					if (user.getId() == targetId) {
					validId = true;
					}		
				}
				
		} catch (Exception e) {
			System.out.println("*** Invalid Input ***");
			scanner.nextLine();
		}
		}
		
		while (validAmount == false) {
			double amountCap = viewCurrentBalance();
			
			System.out.println("Enter the amount you would like to send: ");
			
			try {
				amount = scanner.nextDouble();
			} catch (Exception e) {
				System.out.println("*** Invalid Input ***");
				scanner.nextLine();
			}
	
			if (amount <= amountCap && amount > 0) {
				validAmount = true;
			}
		}
		
//		boolean transactionComplete = false;
//		
//		while (transactionComplete = false) {
		
			try {
				//create a transfer POST
				Transfer transfer = new Transfer();
				
				transfer.setAccountFrom(currentUser.getUser().getId());
				transfer.setAccountTo(targetId);
				transfer.setAmount(amount);
				transfer.setTransferStatusId(2);
				transfer.setTransferTypeId(2);
				
				HttpEntity<Transfer> transferEntity = makeTransferEntity(transfer);
				
				Transfer returnTransfer = restTemplate.postForObject(BASE_URL + "transfers", transferEntity, Transfer.class);
				
				
				System.out.println("You sent " + returnTransfer.getAmount() + " to User Id: " + returnTransfer.getAccountTo());
				
				
				
				
				//withdraw from user PUT
				Account newUserAccount = new Account();
				
				double newUserBalance = getAccount(currentUser.getUser().getId()).getBalance() - amount;
				
				newUserAccount.setAccountId(currentUser.getUser().getId());
				newUserAccount.setUserId(currentUser.getUser().getId());
				newUserAccount.setBalance(newUserBalance);
				
				HttpEntity<Account> accountEntity = makeAccountEntity(newUserAccount);
				
				restTemplate.put(BASE_URL + "accounts/" + currentUser.getUser().getId(), accountEntity, HttpMethod.PUT);
				
				//add to target PUT
				
				Account newTargetAccount = new Account();
				
				double newTargetBalance = (getAccount(targetId).getBalance() + amount);
				
				newTargetAccount.setAccountId(targetId);
				newTargetAccount.setUserId(targetId);
				newTargetAccount.setBalance(newTargetBalance);
				
				accountEntity = makeAccountEntity(newTargetAccount);
			
				restTemplate.put(BASE_URL + "accounts/" + targetId, accountEntity, HttpMethod.PUT);
			
//				transactionComplete = true;
				
			} catch (Exception e) {
				System.out.println("\n");
				System.out.println("***Something went wrong. ***");
				
			}
		}
//	}
	
	
	//CHALLENGE QUESTION
	public void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}
	
	private HttpEntity<Account> makeAccountEntity(Account account){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<Account> entity = new HttpEntity<>(account, headers);
		return entity;
	}
	
	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
	}
}
