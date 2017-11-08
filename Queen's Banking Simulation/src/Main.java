//This program does the following:
//	- loops until user enters 'login'
//	- loops until user enters a mode, either 'machine' or 'agent'
//	- goes through a continuous loop of getting user command, and
//	calling the appropriate method to handle the command
//	- program returns if user enters input command 'logout'

import java.util.ArrayList;
import java.util.Scanner;

public class Main{
	
	//global variables - login mode, valid accounts list, list of transactions
	public static String mode;
	public static int sum;
	public static ArrayList<String> tsf = new ArrayList<String>();
	static ArrayList<String> accounts = new ArrayList<String>();
	public static Scanner scan = new Scanner(System.in);
	
	//main method for processing user input and output to console
	public static ArrayList<String> Activity(ArrayList<String> accountList) {
		
		//local variables
		boolean flag = false;
		String input = null;
		accounts = accountList;
		
		//check for login, keeps looping until user enters 'login'
		while(!flag) {
			System.out.println("Please enter 'login'.");
			input = userInput();
			if (input.matches("login")) {
				flag = true;
			}
			else {
				System.out.println("Error: You did not enter login.");
			}
		}	
		
		flag = false;
		
		//check for mode, keeps looping until user enters 'machine' or 'agent'
		while(!flag) {
			System.out.println("Please enter mode ('machine' or 'agent').");
			input = userInput();
			if (!input.matches("machine") && !input.matches("agent")) {
				System.out.println("Error: You did not enter a mode.");
			} else {
				//sets mode to user's input
				mode = input;
				flag = true;
			}
		}
		
		//this loop continuously asks for the user to input a command and processes those commands
		//keeps looping until user enters 'logout'
		while(true) {
			System.out.println("What would you like to do? ('createacct', 'deleteacct', 'deposit', 'withdraw', 'transfer', 'logout')");
			input = userInput();
			
			//createacct
			if(input.matches("createacct")) {
				
				//check if user is in agent mode
				//if yes, call create account method
				if (mode.matches("agent")) {
					createAccount();
				} else {
					System.out.println("Error: Must create an account in agent mode.");
				}
			}
			
			//deleteacct
			else if (input.matches("deleteacct")) {
				
				//check if user is in agent mode
				//if yes, call delete account method
				if (mode.matches("agent")) {
					deleteAccount();
				} else {
					System.out.println("Error: Must delete an account in agent mode.");
				}
			}
			
			//deposit
			else if (input.matches("deposit")) {
				Deposit();
			}
			
			//withdraw
			else if (input.matches("withdraw")) {
				Withdraw();
			}
			
			//transfer
			else if (input.matches	("transfer")) {
				Transfer();
			}
			
			//logout
			else if (input.matches("logout")) {
				System.out.println("You have successfully logged out.");
				//returns a list of all successful transactions to Test.java
				return tsf;
			}
			
			//default
			else {
				System.out.println("Error: Illegal Action.");
			}
			
			//clear input
			input = null;
		}
	}
	
	//method to create a new account
	public static void createAccount() {
		
		//initialize variables
		String number = null;
		String name = null;
		
		//get account number
		System.out.println("Please enter an account number.");
		number = userInput();
			
		//check for valid user input
		if(!checkAccountNumber(number)) {
			return;
		}
		
		//check if account already exists
		if(verifyAccount(number)) {
			System.out.println("Error: Account already exists.");
			return;
		}
		
		//get account name
		System.out.println("Please enter an account name.");
		name = userInput();
			
		//check if user input is valid
		if(!checkAccountName(name)){
			return;
		}
		
		//add to transaction summary file
		tsf.add("NEW " + number + " 000 0000000 " + name);
		
		//add to valid accounts list
		accounts.add(number);
		System.out.println("Account created.");
		return;
	}
	
