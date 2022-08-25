package JDBC;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;



    class JDBCTest {

        static Connection con = CreateConnection.getConnection();

        public static int setUpUser() throws Exception {

            String query = "select count(Id) from user";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();

            return rs.getInt(1);

        }

        public static int setUpQuantity() throws Exception {

            String query = "select sum(quantity) from items_store where itemId=1";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();

            return rs.getInt(1);

        }

        int numberOfUser = 2;
        int itemId = 1;
        int quantityPerUser = 3;

        public int user() throws Exception {
            int currentUser = setUpUser();
            if (currentUser > numberOfUser) {
                return currentUser;
            }
            return numberOfUser;

        }

        public int quantity() throws Exception {
            int currentQuantity = setUpQuantity();

            if (currentQuantity > quantityPerUser * numberOfUser) {
                return currentQuantity - quantityPerUser * numberOfUser;
            }
            while (setUpQuantity() > quantityPerUser) {
                currentQuantity -= setUpQuantity() - quantityPerUser;
            }
            return currentQuantity;
        }

        @Test
        void test() throws Exception {
//-----------------before-------------------
            System.out.print("Number of user befor test:");

            int numberOfUserBeforTest = setUpUser();
            System.out.println(numberOfUserBeforTest);

            System.out.print("Number of quantity befor test:");
            int quantityBeforTest = setUpQuantity();
            System.out.println(quantityBeforTest);
//----------------calling-------------------------
            MultiPurchase.getTargetParams(numberOfUser, itemId, quantityPerUser);
//------------------after----------------------
            System.out.print("Number of user after test");

            int numberOfUserAfterTest = setUpUser();
            System.out.println(numberOfUserAfterTest);


            System.out.print("Number of quantity after test:");
            int quantityAfterTest = setUpQuantity();
            System.out.println(quantityAfterTest);
//------------------test-------------------------


            assertAll("result", () -> assertEquals(36, user()),
                    () -> assertEquals(2, quantity()));

        }

    }