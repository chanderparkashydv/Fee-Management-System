import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Statement;

public class AddStudentFrame extends JFrame implements ActionListener  
{
	JLabel lblTitle,lblMobile,lblEmail,lblDob,lblFirstName,lblLastName,lblNote,lblMessage;
	JTextField txtMobile,txtEmail,txtDob,txtFirstName,txtLastName;
	JButton btnSave;
	public AddStudentFrame()
	{
		setTitle("ADD STUDENT");
		setSize(560,420);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblTitle=new JLabel("Add Student");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(210,20,160,30);
		add(lblTitle);

		lblFirstName=new JLabel("First Name :");
		lblFirstName.setBounds(60,80,120,25);
		add(lblFirstName);

		txtFirstName=new JTextField();
		txtFirstName.setBounds(190,80,250,25);
		add(txtFirstName);

		lblLastName=new JLabel("Last Name:");
		lblLastName.setBounds(60,120,120,25);
		add(lblLastName);

		txtLastName=new JTextField();
		txtLastName.setBounds(190,120,250,25);
		add(txtLastName);

		lblEmail=new JLabel("Email");
		lblEmail.setBounds(60,160,120,25);
		add(lblEmail);

		txtEmail=new JTextField();
		txtEmail.setBounds(190,160,250,25);
		add(txtEmail);

		lblMobile=new JLabel("Mobile:");
		lblMobile.setBounds(60,200,120,25);
		add(lblMobile);

		txtMobile=new JTextField();
		txtMobile.setBounds(190,200,250,25);
		add(txtMobile);

		lblDob=new JLabel("DOB:");
		lblDob.setBounds(60,240,120,25);
		add(lblDob);

		txtDob=new JTextField();
		txtDob.setBounds(190,240,250,25);
		add(txtDob);

		lblNote=new JLabel("Use date format : YYYY-MM-DD");
		lblNote.setBounds(190,270,220,20);
		lblNote.setForeground(Color.BLUE);
		add(lblNote);

		btnSave=new JButton("Save");
		btnSave.setBounds(210,300,100,25);
		btnSave.addActionListener(this);
		add(btnSave);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,345,430,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}
	
	private void saveStudent()
	{
		String firstName=txtFirstName.getText().trim();
		String lastName=txtLastName.getText().trim();
		String email= txtEmail.getText().trim();
		String mobile = txtMobile.getText().trim();
		String dob = txtDob.getText().trim();

		if(firstName.equals("") || lastName.equals("") || email.equals("") || mobile.equals("") || dob.equals(""))
		{
			lblMessage.setText("All fields are required.");
			return;
		}

		Connection con=null;
		Statement stmt=null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "INSERT INTO student(firstname, lastname, email, mobile, dob) VALUES ('"
			+escapeValue(firstName)+"', '"
			+escapeValue(lastName)+"', '"
			+escapeValue(email)+"', '"
			+escapeValue(mobile)+"', '"
			+escapeValue(dob)+"')";

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Student added Successfully.");
				txtFirstName.setText("");
				txtLastName.setText("");
				txtEmail.setText("");
				txtMobile.setText("");
				txtDob.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Student not added");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while saving Student.");
		}
		finally
		{
			try
			{
				if(stmt != null)
					stmt.close();
			}
			catch(Exception ex){}
			try
			{
				if(con != null)
					con.close();
			}
			catch(Exception ex){}
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnSave)
		{
			saveStudent();
		}
	}
}