	//method to delete existing account
	public static void deleteAccount() {
		
		//initialize variables
		String number = null;
		String name = null;
		
		//get account number
		System.out.println("Please enter an account number.");
		number = userInput();
			
		//check if user input is valid
		if(!checkAccountNumber(number)) {
			return;
		}
		
		//check if account exists
		if(!verifyAccount(number)) {
			System.out.println("Error: Account number does not exist.");
			return;
		}
		
		
		//get account name
		System.out.println("Please enter an account name.");
		name = userInput();
			
		//check if user input is valid
		if(!checkAccountName(name)) {
			return;
		}
		
		//add to transaction summary file
		tsf.add("DEL " + number + " 000 0000000 " + name);
		
		//remove from valid accounts list
		accounts.remove(number);
		System.out.println("Account deleted.");
		return;
	}
	
	//method to perform deposit
	public static void Deposit() {
		
		//initialize variables
		String action = "deposit";
		String number = null;
		String amount = null;
		
		//get account number
		System.out.println("Please enter an account number.");
		number = userInput();
			
		//check if user input is valid
		if(!checkAccountNumber(number)) {
			return;
		}
	
		//check if account exists
		if(!verifyAccount(number)) {
			System.out.println("Error: Account number does not exist.");
			return;
		}
		
		//get amount to deposit
		System.out.println("Please enter the amount of money to deposit.");
		amount = userInput();
			
		//check if user input is valid
		if(!checkAmount(amount, action)) {
			return;
		}
		
		//add to transaction summary file
		tsf.add("DEP " + number + " " + amount + " 0000000 ***");
		System.out.println("Deposit completed.");
		return;
	}
	
	//method to perform withdraw
	public static void Withdraw() {
		
		//get account number and amount to deposit
		String action = "withdraw";
		String number = null;
		String amount = null;
		
		//get to account number
		System.out.println("Please enter an account number.");
		number = userInput();
			
		//check if user input is valid
		if(!checkAccountNumber(number)) {
			return;	
		}
		
		//check if account exists
		if(!verifyAccount(number)) {
			System.out.println("Error: Account number does not exist.");
			return;
		}
		
		//get amount to withdraw
		System.out.println("Please enter the amount of money to withdraw.");
		amount = userInput();
		sum += Integer.parseInt(amount);
			
		if(!checkAmount(amount, action)) {
			sum -= Integer.parseInt(amount);
			return;
		}
		
		//add to transaction summary file
		tsf.add("WDR " + number + " " + amount + " 0000000 ***");
		System.out.println("Withdraw completed.");
		return;
	}
	
	public static void Transfer() {
		//initialize variables
		String action = "transfer";
		String fromAccount = null;
		String toAccount = null;
		String amount = null;
		
		//get from account number
		System.out.println("Please enter the 'from' account number.");
		fromAccount = userInput();
			
		//check if user input is valid
		if(!checkAccountNumber(fromAccount)) {
			return;
		}
		
		//check if account exists
		if(!verifyAccount(fromAccount)) {
			System.out.println("Error: Account number does not exist.");
			return;
		}
		
		
		//get to account number
		System.out.println("Please enter the 'to' account number.");
		toAccount = userInput();
			
		//check if user input is valid
		if(!checkAccountNumber(toAccount)) {
			return;
		}
		
		//check if account exists
		if(!verifyAccount(toAccount)) {
			System.out.println("Error: Account number does not exist.");
			return;
		}
		
		//get amount to transfer
		System.out.println("Please enter the amount of money to transfer.");
		amount = userInput();
			
		//check if user input is valid
		if(!checkAmount(amount, action)) {
			return;
		}
		
		//add to transaction summary file
		tsf.add("XFR " + toAccount + " " + amount + " " + fromAccount + " ***");
		System.out.println("Transfer completed.");
		return;
	}
	
	//method to scan for user input
	public static String userInput() {
		String user = "";
		user = scan.nextLine();
		//return scanned input
		return user;
	}
	
