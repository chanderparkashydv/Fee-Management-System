import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowFeeDepositHistoryFrame extends JFrame implements ActionListener 
{	
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowFeeDepositHistoryFrame()
	{
		setTitle("Fee Deposit History");
		setSize(1150,450);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Enrollment List");
		lblTitle.setBounds(510,20,180,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Deposit ID");
		model.addColumn("Student ID");
		model.addColumn("Student Name");
		model.addColumn("Batch Name");
		model.addColumn("Course Name");
		model.addColumn("Amount");
		model.addColumn("Date and Time");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,1080,280);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(510,365,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage=new JLabel("");
		lblMessage.setBounds(30,400,400,20);
		add(lblMessage);

		loadDepositHistory();

		setVisible(true);
	}

	private void loadDepositHistory()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs =	null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "SELECT fsd.fsd_id, fsd.s_id, s.firstname, s.lastname, b.batch_name, c.course_name, fsd.amount " +
               "FROM fees_student_deposit fsd, student s, batch b, course c " +
               "WHERE fsd.s_id = s.s_id " +
               "AND fsd.b_id = b.b_id " +
               "AND fsd.c_id = c.c_id " +
               "ORDER BY fsd.fsd_id DESC";

			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while (rs.next()) 
			{
				hasData = true;
				Object[] row = 
				{
					rs.getInt("fsd_id"),
					rs.getInt("s_id"),
					rs.getString("firstname")+" "+rs.getString("lastname"),
					rs.getString("batch_name"),
					rs.getString("course_name"),
					rs.getDouble("amount"),
					rs.getString("dateandtime")
				};
				model.addRow(row);	
			}

			if(!hasData)
			{
				lblMessage.setText("No deposit history Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching deposit history.");
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
			loadDepositHistory();
		}
	}
}
