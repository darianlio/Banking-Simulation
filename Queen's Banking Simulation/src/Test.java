//This program reads the valid accounts file and creates a valid accounts list.
//Program then calls Activity in Main.java, which eventually returns a list of transactions.
//The writes transactions list to transaction summary file.

//Program run by entering 'java Test' into the Windows command prompt.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Test extends Main{
	
	//global variables - list of valid accounts and list of transactions
	public static ArrayList<String> accounts = new ArrayList<String>();
	public static ArrayList<String> tsf = new ArrayList<String>();
	
	//main method to start
	public static void main(String[] args) {
		
		//read valid accounts file
		readFile("validaccounts.txt");
		
		//start user's use in Main.java
		tsf = Activity(accounts);
		
		//write transactions to transaction summary file
		writeFile();
		
		scan.close();
	}
	
	//read valid accounts file and insert account numbers into accounts list
	public static void readFile(String fileName) {
		
		//create the file
		File file = new File(fileName);
		try {
			Scanner scan = new Scanner(file);
			
			//scan each line to add account number to accounts list
			while(scan.hasNext()) {
				accounts.add(scan.next());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//create transaction summary file and add transactions
	public static void writeFile() {
		try {
			
			//create new file
			File file = new File("transactionsummaryfile.txt");
			file.createNewFile();
			FileWriter write = new FileWriter(file);
			
			//write each transaction to file, and add new line character
			for(int i = 0; i < tsf.size(); i++) {
				String str = tsf.get(i);
				write.write(str);
				write.append(System.lineSeparator());
			}
			//write EOS line and finish file
			write.write("EOS");
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
