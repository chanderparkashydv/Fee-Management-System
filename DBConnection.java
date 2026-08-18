import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection
{
	public static Connection getConnection()
	{
		Connection con = null;
		try
		{
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch (ClassNotFoundException e)
			{
				// Fallback for older mysql-connector-java versions
				Class.forName("com.mysql.jdbc.Driver");
			}

			con = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/fees_management?useSSL=false&serverTimezone=UTC",
				"root",
				""
			);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return con;
	}
}
