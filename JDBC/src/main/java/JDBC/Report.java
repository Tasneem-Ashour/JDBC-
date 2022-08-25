package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

public class Report {

	static Connection con = CreateConnection.getConnection();

	static Scanner s = new Scanner(System.in);
	
	public static void PurchaseItemReport() throws SQLException {

		System.out.println("enter the date ... you should enter in form [YYYY-MM-DD]");
		String reportDate = s.nextLine();
		String purchesDate = String.format("CALL `itemstask`.`purches_report`(%s);", reportDate);

		PreparedStatement purchaseReport = con.prepareStatement(purchesDate);


		ResultSet purchesReportResult = purchaseReport.executeQuery(purchesDate);
		ResultSetMetaData rsmd = purchesReportResult.getMetaData();
		int columsNumber = rsmd.getColumnCount();


		while (purchesReportResult.next()) {

			String finalReport = "";
			for (int i = 1; i < columsNumber; i++) {
				finalReport += purchesReportResult.getString(i) + ": ";
			}
			System.out.println(finalReport);
		}
	}
	
	public static void AddItemReport() throws SQLException {
		System.out.println("enter the date ... you should enter in form [YYYY-MM-DD]");
		String reportDate = s.nextLine();
		
		String addDate = String.format("CALL `itemstask`.`add_report`(%s);", reportDate);

		PreparedStatement addReport = con.prepareStatement(addDate);


		ResultSet addReportResult = addReport.executeQuery(addDate);
		ResultSetMetaData rsmd = addReportResult.getMetaData();
		int columsNumber = rsmd.getColumnCount();

		while (addReportResult.next()) {

			String finalReport = "";
			for (int i = 1; i < columsNumber; i++) {
				finalReport += addReportResult.getString(i) + ":";
			}
			System.out.println(finalReport);
		}
	}

}
