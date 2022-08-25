package JDBC;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Target {
	static Connection con = CreateConnection.getConnection();

	public static void getTargetParams(int numberOfUsers, int itemId, int quantityPerUser) throws SQLException {
		// 1. Check how many users to create
		int usersToCreate = getUsersToCreateCount(numberOfUsers);
		User[] users = new User[numberOfUsers];
		for (int i = 0; i < usersToCreate; i++) {
			users[i] = generateRandomUser();

			String query = users[i].getInsertQuery();

			PreparedStatement preparedStatement = con.prepareStatement(query);

			preparedStatement.execute();
		}

		// 2. Check that item exists
		if (doesItemExist(itemId) == 0) {
			System.out.println("Item doesn't exist.");
			return;
		}

		// 3. Check that item has sufficient quantity
		int itemTotalQutantity = getItemTotalQuantity(itemId);
		if (itemTotalQutantity < quantityPerUser * numberOfUsers) {
			increaseQuantity(itemId, quantityPerUser * numberOfUsers - itemTotalQutantity);
		}
		itemTotalQutantity = getItemTotalQuantity(itemId);

		// 4. Get All Needed Users Ids
		int[] neededUsers = getAllNeededUsersIds(numberOfUsers);

		// 5. Create Purchases for them
		int remainingQuantity = itemTotalQutantity - quantityPerUser * numberOfUsers;
		createPurchasesForUsers(neededUsers, itemId, quantityPerUser, remainingQuantity);

	}

	public static void getTarget() throws SQLException {
		Scanner s = new Scanner(System.in);

		System.out.println("Create Target ....");

		String insertInput = s.nextLine();
		String[] commaSeparatedInsert = insertInput.split(" ")[1].split(",");
		String numberOfUsersStr = "" + commaSeparatedInsert[0];
		String itemIdStr = "" + commaSeparatedInsert[1];
		String quantityPerUserStr = "" + commaSeparatedInsert[2];

		int numberOfUsers = Integer.parseInt(numberOfUsersStr);
		int itemId = Integer.parseInt(itemIdStr);
		int quantityPerUser = Integer.parseInt(quantityPerUserStr);

		getTargetParams(numberOfUsers, itemId, quantityPerUser);

	}

	public static int getUsersToCreateCount(int numberOfUsers) throws SQLException {
		String query = "Select Count(id) from User";
		// TODO: To run the query in Db

		PreparedStatement preparedStatement = con.prepareStatement(query);

		// preparedStatement.execute(query);
		ResultSet result = preparedStatement.executeQuery(query);
		result.next();

		return numberOfUsers - Integer.parseInt(result.getString(1));
	}

	public static User generateRandomUser() throws SQLException {
		int fNameIndex = new Random().nextInt(StaticData.names.length);
		int sNameIndex = new Random().nextInt(StaticData.names.length);
		int cityIndex = new Random().nextInt(StaticData.cities.length);

		;

		return new User(0, StaticData.names[fNameIndex], StaticData.names[sNameIndex], StaticData.cities[cityIndex]);
	}

	public static int doesItemExist(int itemId) throws SQLException {
		String query = String.format("Select Count(*) from items where Id = %s", itemId);
		// TODO: To run the query in Db
		PreparedStatement preparedStatement = con.prepareStatement(query);

		// preparedStatement.execute(query);
		ResultSet result = preparedStatement.executeQuery();
		result.next();

		return Integer.parseInt(result.getString(1));
	}

	public static int getItemTotalQuantity(int itemId) throws SQLException {
		String query = "select sum(quantity) as sum from items_store group by itemId ;";
		// TOOD: execute the command
		PreparedStatement preparedStatement = con.prepareStatement(query);

		// preparedStatement.execute(query);

		ResultSet result = preparedStatement.executeQuery();
		result.next();
		return Integer.parseInt(result.getString(1));
	}

	public static void increaseQuantity(int itemId, int quantity) throws SQLException {
		String query = String.format(
				"insert into items_store values (%s, %s, %s,curDate()) on duplicate key update Quantity=Quantity+%s;",
				itemId, 1, quantity, quantity);

		// TODO: Execute the command
		PreparedStatement preparedStatement = con.prepareStatement(query);

		preparedStatement.execute(query);
	}

	public static int[] getAllNeededUsersIds(int numberOfUsers) throws SQLException {
		int[] usersIds = new int[numberOfUsers];
		String query = String.format("select id from user limit %s", numberOfUsers);
		// TODO execute query
		PreparedStatement preparedStatement = con.prepareStatement(query);

		ResultSet result = preparedStatement.executeQuery(query);

		int i = 0;
		try {
			while (result.next()) {
				usersIds[i] = Integer.parseInt(result.getString("Id"));
				i++;
			}
		} catch (Exception e) {

		}

		return usersIds;
	}

	public static void createPurchasesForUsers(int[] neededUsers, int itemId, int quantityPerUser,
			int remainingQuantity) throws SQLException {
		// userId. itemId, quantity
		for (int i = 0; i < neededUsers.length; i++) {
			createPurchaseForUser(neededUsers[i], itemId, quantityPerUser);
		}
		setItemQuantityToRemaining(itemId, remainingQuantity);
	}
	// statement

	public static void createPurchaseForUser(int userId, int itemId, int quantity) throws SQLException {

		con.setAutoCommit(false);

		String queryToInsertPurchase = String.format("insert into purchas(userId,creationDate) values(%s,curDate());",
				userId);
		Statement Statement = con.createStatement();
		Statement.executeUpdate(queryToInsertPurchase);
		String queryToSelectPurchaseId = String.format("select Id from purchas order by Id desc limit 1");
		PreparedStatement ps = con.prepareStatement(queryToSelectPurchaseId);
		ResultSet rs= ps.executeQuery();
		rs.next();
		int purchaseId= rs.getInt(1);
		String queryToInsertPurchase_items = String.format(
				"insert into purches_items values(%s,%s,%s)",
				itemId ,purchaseId, quantity);
		// TODO: Execute the query

		Statement.executeUpdate(queryToInsertPurchase_items);

		con.commit();
		con.setAutoCommit(true);
	}

	public static void setItemQuantityToRemaining(int itemId, int remainingQuantity) throws SQLException {
		String queryToSetZero = String.format("update items_store set quantity = 0 where itemId = %s", itemId);
		// Todo: Execute Command
		PreparedStatement preparedStatement = con.prepareStatement(queryToSetZero);

		preparedStatement.execute(queryToSetZero);
		String queryToSetValue = String.format("update items_store set quantity = %s where itemId = %s And StoreId = 1",
				remainingQuantity, itemId);

		PreparedStatement result = con.prepareStatement(queryToSetValue);

		result.execute(queryToSetValue);
	}

}
