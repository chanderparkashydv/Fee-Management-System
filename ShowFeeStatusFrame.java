import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowFeeStatusFrame extends JFrame implements ActionListener 
{	
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowFeeStatusFrame()
	{
		setTitle("Show Fee History");
		setSize(1100,450);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Fee Status");
		lblTitle.setBounds(500,20,150,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Student ID");
		model.addColumn("Student Name");
		model.addColumn("Batch Name");
		model.addColumn("Course Name");
		model.addColumn("Total Fee");
		model.addColumn("Deposited");
		model.addColumn("Pending");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,1020,280);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(500,365,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage=new JLabel("");
		lblMessage.setBounds(30,400,400,20);
		add(lblMessage);

		loadFeeStatus();

		setVisible(true);
	}

	private void loadFeeStatus()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs =	null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "SELECT fs.s_id, s.firstname, s.lastname, b.batch_name, c.course_name, fs.fees, " +
           "IFNULL((SELECT SUM(fsd.amount) FROM fees_student_deposit fsd " +
           "WHERE fsd.s_id = fs.s_id AND fsd.b_id = fs.b_id AND fsd.c_id = fs.c_id), 0) AS deposited " +
           "FROM fees_student fs, student s, batch b, course c " +
           "WHERE fs.s_id = s.s_id " +
           "AND fs.b_id = b.b_id " +
           "AND fs.c_id = c.c_id";

			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while (rs.next()) 
			{
				hasData = true;
				
				double totalFee = rs.getDouble("fees");
				double deposited = rs.getDouble("deposited");
				double pending = totalFee - deposited;
				if(pending < 0) pending = 0;

				Object []row = {
							rs.getInt("s_id"),
							rs.getString("firstname")+" "+rs.getString("lastname"),
							rs.getString("batch_name"),
							rs.getString("course_name"),
							totalFee,
							deposited,
							pending
				};

				model.addRow(row);	
			}

			if(!hasData)
			{
				lblMessage.setText("No fee record Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching Fee Status.");
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
			loadFeeStatus();
		}
	}
}
