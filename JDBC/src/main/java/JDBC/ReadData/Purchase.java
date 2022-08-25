package JDBC.ReadData;

import JDBC.Data.AddPurchase;

import java.util.Scanner;

public class Purchase {

    public static AddPurchase AddPurchase(){
        Scanner s = new Scanner(System.in);
        AddPurchase addPurchase=new AddPurchase();
        System.out.println("Add Purchase Transaction ....");
        System.out.println("Add userId,itemId,quantity");

        String input = s.nextLine();
        String[] commaSeparated = input.split(" ")[1].split(",");
        addPurchase.setUserId(Integer.parseInt("" + commaSeparated[0])) ;
        addPurchase.setItemId(Integer.parseInt("" + commaSeparated[1]));
        addPurchase.setQuantity(Integer.parseInt("" + commaSeparated[2]));
        return addPurchase;
    }
}
