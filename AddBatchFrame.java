import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddBatchFrame extends JFrame implements ActionListener  
{
	JLabel lblTitle,lblCourse,lblBatchName,lblTime,lblStartDate,lblEndDate,lblNote,lblMessage;
	JTextField txtBatchName,txtTime,txtStartDate,txtEndDate;
	JComboBox<String> cmbCourse;
	JButton btnSave;
	public AddBatchFrame()
	{
		setTitle("ADD BATCH");
		setSize(550,420);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblTitle=new JLabel("Add Batch");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(210,20,150,30);
		add(lblTitle);

		lblCourse=new JLabel("Course :");
		lblCourse.setBounds(60,80,120,25);
		add(lblCourse);

		cmbCourse=new JComboBox<>();
		cmbCourse.setBounds(190,80,250,25);
		add(cmbCourse);

		lblBatchName=new JLabel("Batch Name:");
		lblBatchName.setBounds(60,120,120,25);
		add(lblBatchName);

		txtBatchName=new JTextField();
		txtBatchName.setBounds(190,120,250,25);
		add(txtBatchName);

		lblStartDate=new JLabel("Start Date ");
		lblStartDate.setBounds(60,160,120,25);
		add(lblStartDate);

		txtStartDate=new JTextField();
		txtStartDate.setBounds(190,160,250,25);
		add(txtStartDate);

		lblEndDate=new JLabel("End Date ");
		lblEndDate.setBounds(60,200,120,25);
		add(lblEndDate);

		txtEndDate=new JTextField();
		txtEndDate.setBounds(190,200,250,25);
		add(txtEndDate);

		lblTime=new JLabel("Batch Time");
		lblTime.setBounds(60,240,120,25);
		add(lblTime);

		txtTime=new JTextField();
		txtTime.setBounds(190,240,250,25);
		add(txtTime);

		lblNote=new JLabel("Use date format : YYYY-MM-DD");
		lblNote.setBounds(190,270,220,20);
		lblNote.setForeground(Color.BLUE);
		add(lblNote);

		btnSave=new JButton("Save");
		btnSave.setBounds(210,305,100,25);
		btnSave.addActionListener(this);
		add(btnSave);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,350,420,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		loadCourses();

		setVisible(true);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}


	private void loadCourses()
	{
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{	
			cmbCourse.removeAllItems();
			cmbCourse.addItem("Select Courses");

			con=DBConnection.getConnection();
			stmt=con.createStatement();

			rs = stmt.executeQuery("Select * from course");

			while(rs.next())
			{
				cmbCourse.addItem(rs.getInt("c_id")+" - "+rs.getString("course_name"));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading course.");
		}
		finally
		{
			try
			{
				if(rs != null)
					rs.close();
			}
			catch(Exception e){}
			try
			{
				if(stmt != null)
					stmt.close();
			}
			catch(Exception e){	}
			try
			{
				if(con != null)
					con.close();
			}
			catch(Exception e)
			{
				
			}
		}
	}

	private int getSelectedCourseId()
	{
		String selected = (String)cmbCourse.getSelectedItem();

		if(selected == null || selected.equals("Select Courses"))
		{
			return -1;
		}

		String []parts=selected.split(" - ");
		return Integer.parseInt(parts[0]);
	}

	private void saveBatch()
	{
		int cId=getSelectedCourseId();
		String batchName=txtBatchName.getText().trim();
		String startDate=txtStartDate.getText().trim();
		String endDate= txtEndDate.getText().trim();
		String batchTime = txtTime.getText().trim();

		if(cId == -1 || batchName.equals("") || startDate.equals("") || endDate.equals("") || batchTime.equals(""))
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

			String query = "INSERT INTO batch(c_id,batch_name,batch_start_date,batch_end_date,batch_time) VALUES ('"
			+cId+"', '"
			+escapeValue(batchName)+"', '"
			+escapeValue(startDate)+"', '"
			+escapeValue(endDate)+"', '"
			+escapeValue(batchTime)+"')";

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Batch added Successfully.");
				cmbCourse.setSelectedIndex(0);
				txtBatchName.setText("");
				txtStartDate.setText("");
				txtEndDate.setText("");
				txtTime.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Batch not found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while saving batch.");
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
			saveBatch();
		}
	}
}
