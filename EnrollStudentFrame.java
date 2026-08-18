import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EnrollStudentFrame extends JFrame implements ActionListener
{	
	JLabel lblTitle,lblStudent,lblBatch,lblMessage;
	JComboBox<String> cmbStudent,cmbBatch;
	JButton btnEnroll;
	public EnrollStudentFrame()
	{
		setTitle("Enroll Student");
		setSize(550,300);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle=new JLabel("Enroll Student into Batch");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(130,20,280,30);
		add(lblTitle);

		lblStudent = new JLabel("Student : ");
		lblStudent.setBounds(60,90,100,25);
		add(lblStudent);

		cmbStudent = new JComboBox<>();
		cmbStudent.setBounds(180,90,300,25);
		add(cmbStudent);

		lblBatch = new JLabel("Batch : ");
		lblBatch.setBounds(60,135,100,25);
		add(lblBatch);

		cmbBatch = new JComboBox<>();
		cmbBatch.setBounds(180,135,300,25);
		add(cmbBatch);

		btnEnroll = new JButton("Enroll");
		btnEnroll.setBounds(210,185,100,35);
		btnEnroll.addActionListener(this);
		add(btnEnroll);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,230,420,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		loadStudent();
		loadBatches();

		setVisible(true);
	}

	private void loadStudent()
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			cmbStudent.removeAllItems();
			cmbStudent.addItem("Select Student");

			con=DBConnection.getConnection();
			stmt=con.createStatement();
			rs=stmt.executeQuery("Select * from student");

			while (rs.next()) 
			{
				cmbStudent.addItem(rs.getInt("s_id")+" - "
					+rs.getString("firstname")+ " "
					+rs.getString("lastname"));	
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading Students.");
		}
		finally
		{
			try	
			{
				if(rs!=null)
					rs.close();
			}
			catch (Exception e) 
			{
				
			}
			try	
			{
				if(stmt!=null)
					stmt.close();
			}
			catch (Exception e) 
			{
				
			}
			try	
			{
				if(con!=null)
					con.close();
			}
			catch (Exception e) 
			{
				
			}
		}	
	}

	private void loadBatches()
	{
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			cmbBatch.removeAllItems();
			cmbBatch.addItem("Select Batch");

			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query = "select b.*,c.course_name from batch b, course c "
				+"Where b.c_id = c.c_id";
			rs=stmt.executeQuery(query);

			while (rs.next()) 
			{
				cmbBatch.addItem(rs.getInt("b_id")+" - "
					+rs.getString("batch_name")+ " ( "
					+rs.getString("course_name")+" ) ");	
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading Batches.");
		}
		finally
		{
			try	
			{
				if(rs!=null)
					rs.close();
			}
			catch (Exception e)	{}

			try	
			{
				if(stmt!=null)
					stmt.close();
			}
			catch (Exception e)	{}

			try	
			{
				if(con!=null)
					con.close();
			}
			catch (Exception e)	{}
		}
	}

	private int getSelectedStudentId()
	{
		String selected = (String)cmbStudent.getSelectedItem();

		if(selected == null || selected.equals("Select Student"))
		{
			return -1;
		}

		String []parts=selected.split(" - ");
		return Integer.parseInt(parts[0]);
	}

	private int getSelectedBatchId()
	{
		String selected = (String)cmbBatch.getSelectedItem();

		if(selected == null || selected.equals("Select Batch"))
		{
			return -1;
		}

		String []parts=selected.split(" - ");
		return Integer.parseInt(parts[0]);
	}

	private void enrollStudent()
	{
		int sId=getSelectedStudentId();
		int bId=getSelectedBatchId();

		if(sId == -1 || bId == -1)
		{
			lblMessage.setText("Please select student and batch.");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String checkQuery = "select * from student_batch Where s_id = "+sId+" AND b_id = "+bId;
			rs = stmt.executeQuery(checkQuery);

			if(rs.next())
			{
				lblMessage.setText("This Student is already enrolled in this batch.");
				return ;
			}
			rs.close();

			String insertQuery = "INSERT INTO student_batch (s_id,b_id) VALUES ("+sId +" , "+bId+")";
			int result = stmt.executeUpdate(insertQuery);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Student added Successfully.");
				cmbStudent.setSelectedIndex(0);
				cmbBatch.setSelectedIndex(0);
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Enrollment failed.");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while Enrollment.");
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
		if(ae.getSource() == btnEnroll)
		{
			enrollStudent();
		}
	}
}
