import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowEnrollmentFrame extends JFrame implements ActionListener 
{	
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowEnrollmentFrame()
	{
		setTitle("Show Enrollments");
		setSize(950,430);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Enrollment List");
		lblTitle.setBounds(410,20,150,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Enrollment ID");
		model.addColumn("Student ID");
		model.addColumn("Student Name");
		model.addColumn("Batch ID");
		model.addColumn("Batch Name");
		model.addColumn("Course Name");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,870,250);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(410,335,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage=new JLabel("");
		lblMessage.setBounds(30,370,400,20);
		add(lblMessage);

		loadEnrollments();

		setVisible(true);
	}

	private void loadEnrollments()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs =	null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();
			String query = "select sb.sb_id,s.s_id, s.firstname, s.lastname, "
			+"b.b_id,b.batch_name, c.course_name "
			+"from student_batch sb,student s,batch b,course c"
			+"where sb.s_id = s.s_id"
			+" AND sb.b_id = b.b_id"
			+" AND b.c_id = c.c_id";

			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while (rs.next()) 
			{
				hasData = true;
				Object[] row = 
				{
					rs.getInt("sb_id"),
					rs.getInt("s_id"),
					rs.getString("firstname")+" "+rs.getString("lastname"),
					rs.getInt("b_id"),
					rs.getString("batch_name"),
					rs.getString("course_name")
				};
				model.addRow(row);	
			}

			if(!hasData)
			{
				lblMessage.setText("No Enrollment Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching Enrollments.");
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
			loadEnrollments();
		}
	}
}
