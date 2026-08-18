import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminLoginFrame extends JFrame implements ActionListener
{	
	JLabel lblTitle,lblUserName,lblPassword,lblMessage;
	JTextField txtuserName;
	JPasswordField txtPassword;
	JButton btnlogin;
	public AdminLoginFrame()
	{
		setTitle("Admin Login");
		setSize(450,300);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		lblTitle=new JLabel("ADMIN LOGIN");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(150,20,200,30);
		add(lblTitle);

		lblUserName=new JLabel("Username:");
		lblUserName.setBounds(60,80,100,25);
		add(lblUserName);

		txtuserName=new JTextField();
		txtuserName.setBounds(160,80,180,25);
		add(txtuserName);

		lblPassword=new JLabel("Password:");
		lblPassword.setBounds(60,120,100,25);
		add(lblPassword);

		txtPassword=new JPasswordField();
		txtPassword.setBounds(160,120,180,25);
		add(txtPassword);

		btnlogin= new JButton("LOGIN");
		btnlogin.setBounds(160,165,100,25);
		btnlogin.addActionListener(this);
		add(btnlogin);

		lblMessage=new JLabel("");
		
		setVisible(true);
	}
	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}

	public void actionPerformed(ActionEvent ae)
	{
		String userName = txtuserName.getText().trim();
		String password = new String(txtPassword.getPassword()).trim();
		if(userName.equals("") || password.equals(""))
		{
			lblMessage.setText("Please Enter username and password.");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;
		try
		{
			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query ="Select * from admin where username ='"+escapeValue(userName)+"' And Password= '"+escapeValue(password)+"'";
			rs=stmt.executeQuery(query);

			if(rs.next())
			{
				lblMessage.setText("");
				JOptionPane.showMessageDialog(this,"login successful");
				new AdminDashboardFrame();
				dispose();
			}
			else
			{
				lblMessage.setText("Invalid Credentials");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while login");
		}
		finally
		{
			try	
			{
				if(rs!=null)
					rs.close();
			}
			catch (Exception e) 
			{
				
			}
			try	
			{
				if(stmt!=null)
					stmt.close();
			}
			catch (Exception e) 
			{
				
			}
			try	
			{
				if(con!=null)
					con.close();
			}
			catch (Exception e) 
			{
				
			}
		}
	}
	public static void main(String[] args) 
	{
		new AdminLoginFrame();
	}
}