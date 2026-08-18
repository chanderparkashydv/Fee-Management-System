import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowCourseFrame extends JFrame implements ActionListener 
{	
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowCourseFrame()
	{
		setTitle("Show Courses");
		setSize(650,400);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Course List");
		lblTitle.setBounds(260,20,150,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Course ID");
		model.addColumn("Course Name");
		model.addColumn("Duration");
		model.addColumn("Fees");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,570,220);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(260,310,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage=new JLabel("");
		lblMessage.setBounds(30,345,300,20);
		add(lblMessage);

		LoadCourses();

		setVisible(true);
	}

	private void LoadCourses()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs =	null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();
			String query = "select * from course";
			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while (rs.next()) 
			{
				hasData = true;
				Object[] row = 
				{
					rs.getInt("c_id"),
					rs.getString("course_name"),
					rs.getString("course_duration"),
					rs.getString("course_fees")
				};
				model.addRow(row);	
			}

			if(!hasData)
			{
				lblMessage.setText("No course Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching courses");
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
			catch(Exception e){	}
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnRefresh)
		{
			LoadCourses();
		}
	}
}
