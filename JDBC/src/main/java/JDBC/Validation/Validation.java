package JDBC.Validation;

import JDBC.CreateConnection;
import JDBC.ReadData.Purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validation {
    static Connection con = CreateConnection.getConnection();
    public static boolean checkIfItemExist(int itemId) throws SQLException {
        String query =String.format("select Id from items where Id=%s", itemId)	;
        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }
    public static boolean checkIfStoreExist(int storeId) throws SQLException {
        String query =String.format("select Id from stores where Id=%s", storeId)	;
        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }
    public static boolean checkQuantityIsValid(int itemIdInt, int quantityInt) throws SQLException {
        // TODO Auto-generated method stub
        var purchase = Purchase.AddPurchase();
        itemIdInt=purchase.getItemId();
        quantityInt=purchase.getQuantity();

        String query = String.format("select Sum(Quantity) from items_store where ItemId=%s",
                itemIdInt);
        PreparedStatement preparedStatement = con.prepareStatement(query);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        int currentQuantity = Integer.parseInt(rs.getString(1));
        int checkQuatity = currentQuantity - quantityInt;
        boolean isValied = true;

        if (checkQuatity < 0) {
            System.out.println("There's no quantity enough");
            isValied = false;
        }

        return isValied;

    }
}
