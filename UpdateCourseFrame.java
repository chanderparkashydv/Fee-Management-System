import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateCourseFrame extends JFrame implements ActionListener
{
	JLabel lblTitle, lblId, lblName, lblDuration, lblFees, lblMessage;
	JTextField txtId, txtName, txtDuration, txtFees;
	JButton btnLoad, btnUpdate;

	public UpdateCourseFrame()
	{
		setTitle("Update Course");
		setSize(500,400);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Update Course");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(150,20,220,30);
		add(lblTitle);

		lblId = new JLabel("Course ID:");
		lblId.setBounds(60,80,100,25);
		add(lblId);

		txtId = new JTextField();
		txtId.setBounds(180,80,120,25);
		add(txtId);

		btnLoad = new JButton("Load");
		btnLoad.setBounds(320,80,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblName = new JLabel("Course Name:");
		lblName.setBounds(60,130,100,25);
		add(lblName);

		txtName = new JTextField();
		txtName.setBounds(180,130,220,25);
		add(txtName);

		lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(60,170,100,25);
		add(lblDuration);

		txtDuration = new JTextField();
		txtDuration.setBounds(180,170,220,25);
		add(txtDuration);

		lblFees = new JLabel("Fees:");
		lblFees.setBounds(60,210,100,25);
		add(lblFees);

		txtFees = new JTextField();
		txtFees.setBounds(180,210,220,25);
		add(txtFees);

		btnUpdate = new JButton("Update");
		btnUpdate.setBounds(180,260,100,35);
		btnUpdate.addActionListener(this);
		add(btnUpdate);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,310,400,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}

	private void loadCourse()
	{
		String idText = txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please enter course id.");
			return;
		}

		int courseId = 0;
		try
		{
			courseId = Integer.parseInt(idText);
		}
		catch(Exception e)
		{
			lblMessage.setText("Please Enter Valid course id");
			return;
		}

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "Select * from course Where c_id = " + courseId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtName.setText(rs.getString("course_name"));
				txtDuration.setText(rs.getString("course_duration"));
				txtFees.setText(rs.getString("course_fees"));
				lblMessage.setText("");
			}
			else
			{
				txtName.setText("");
				txtDuration.setText("");
				txtFees.setText("");
				lblMessage.setText("Course Not Found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading course.");
		}
		finally
		{
			try { if(rs != null) rs.close(); } catch(Exception e){}
			try { if(stmt != null) stmt.close(); } catch(Exception e){}
			try { if(con != null) con.close(); } catch(Exception e){}
		}
	}

	private void updateCourse()
	{
		String idText = txtId.getText().trim();
		String name = txtName.getText().trim();
		String duration = txtDuration.getText().trim();
		String feesText = txtFees.getText().trim();

		if(idText.equals("") || name.equals("") || duration.equals("") || feesText.equals(""))
		{
			lblMessage.setText("All fields are required.");
			return;
		}

		int courseId = 0;
		double fees = 0;

		try
		{
			courseId = Integer.parseInt(idText);
		}
		catch(Exception e)
		{
			lblMessage.setText("Please Enter Valid course id.");
			return;
		}

		try
		{
			fees = Double.parseDouble(feesText);
			if(fees < 0)
			{
				lblMessage.setText("Fees cannot be negative.");
				return;
			}
		}
		catch(Exception e)
		{
			lblMessage.setText("Please Enter Valid Fees");
			return;
		}

		Connection con = null;
		Statement stmt = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "UPDATE course SET course_name = '" + escapeValue(name) + "', "
			+ "course_duration = '" + escapeValue(duration) + "', "
			+ "course_fees = '" + fees + "' "
			+ "WHERE c_id = " + courseId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Course Updated Successfully.");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Course id not found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while Updating course.");
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
			loadCourse();
		}
		else if(ae.getSource() == btnUpdate)
		{
			updateCourse();
		}
	}
}
