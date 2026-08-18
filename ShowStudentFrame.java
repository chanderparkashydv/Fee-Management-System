import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowStudentFrame extends JFrame implements ActionListener
{
	JLabel lblTitle, lblMessage;
	JTable table;
	JScrollPane scrollPane;
	JButton btnRefresh;
	DefaultTableModel model;

	public ShowStudentFrame()
	{
		setTitle("Show Students");
		setSize(800,420);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Student List");
		lblTitle.setBounds(340,20,150,30);
		add(lblTitle);

		model = new DefaultTableModel();
		model.addColumn("Student ID");
		model.addColumn("First Name");
		model.addColumn("Last Name");
		model.addColumn("Email");
		model.addColumn("Mobile");
		model.addColumn("DOB");

		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(30,70,720,240);
		add(scrollPane);

		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(340,325,100,30);
		btnRefresh.addActionListener(this);
		add(btnRefresh);

		lblMessage = new JLabel("");
		lblMessage.setBounds(30,360,300,20);
		add(lblMessage);

		loadStudents();

		setVisible(true);
	}

	private void loadStudents()
	{
		model.setRowCount(0);

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();
			String query = "select * from student";
			rs = stmt.executeQuery(query);

			boolean hasData = false;

			while(rs.next())
			{
				hasData = true;
				Object[] row =
				{
					rs.getInt("s_id"),
					rs.getString("firstname"),
					rs.getString("lastname"),
					rs.getString("email"),
					rs.getString("mobile"),
					rs.getString("dob")
				};
				model.addRow(row);
			}

			if(!hasData)
			{
				lblMessage.setText("No student Found.");
			}
			else
			{
				lblMessage.setText("");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Fetching students");
		}
		finally
		{
			try { if(rs != null) rs.close(); } catch(Exception e){}
			try { if(stmt != null) stmt.close(); } catch(Exception e){}
			try { if(con != null) con.close(); } catch(Exception e){}
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnRefresh)
		{
			loadStudents();
		}
	}
}
