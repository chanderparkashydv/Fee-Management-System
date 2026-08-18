import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteCourseFrame extends JFrame implements ActionListener
{	
	JLabel lblTitle,  lblID, lblMessage, lblName, lblDuration, lblFees;
	JTextField txtId, txtName,txtDuration,txtFees;
	JButton btnLoad,btnDelete;
	public DeleteCourseFrame()
	{
		setTitle("Delete Course");
		setSize(500,380);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle=new JLabel("Delete Course");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(160,20,180,30);
		add(lblTitle);

		lblID=new JLabel("Course ID:");
		lblID.setBounds(60,80,100,25);
		add(lblID);

		txtId=new JTextField();
		txtId.setBounds(180,80,120,25);
		add(txtId);

		btnLoad=new JButton("Load");
		btnLoad.setBounds(320,80,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblName=new JLabel("Course Name: ");
		lblName.setBounds(60,130,100,25);
		add(lblName);

		txtName = new JTextField();
		txtName. setBounds(180,130, 220,25);
		txtName.setEditable(false);
		add(txtName);
		
		lblDuration = new JLabel("Duration:");
		lblDuration.setBounds(60,170,100,25);
		add(lblDuration);
		
		txtDuration = new JTextField();
		txtDuration.setBounds(180,170,220,25);
		txtDuration.setEditable(false);

		add(txtDuration);

		lblFees = new JLabel("Fees:");
		lblFees.setBounds(60,210,100,25);
		add(lblFees);

		txtFees = new JTextField();
		txtFees.setBounds(180,210,220,25);
		txtFees.setEditable(false);

		add(txtFees);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(180,260,100,35);
		btnDelete.addActionListener(this);
		add(btnDelete);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,310,350,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	private void LoadCourse()
	{
		String idText =txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please enter course id.");
			return;
		}

		int courseId=0;

		try
		{
			courseId=Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid course id");
			return;
		}

		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{
			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query="Select * from course Where c_id ="+courseId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtName.setText(rs.getString("course_name"));
				txtDuration.setText(rs.getString("course_duration"));
				txtFees.setText(rs.getString("course_fees"));
				lblMessage.setText("");	
			}
			else
			{
				txtName.setText("");
				txtDuration.setText("");
				txtFees.setText("");
				lblMessage.setText("Course Not Found");
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
			catch(Exception e)
			{

			}
			try
			{
				if(stmt != null)
					stmt.close();
			}
			catch(Exception e)
			{

			}
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

	private void deleteCourse()
	{
		String idText=txtId.getText().trim();
		

		if(idText.equals(""))
		{
			lblMessage.setText("Please Enter Course id.");
			return;
		}

		int courseId=0;

		try
		{
			courseId = Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid course id.");			
			return;
		}

		if(txtName.getText().trim().equals(""))
		{
			lblMessage.setText("Please load course First.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
			this,
			"Are you sure you want to delete this course?",
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

			String query="Delete From course where c_id="+courseId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Course delete successfully.");
				txtId.setText("");
				txtName.setText("");
				txtDuration.setText("");
				txtFees.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Course id not found");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while Deleting Course.");
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
			LoadCourse();
		}
		else if (ae.getSource() == btnDelete)
		{
			deleteCourse();
		}
	}
}