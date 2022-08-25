package JDBC;


import JDBC.Validation.Validation;

import java.sql.SQLException;
import java.util.Scanner;

public class JDBC {
	public static void main(String[] args) throws SQLException {

		Scanner s =new Scanner(System.in);
		System.out.println("write insert: to insert new item /n" +
				"write add: to add item quantity /n" +
				"write purchase: to create purchase /n" +
				"write MultiPurchase: to create Multi Purchase/n" +
				"write report: to get purchase report" +
				"write topPurchase: to get top purchase item /n" +
				"wirte zeroQuantity: to get items with zero quantity");

		String userCommand =s.next();

		switch (userCommand.toLowerCase()){
			case "insert":{
				UserCommand.insertItem();
				break;
			}
			case "add":{
				UserCommand.addQuantity();
				break;
			}
			case "purchase":{
				UserCommand.createPerchase();
				break;
			}
			case "multipurchase":{
				MultiPurchase.getTarget();
				break;
			}
			case "toppurchase":{
				UserCommand.getTopPurchaseItem();
				break;
			}
			case "zeroquantity":{
				UserCommand.getItemWithZeroQuantity();
				break;
			}
			case "report":{
				Report.PurchaseItemReport();
				break;
			}
		}




	}

}
