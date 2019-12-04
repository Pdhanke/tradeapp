package com.prashant.database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;

/**
 * A sample app that connects to a Cloud SQL instance and lists all available
 * tables in a database.
 */

public class Cloud_sql {
	public static void main(String[] args) throws SQLNonTransientConnectionException, IOException, SQLException {

		String instanceConnectionName = "credible-spark-248408:asia-south1:tickdataaug";
		String databaseName = "ticksAug";

		String IP_of_instance = "35.200.148.170";
		String username = "zstreamingquotesdb";
		String password = "admin";

		String jdbcurl = "jdbc:mysql://google/ticksAug?cloudSqlInstance=credible-spark-248408:asia-south1:tickdataaug&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false"	;
		String jdbcUrl = String.format(
				"jdbc:mysql://%s/%s?cloudSqlInstance=%s"
						+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false",
				IP_of_instance, databaseName, instanceConnectionName);

		Connection connection = DriverManager.getConnection(jdbcurl, username, password);

		try (Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("Select * from TickData1s");
			System.out.println("Cloud_sql.main()" + resultSet.getFetchSize());
			while (resultSet.next()) {
				System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " 
						+resultSet.getString(3) + " " +resultSet.getString(4)
						+resultSet.getString(5) + " " +resultSet.getString(6)
						+resultSet.getString(7) + " " +resultSet.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
