import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowBatchFrame extends JFrame implements ActionListener 
{	
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowBatchFrame()
	{
		setTitle("Show Batch");
		setSize(900,430);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Batch List");
		lblTitle.setBounds(390,20,150,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Batch ID");
		model.addColumn("Course Name");
		model.addColumn("Batch Name");
		model.addColumn("Start Date");
		model.addColumn("End Date");
		model.addColumn("Batch Time");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,820,250);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(390,335,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage=new JLabel("");
		lblMessage.setBounds(30,370,300,20);
		add(lblMessage);

		LoadBatches();

		setVisible(true);
	}

	private void LoadBatches()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs =	null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();
			String query="Select b.*,c.course_name from batch b,course c"
			+" Where b.c_id = c.c_id ";
			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while (rs.next()) 
			{
				hasData = true;
				Object[] row = 
				{
					rs.getInt("b_id"),
					rs.getString("course_name"),
					rs.getString("batch_name"),
					rs.getString("batch_start_date"),
					rs.getString("batch_end_date"),
					rs.getString("batch_time")
				};
				model.addRow(row);	
			}

			if(!hasData)
			{
				lblMessage.setText("No batch Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching batches");
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
			LoadBatches();
		}
	}
}
