package JDBC.ReadData;

import JDBC.Data.AddQuantity;

import java.util.Scanner;

public  class Quantity {

    public static AddQuantity readAddQuantity(){
        Scanner s=new Scanner(System.in);
        AddQuantity quantity= new AddQuantity();
        System.out.println("Add Quantity to stores ....");
        System.out.println("Add ItemId,StoreId,Quantity ");

        String input = s.nextLine();
        String[] commaSeparated = input.split(" ")[1].split(",");
        quantity.setItemId(Integer.parseInt("" + commaSeparated[0]));
        quantity.setStoreId(Integer.parseInt("" + commaSeparated[1]));
        quantity.setQuantity(Integer.parseInt("" + commaSeparated[2]));
        return quantity;
    }
}
