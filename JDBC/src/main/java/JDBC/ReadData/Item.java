package JDBC.ReadData;

import JDBC.Data.InsertNewItem;

import java.util.Scanner;

public class Item {
    public InsertNewItem  readInsertItemData(){
        Scanner s = new Scanner(System.in);
        InsertNewItem item = new InsertNewItem();
        System.out.println("Add new item...");
        System.out.println("Add Item name , Item demintions width , Item demintions height , Author name , Manufacturer");
        String insertInput = s.nextLine();
        String[] commaSeparatedInsert = insertInput.split(" ")[1].split(",");
       item.setItemName(commaSeparatedInsert[0]) ;
       item.setDemintionsW(Integer.parseInt("" + commaSeparatedInsert[1]));
       item.setDemintionsH(Integer.parseInt("" + commaSeparatedInsert[2]));
       item.setAuthor("" + commaSeparatedInsert[3]);
       item.setManufacturer("" + commaSeparatedInsert[4]);
       return item;
    }
}
