import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteBatchFrame extends JFrame implements ActionListener  
{
	JLabel lblTitle,lblId,lblCourse,lblBatchName,lblTime,lblStartDate,lblEndDate,lblMessage;
	JTextField txtId, txtCourse, txtBatchName,txtTime,txtStartDate,txtEndDate;
	JComboBox<String> cmbCourse;
	JButton btnLoad,btnDelete;
	public DeleteBatchFrame()
	{
		setTitle("Delete BATCH");
		setSize(580,430);
		setLayout(null);
		setLocationRelativeTo(null);
		
		lblTitle=new JLabel("Delete Batch");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(210,20,180,30);
		add(lblTitle);

		lblId=new JLabel("Batch ID:");
		lblId.setBounds(60,70,120,25);
		add(lblId);

		txtId=new JTextField();
		txtId.setBounds(190,70,120,25);
		add(txtId);

		btnLoad=new JButton("Load");
		btnLoad.setBounds(330,70,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblCourse=new JLabel("Course :");
		lblCourse.setBounds(60,120,120,25);
		add(lblCourse);

		txtCourse = new JTextField();
		txtCourse.setBounds(190,120,250,25);
		txtCourse.setEditable(false);
		add(txtCourse);

		lblBatchName=new JLabel("Batch Name:");
		lblBatchName.setBounds(60,160,120,25);
		add(lblBatchName);

		txtBatchName=new JTextField();
		txtBatchName.setBounds(190,160,250,25);
		txtBatchName.setEditable(false);
		add(txtBatchName);

		lblStartDate=new JLabel("Start Date ");
		lblStartDate.setBounds(60,200,120,25);
		add(lblStartDate);

		txtStartDate=new JTextField();
		txtStartDate.setBounds(190,200,250,25);
		txtStartDate.setEditable(false);

		add(txtStartDate);

		lblEndDate=new JLabel("End Date ");
		lblEndDate.setBounds(60,240,120,25);
		add(lblEndDate);

		txtEndDate=new JTextField();
		txtEndDate.setBounds(190,240,250,25);
		txtEndDate.setEditable(false);
		add(txtEndDate);

		lblTime=new JLabel("Batch Time");
		lblTime.setBounds(60,280,120,25);
		add(lblTime);

		txtTime=new JTextField();
		txtTime.setBounds(190,280,250,25);
		txtTime.setEditable(false);
		add(txtTime);
		
		btnDelete=new JButton("Delete");
		btnDelete.setBounds(210,340,100,25);
		btnDelete.addActionListener(this);
		add(btnDelete);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,85,450,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void loadBatch()
	{
		String idText = txtId.getText().trim();
		if(idText.equals(""))
		{
			lblMessage.setText("Please Enter Batch Id.");
			return;
		}

		int batchId = 0;
		try
		{
			batchId = Integer.parseInt(idText);
		}
		catch (Exception ex) 
		{
			lblMessage.setText("Please Enter Valid batch Id");
			return;
		}

		Connection con = null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{
			con=DBConnection.getConnection();
			stmt=con.createStatement();

			String query="Select b.*,c.course_name from batch b,course c"
			+" Where b.c_id = c.c_id AND b.b_id = "+batchId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtCourse.setText(rs.getString("course_name"));
				txtBatchName.setText(rs.getString("batch_name"));
				txtStartDate.setText(rs.getString("batch_start_date"));
				txtEndDate.setText(rs.getString("batch_end_date"));
				txtTime.setText(rs.getString("batch_time"));
				lblMessage.setText("");	
			}
			else
			{
				txtCourse.setText("");
				txtBatchName.setText("");
				txtStartDate.setText("");
				txtEndDate.setText("");
				txtTime.setText("");
				lblMessage.setText("Batch Not Found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading Batch.");
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
			catch(Exception e)
			{
				
			}
		}
	}

	private void deleteBatch()
	{
		String idText = txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please Enter Batch Id.");
			return;
		}

		int batchId = 0;
		try
		{
			batchId = Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid batch Id.");
			return;
		}

		if(txtBatchName.getText().trim().equals(""))
		{
			lblMessage.setText("Please load batch First.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
			this,
			"Are you sure you want to delete this batch?",
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
			stmt = con.createStatement();

			String query = "DELETE from batch WHERE b_id = "+batchId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Batch deleted Successfully.");
				txtId.setText("");
				txtCourse.setText("");
				txtBatchName.setText("");
				txtStartDate.setText("");
				txtEndDate.setText("");
				txtTime.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Batch id not found");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while deleting batch.");
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
			loadBatch();
		}
		else if(ae.getSource() == btnDelete)
		{
			deleteBatch();
		}
	}
}