package JDBC.ExecuteQuery;

import JDBC.CreateConnection;
import JDBC.Data.InsertNewItem;
import JDBC.ItemStore;
import JDBC.ReadData.Item;
import JDBC.ReadData.Purchase;
import JDBC.ReadData.Quantity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ExecuteQuery {
    static Connection con = CreateConnection.getConnection();
    static ResultSet rs;

    public static void InsetNewItem() throws SQLException {

        Item i = new Item();
        InsertNewItem item = i.readInsertItemData();

        System.out.println(item.getItemName());
        String addNewItem = ("insert into items(itemName,demintionsW,deminitionH,author,manufacturer) values(?,?,?,?,?);");

        PreparedStatement preparedStatement = con.prepareStatement(addNewItem, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, item.getItemName());
        preparedStatement.setInt(2, item.getDemintionsW());
        preparedStatement.setInt(3, item.getDemintionsH());
        preparedStatement.setString(4, item.getAuthor());
        preparedStatement.setString(5, item.getManufacturer());

        preparedStatement.execute();
        System.out.println(item.getItemName());

        rs = preparedStatement.getGeneratedKeys();
        if (rs.next()) {
            String id = rs.getString(1);
            System.out.printf("item %s add successfully", id);

        }


    }

    public static void AddQuantity() throws SQLException {

        var addQuantity = Quantity.readAddQuantity();


        String query = String.format("insert into items_store values (%s,%s,%s,curDate()) on duplicate key update Quantity=Quantity+%s;", addQuantity.getItemId(), addQuantity.getStoreId(), addQuantity.getQuantity(), addQuantity.getQuantity());

        PreparedStatement preparedStatement = con.prepareStatement(query);

        preparedStatement.execute(query);

        System.out.println("data inserted successfully");

    }

    public static void AddPurchase() throws SQLException {
        var purchase = Purchase.AddPurchase();

        con.setAutoCommit(false);
        String queryToAddPurchase = String.format("insert into purchas(userId,creationDate) values(%s,curDate());", purchase.getItemId());
        String queryToAddPurchaseItems = String.format("insert into purches_items select %s as t ,Id , %s as q  from purchas order by Id desc limit 1;", purchase.getItemId(), purchase.getQuantity());

        Statement statement = con.createStatement();
        statement.executeUpdate(queryToAddPurchase);
        statement.executeUpdate(queryToAddPurchaseItems);

        con.commit();
        con.setAutoCommit(true);

        System.out.println("Purchase add successfully");


    }

    public static int getNewlyPurchaseAdded() throws SQLException {


        String queryToGetNewlyAdded = "select Id from purchas order by Id desc limit 1";

        PreparedStatement preparedStatementToGetNewId = con.prepareStatement(queryToGetNewlyAdded);


        ResultSet rs = preparedStatementToGetNewId.executeQuery();
        if (rs.next())
            return rs.getInt(1);


        return rs.getInt(1);
    }


    public static void SubstractQuantityOfItem(int itemIdInt, int quantityInt, int purchaseId) throws SQLException {
        // TODO Auto-generated method stub

        String query = String.format("select itemId, storeId, Quantity from items_store where ItemId=%s", itemIdInt);
        PreparedStatement preparedStatement = con.prepareStatement(query);

        // 1. Mapping query result to list
        List<ItemStore> items_stores = new ArrayList<ItemStore>();
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            ItemStore current = new ItemStore(Integer.parseInt(rs.getString("itemId")),
                    Integer.parseInt(rs.getString("storeId")), Integer.parseInt(rs.getString("Quantity")));
            items_stores.add(current);
        }

        // 2. Update Quantity
        for (ItemStore itemStore : items_stores) {
            if(itemStore.quantity ==0) {
                continue;
            }
            if (quantityInt > itemStore.quantity) {
                quantityInt -= itemStore.quantity;
                setItemQuantity(itemStore.itemId, itemStore.storeId, 0);
                addToPurchasesStores(purchaseId, itemStore.storeId, itemStore.quantity);
            } else {
                setItemQuantity(itemStore.itemId, itemStore.storeId, itemStore.quantity - quantityInt);
                addToPurchasesStores(purchaseId, itemStore.storeId, quantityInt);
                quantityInt = 0;
                break;
            }
        }




    }

    private static void setItemQuantity(int itemId, int storeId, int quantity) throws SQLException {
        String queryToSetValue = String.format(
                "update items_store set quantity = %s where itemId = %s And StoreId = %s", quantity, itemId, storeId);
        PreparedStatement result = con.prepareStatement(queryToSetValue);
        result.execute(queryToSetValue);
    }

    private static void addToPurchasesStores(int purchaseId, int storeId, int quantity) throws SQLException {
        String queryToAddPurchaseInTable = String.format("insert into purchases_stores values(%s,%s,%s) ",
                storeId, purchaseId, quantity);
        PreparedStatement ps = con.prepareStatement(queryToAddPurchaseInTable);
        ps.execute();
    }

}
