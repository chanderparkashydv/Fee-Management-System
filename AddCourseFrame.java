import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Statement;

public class AddCourseFrame extends JFrame implements ActionListener
{
	JLabel lblTitle,lblMessage,name,duration,fees;
	JTextField nameField,durationField,feesField;
	JButton btnSave;
	public AddCourseFrame()
	{
		setTitle("Add Course");
		setSize(500,350);
		setLayout(null);
		setLocationRelativeTo(null);


		lblTitle=new JLabel("Add Course");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(180,20,150,30);
		add(lblTitle);

		name=new JLabel("Course Name:");
		name.setBounds(60,80,100,25);
		add(name);

		nameField=new JTextField(20);
		nameField.setBounds(180,80,200,25);
		add(nameField);

		duration=new JLabel("Duration:");
		duration.setBounds(60,125,200,25);
		add(duration);

		durationField=new JTextField();
		durationField.setBounds(180,125,200,25);
		add(durationField);

		fees=new JLabel("Fees:");
		fees.setBounds(60,170,100,25);
		add(fees);

		feesField=new JTextField();
		feesField.setBounds(180,170,200,25);
		add(feesField);

		btnSave=new JButton("Save");
		btnSave.setBounds(180,220,100,25);
		btnSave.addActionListener(this);
		add(btnSave);

		lblMessage= new JLabel();
		lblMessage.setBounds(60,270,350,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}
	public void actionPerformed(ActionEvent ae)
	{
		String name= nameField.getText().trim();
		String duration=durationField.getText().trim();
		String feesText=feesField.getText().trim();

		if(name.equals("") || duration.equals("") || feesText.equals(""))
		{
			lblMessage.setText("ALl Fields are required.");
			return;
		}

		double fees=0;
		try
		{
			fees=Double.parseDouble(feesText);
			if(fees < 0)
			{
				lblMessage.setText("Fees cannot be negative.");
				return;
			}
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid Fees");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		try
		{
			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query="INSERT INTO course(course_name,course_duration,course_fees) VALUES('"
			+escapeValue(name)+"','"
			+escapeValue(duration)+"','"+fees+"' )";

			int result= stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Course added Successfully");
				nameField.setText("");
				durationField.setText("");
				feesField.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Course not Added");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while saving course.");
		}
		finally
		{
			try
			{
				if(stmt != null)
					stmt.close();
			}
			catch(Exception ex)
			{

			}
			try
			{
				if(con != null)
					con.close();
			}
			catch(Exception ex)
			{
				
			}
		}
	} 
}
