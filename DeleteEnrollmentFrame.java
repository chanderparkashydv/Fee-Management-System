import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteEnrollmentFrame extends JFrame implements ActionListener  
{
	JLabel lblTitle, lblId,lblStudent,lblBatch, lblCourse,lblMessage;
	JTextField txtId,txtStudent, txtBatch, txtCourse;
	JButton btnLoad, btnRemove;
	public DeleteEnrollmentFrame()
	{
		setTitle("Remove Enrollment");
		setSize(600,380);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblTitle=new JLabel("Remove Enrollment");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(190,20,220,30);
		add(lblTitle);

		lblId=new JLabel("Enrollment ID:");
		lblId.setBounds(60,80,120,25);
		add(lblId);

		txtId=new JTextField();
		txtId.setBounds(190,80,120,25);
		add(txtId);

		btnLoad=new JButton("Load");
		btnLoad.setBounds(330,80,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblStudent=new JLabel("Student :");
		lblStudent.setBounds(60,130,120,25);
		add(lblStudent);

		txtStudent=new JTextField();
		txtStudent.setBounds(190,130,250,25);
		txtStudent.setEditable(false);
		add(txtStudent);

		lblBatch=new JLabel("Last Name:");
		lblBatch.setBounds(60,170,120,25);
		add(lblBatch);

		txtBatch=new JTextField();
		txtBatch.setBounds(190,170,250,25);
		txtBatch.setEditable(false);
		add(txtBatch);

		lblCourse=new JLabel("Course");
		lblCourse.setBounds(60,210,120,25);
		add(lblCourse);

		txtCourse=new JTextField();
		txtCourse.setBounds(190,210,250,25);
		txtCourse.setEditable(false);
		add(txtCourse);

		btnRemove=new JButton("Remove");
		btnRemove.setBounds(220,265,100,25);
		btnRemove.addActionListener(this);
		add(btnRemove);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,315,430,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
	}

	private void loadEnrollment()
	{
		String idText =txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please enter enrollment id.");
			return;
		}

		int sbId=0;

		try
		{
			sbId=Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid enrollment id");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{
			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query = "SELECT sb.sb_id, s.firstname, s.lastname, b.batch_name, c.course_name " +
               "FROM student_batch sb, student s, batch b, course c " +
               "WHERE sb.s_id = s.s_id " +
               "AND sb.b_id = b.b_id " +
               "AND b.c_id = c.c_id " +
               "AND sb.sb_id = " + sbId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtStudent.setText(rs.getString("firstname")+" "+rs.getString("lastname"));
				txtBatch.setText(rs.getString("batch_name"));
				txtCourse.setText(rs.getString("course_name"));
				lblMessage.setText("");
			}
			else
			{
				txtStudent.setText("");
				txtCourse.setText("");
				txtBatch.setText("");
				lblMessage.setText("Enrollment Not Found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading enrollment.");
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
			catch(Exception e) {}

		}
	}

	private void removeEnrollment()
	{
		String idText = txtId.getText().trim();

		if(idText.equals("") )
		{
			lblMessage.setText("Please Enter enrollment Id.");
			return;
		}

		int sbId=0;
		try
		{
			sbId = Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid enrollment id.");			
			return;
		}

		if(txtStudent.getText().trim().equals(""))
		{
			lblMessage.setText("Please load enrollment first.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
			this,
			"Are you sure you want to remove this batch?",
			"Confirm Delete ",
			JOptionPane.YES_NO_OPTION
		);

		if(confirm != JOptionPane.YES_OPTION)
		{
			return;
		}

		Connection con=null;
		Statement stmt=null;

		try
		{
			con = DBConnection.getConnection();
			stmt=con.createStatement();

			String query="Delete from student_batch Where sb_id="+sbId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Enrollment Removed successfully.");
				txtId.setText("");
				txtStudent.setText("");
				txtBatch.setText("");
				txtCourse.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Enrollment id not found");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Deleting enrollment.");
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
		if(ae.getSource() == btnLoad)
		{
			loadEnrollment();
		}
		else if(ae.getSource() == btnRemove)
		{
			removeEnrollment();
		}
	}
}