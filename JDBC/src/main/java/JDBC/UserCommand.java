package JDBC;

import JDBC.ExecuteQuery.ExecuteQuery;
import JDBC.ReadData.Purchase;
import JDBC.ReadData.Quantity;
import JDBC.Validation.Validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;



public class UserCommand {
	static Connection con = CreateConnection.getConnection();


	public static void insertItem() throws SQLException {

		ExecuteQuery.InsetNewItem();

	}

	public static void addQuantity() throws SQLException {

		var addQuantity = Quantity.readAddQuantity();
	if  (Validation.checkIfItemExist(addQuantity.getItemId())) {
			Validation.checkIfStoreExist(addQuantity.getStoreId());

			ExecuteQuery.AddQuantity();
		}



		
		


	}

	public static void createPerchase() throws SQLException {
			var purchase = Purchase.AddPurchase();


		  Validation.checkQuantityIsValid(purchase.getItemId(),purchase.getQuantity());

		 {

			 ExecuteQuery.AddPurchase();
			int id= ExecuteQuery.getNewlyPurchaseAdded();

			ExecuteQuery.SubstractQuantityOfItem(purchase.getItemId(), purchase.getQuantity(), id);



		}

	}

	public static void getTopPurchaseItem() throws SQLException {
		String topItemPurches = "select itemId, sum(quantity) as sum from purches_items "
				+ " group by itemId order by quantity desc limit 1";

		PreparedStatement topItem = con.prepareStatement(topItemPurches);
		ResultSet topResult = topItem.executeQuery(topItemPurches);
		ResultSetMetaData rsmd = topResult.getMetaData();
		int columsNumber = rsmd.getColumnCount();

		while (topResult.next()) {
			String top = "";
			for (int i = 1; i <= columsNumber; i++) {
				top += topResult.getString(i) + ":";
			}

			System.out.println(top);
		}
	}

	public static void getItemWithZeroQuantity() throws SQLException {
		String itemQuantity = "select itemName,Quantity from items inner join items_store  on items.Id=items_store.itemId and Quantity=0";

		// String userCount = " select count(id) from user;";

		PreparedStatement count = con.prepareStatement(itemQuantity);
		ResultSet Result = count.executeQuery(itemQuantity);
		ResultSetMetaData rsmd = Result.getMetaData();
		int columsNumber = rsmd.getColumnCount();

		while (Result.next()) {
			String top = "";
			for (int i = 1; i <= columsNumber; i++) {
				top += Result.getString(i) + ":";
			}

			System.out.println(top);
		}

	}

}
