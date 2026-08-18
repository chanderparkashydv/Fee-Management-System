import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AssignFeeFrame extends JFrame implements ActionListener
{
	JLabel lblTitle,lblEnrollment,lblFee,lblMessage,lblInfo;
	JComboBox<String> cmbEnrollment;
	JTextField txtFee;
	JButton btnLoadFee,btnAssign;

	int selectedStudentId = -1;
	int selectedBatchId = -1;
	int selectedCourseId = -1;

	public AssignFeeFrame()
	{
		setTitle("Assign Fee");
		setSize(700,350);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle=new JLabel("Assign Fee to Student");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(220,20,260,30);
		add(lblTitle);

		lblEnrollment=new JLabel("Enrollment");
		lblEnrollment.setBounds(50,90,120,25);
		add(lblEnrollment);

		cmbEnrollment = new JComboBox<>();
		cmbEnrollment.setBounds(170,90,450,25);
		add(cmbEnrollment);

		lblFee = new JLabel("Course Fee : ");
		lblFee.setBounds(50,140,120,25);
		add(lblFee);

		txtFee = new JTextField();
		txtFee.setBounds(170,140,200,25);
		add(txtFee);

		btnLoadFee = new JButton("Load Fee");
		btnLoadFee.setBounds(390,140,100,25);
		btnLoadFee.addActionListener(this);
		add(btnLoadFee);

		btnAssign = new JButton("Assign");
		btnAssign.setBounds(390,190,100,25);
		btnAssign.addActionListener(this);
		add(btnAssign);

		lblInfo = new JLabel("Select an enrolled student , then assign the course fee.");
		lblInfo.setBounds(170,245,380,20);
		lblInfo.setForeground(Color.BLUE);
		add(lblInfo);

		lblMessage = new JLabel("");
		lblMessage.setBounds(50,275,550,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		loadEnrollments();

		setVisible(true);
	}

	private void loadEnrollments()
	{
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{	
			cmbEnrollment.removeAllItems();
			cmbEnrollment.addItem("Select Enrollment");

			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query="select sb.sb_id, s.s_id, s.firstname, s.lastname,b.b_id,b.batch_name,c.c_id,c.course_name "
				+"from student_batch sb,student s, batch b, course c "
				+"where sb.s_id = s.s_id "
				+"AND sb.b_id = b.b_id "
				+"AND b.c_id = c.c_id";
				
			rs = stmt.executeQuery(query);

			while(rs.next())
			{
				String item = rs.getInt("s_id")+"|"+rs.getInt("b_id")+"|"+rs.getInt("c_id")
				+" - "+rs.getString("firstname")+" "+rs.getString("lastname")
				+" - "+rs.getString("batch_name")
				+" - "+rs.getString("course_name");

				cmbEnrollment.addItem(item);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading enrollments.");
		}
		finally
		{
			try
			{
				if(rs != null)
					rs.close();
			}
			catch(Exception ex){}

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

	private void loadFee()
	{
		String selected = (String)cmbEnrollment.getSelectedItem();

		if(selected == null || selected.equals("Select Enrollment"))
		{
			lblMessage.setText("Please select Enrollment.");
			return;
		}

		try
		{
			String firstPart=selected.split(" - ")[0];
			String []ids = firstPart.split("\\|");

			selectedStudentId = Integer.parseInt(ids[0]);
			selectedBatchId = Integer.parseInt(ids[1]);
			selectedCourseId = Integer.parseInt(ids[2]); 
		}
		catch (Exception e) 
		{
			lblMessage.setText("Unable to read Enrollment.");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "select course_fees from course where c_id = "+selectedCourseId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtFee.setText(rs.getString("course_fees"));
				lblMessage.setText("");
			}
			else
			{
				txtFee.setText("");
				lblMessage.setText("course fee not found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading fee.");
		}
		finally
		{
			try
			{
				if(rs != null)
					rs.close();
			}
			catch(Exception ex){}

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

	public void assignFee()
	{
		if(selectedStudentId == -1 || selectedBatchId == -1 || selectedCourseId == -1 
			|| txtFee.getText().trim().equals(""))
		{
			lblMessage.setText("Please load fee first.");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String checkQuery = "select * from fees_student where s_id ="+selectedStudentId
			+" AND b_id ="+ selectedBatchId+" And c_id= "+selectedCourseId;
			rs = stmt.executeQuery(checkQuery);

			if(rs.next())
			{
				lblMessage.setText("Fee already assigned for this Student and batch.");
				return;
			}
			rs.close();

			String insertQuery = "INSERT INTO fees_student(b_id,s_id,c_id,fees) values ("
			+selectedBatchId +" ,"
			+selectedStudentId+" ,"
			+selectedCourseId+" ,"
			+txtFee.getText().trim()+")";

			int result = stmt.executeUpdate(insertQuery);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Fee Assigned successfully.");
				cmbEnrollment.setSelectedIndex(0);
				txtFee.setText("");
				selectedStudentId = -1;
				selectedBatchId = -1;
				selectedCourseId = -1;
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Fee Assigned Failed.");
			}

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database Error while assigning fee.");
		}
		finally
		{
			try
			{
				if(rs != null)
					rs.close();
			}
			catch(Exception ex){}

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
		if(ae.getSource() == btnLoadFee)
		{
			loadFee();
		}
		else if(ae.getSource() == btnAssign)
		{
			assignFee();
		}
	}
}