	//method to check if user input for account number is valid
	public static boolean checkAccountNumber(String number) {
		
		//check if null
		if(number.isEmpty()) {
			System.out.println("Error: No account number was entered.");
			return false;
		}
		
		//check if number of digits < 7
		if(number.length() < 7) {
			System.out.println("Error: The account number is too short.");
			return false;
		}
		
		//check if number of digits is > 7
		else if(number.length() > 7){
			System.out.println("Error: The account number is too long.");
			return false;
		}
		else {
			
			//for first digits, check for invalid character or if it is 0
			//if either are true, return false
			if(!Character.isDigit(number.charAt(0)) || number.charAt(0) == '0'){
				System.out.println("Error: An account number must only contain numbers and cannot begin with a '0'.");
				return false;
			}
			
			//check rest of the digits for invalid character
			//if true, return false
			for(int i = 1; i < number.length(); i++) {
				if(!Character.isDigit(number.charAt(i))){
					System.out.println("Error: An account number must only contain numbers.");
					return false;
				}
			}
		}
		
		//otherwise, return true
		return true;
	}
	
	//method to check if user inputs valid account name
	public static boolean checkAccountName(String name) {
		
		//check if null
		if(name.isEmpty()) {
			System.out.println("Error: No account name was entered.");
			return false;
		}
		
		//check if name has amount of characters > 30
		else if(name.length() < 3) {
			System.out.println("Error: An account name must have 3 or more characters.");
			return false;
		} else if(name.length() > 30) {
			System.out.println("Error: An account name must have less than 30 characters.");
			return false;
		}
		else {
			
			//check if any character in input is invalid
			//if yes, return false
			for(int i = 0; i < name.length(); i++) {
				if(!Character.isLetterOrDigit(name.charAt(i))) {
					System.out.println("Error: An account name must only contain letters and/or numbers.");
					return false;
				}
			}
		}
		
		//otherwise, return truth
		return true;
	}
	
	//method to check if money amount if valid
	public static boolean checkAmount(String amount, String action) {
		
		//check if null
		if(amount.isEmpty()) {
			System.out.println("Error: No amount was entered.");
			return false;
		}
		
		//check if any character is invalid
		//if true, return false
		for(int i = 0; i < amount.length(); i++) {
			if(!Character.isDigit(amount.charAt(i))) {
				System.out.println("Error: The amount can only consist of numeric digits.");
				return false;
			}
		}
		
		//if in machine mode, check if amount is > $1,000.00
		//if yes, return false
		if(mode.equals("machine")) {
			if (action.equals("deposit")) {
				if((amount.length() > 6) || (Integer.valueOf(amount) > 100000)) {
					System.out.println("Error: You cannot deposit more than $1,000.00 while in machine mode in a single transaction.");
					return false;
				}
			} else if (action.equals("withdraw")) {
				if(amount.length() > 6 || Integer.valueOf(amount) > 100000 || sum > 100000) {
					System.out.println("Error: You cannot withdraw more than $1,000.00 in an account while in machine mode.");
					return false;
				}
			} else if (action.equals("transfer")) {
				if(amount.length() > 6 || Integer.valueOf(amount) > 100000) {
					System.out.println("Error: You cannot transfer more than $1,000.00 while in machine mode in a single transaction.");
					return false;
				}
			}
		}
		
		//if in agent mode, check if amount is > $999,999.99
		//if yes, return false
		else {
			if (action.equals("deposit")) {
				if(amount.length() > 8) {
					System.out.println("Error: You cannot deposit more than $999,999.99 in a single transaction.");
					return false;
				}
			} else if (action.equals("withdraw")) {
				if(amount.length() > 8) {
					System.out.println("Error: You cannot withdraw more than $999,999.99 in a single transaction.");
					return false;
				}
			} else if (action.equals("transfer")) {
				if(amount.length() > 8) {
					System.out.println("Error: You cannot transfer more than $999,999.99 in a single transaction.");
					return false;
				}
			}
		}
		
		//otherwise, return true
		return true;
	}
	
	//method to check if account is in accounts list
	public static boolean verifyAccount(String number) {
		
		//for each item in account list, check if inputed number matches an item
		//if yes, return true
		for(String key: accounts) {
			if(key.matches(number)) {
				return true;
			}
		}
		
		//otherwise, return false
		return false;
	}
}
