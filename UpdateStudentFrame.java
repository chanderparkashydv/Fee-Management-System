import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateStudentFrame extends JFrame implements ActionListener
{
	JLabel lblTitle, lblId, lblFirstName, lblLastName, lblEmail, lblMobile, lblDob, lblNote, lblMessage;
	JTextField txtId, txtFirstName, txtLastName, txtEmail, txtMobile, txtDob;
	JButton btnLoad, btnUpdate;

	public UpdateStudentFrame()
	{
		setTitle("Update Student");
		setSize(580,460);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Update Student");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(190,20,220,30);
		add(lblTitle);

		lblId = new JLabel("Student ID:");
		lblId.setBounds(60,70,120,25);
		add(lblId);

		txtId = new JTextField();
		txtId.setBounds(190,70,120,25);
		add(txtId);

		btnLoad = new JButton("Load");
		btnLoad.setBounds(330,70,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblFirstName = new JLabel("First Name :");
		lblFirstName.setBounds(60,120,120,25);
		add(lblFirstName);

		txtFirstName = new JTextField();
		txtFirstName.setBounds(190,120,250,25);
		add(txtFirstName);

		lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(60,160,120,25);
		add(lblLastName);

		txtLastName = new JTextField();
		txtLastName.setBounds(190,160,250,25);
		add(txtLastName);

		lblEmail = new JLabel("Email:");
		lblEmail.setBounds(60,200,120,25);
		add(lblEmail);

		txtEmail = new JTextField();
		txtEmail.setBounds(190,200,250,25);
		add(txtEmail);

		lblMobile = new JLabel("Mobile:");
		lblMobile.setBounds(60,240,120,25);
		add(lblMobile);

		txtMobile = new JTextField();
		txtMobile.setBounds(190,240,250,25);
		add(txtMobile);

		lblDob = new JLabel("DOB:");
		lblDob.setBounds(60,280,120,25);
		add(lblDob);

		txtDob = new JTextField();
		txtDob.setBounds(190,280,250,25);
		add(txtDob);

		lblNote = new JLabel("Use date format : YYYY-MM-DD");
		lblNote.setBounds(190,310,220,20);
		lblNote.setForeground(Color.BLUE);
		add(lblNote);

		btnUpdate = new JButton("Update");
		btnUpdate.setBounds(210,345,100,25);
		btnUpdate.addActionListener(this);
		add(btnUpdate);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,385,450,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}

	private void loadStudent()
	{
		String idText = txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please enter Student id.");
			return;
		}

		int studentId = 0;
		try
		{
			studentId = Integer.parseInt(idText);
		}
		catch(Exception e)
		{
			lblMessage.setText("Please Enter Valid Student id");
			return;
		}

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "Select * from student Where s_id = " + studentId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtFirstName.setText(rs.getString("firstname"));
				txtLastName.setText(rs.getString("lastname"));
				txtEmail.setText(rs.getString("email"));
				txtMobile.setText(rs.getString("mobile"));
				txtDob.setText(rs.getString("dob"));
				lblMessage.setText("");
			}
			else
			{
				txtFirstName.setText("");
				txtLastName.setText("");
				txtEmail.setText("");
				txtMobile.setText("");
				txtDob.setText("");
				lblMessage.setText("Student Not Found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading Student.");
		}
		finally
		{
			try { if(rs != null) rs.close(); } catch(Exception e){}
			try { if(stmt != null) stmt.close(); } catch(Exception e){}
			try { if(con != null) con.close(); } catch(Exception e){}
		}
	}

	private void updateStudent()
	{
		String idText = txtId.getText().trim();
		String firstName = txtFirstName.getText().trim();
		String lastName = txtLastName.getText().trim();
		String email = txtEmail.getText().trim();
		String mobile = txtMobile.getText().trim();
		String dob = txtDob.getText().trim();

		if(idText.equals("") || firstName.equals("") || lastName.equals("") || email.equals("") || mobile.equals("") || dob.equals(""))
		{
			lblMessage.setText("All fields are required.");
			return;
		}

		int studentId = 0;
		try
		{
			studentId = Integer.parseInt(idText);
		}
		catch(Exception e)
		{
			lblMessage.setText("Please Enter Valid Student id.");
			return;
		}

		Connection con = null;
		Statement stmt = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "UPDATE student SET firstname = '" + escapeValue(firstName) + "', "
			+ "lastname = '" + escapeValue(lastName) + "', "
			+ "email = '" + escapeValue(email) + "', "
			+ "mobile = '" + escapeValue(mobile) + "', "
			+ "dob = '" + escapeValue(dob) + "' "
			+ "WHERE s_id = " + studentId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Student Updated Successfully.");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Student id not found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while Updating student.");
		}
		finally
		{
			try { if(stmt != null) stmt.close(); } catch(Exception ex){}
			try { if(con != null) con.close(); } catch(Exception ex){}
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnLoad)
		{
			loadStudent();
		}
		else if(ae.getSource() == btnUpdate)
		{
			updateStudent();
		}
	}
}
