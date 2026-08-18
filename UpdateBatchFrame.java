import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateBatchFrame extends JFrame implements ActionListener  
{
	JLabel lblTitle,lblId,lblCourse,lblBatchName,lblTime,lblStartDate,lblEndDate,lblNote,lblMessage;
	JTextField txtId, txtBatchName,txtTime,txtStartDate,txtEndDate;
	JComboBox<String> cmbCourse;
	JButton btnLoad,btnUpdate;
	public UpdateBatchFrame()
	{
		setTitle("UPDATE BATCH");
		setSize(580,450);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lblTitle=new JLabel("Update Batch");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(210,20,150,30);
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

		cmbCourse=new JComboBox<>();
		cmbCourse.setBounds(190,120,250,25);
		add(cmbCourse);

		lblBatchName=new JLabel("Batch Name:");
		lblBatchName.setBounds(60,160,120,25);
		add(lblBatchName);

		txtBatchName=new JTextField();
		txtBatchName.setBounds(190,160,250,25);
		add(txtBatchName);

		lblStartDate=new JLabel("Start Date ");
		lblStartDate.setBounds(60,200,120,25);
		add(lblStartDate);

		txtStartDate=new JTextField();
		txtStartDate.setBounds(190,200,250,25);
		add(txtStartDate);

		lblEndDate=new JLabel("End Date ");
		lblEndDate.setBounds(60,240,120,25);
		add(lblEndDate);

		txtEndDate=new JTextField();
		txtEndDate.setBounds(190,240,250,25);
		add(txtEndDate);

		lblTime=new JLabel("Batch Time");
		lblTime.setBounds(60,280,120,25);
		add(lblTime);

		txtTime=new JTextField();
		txtTime.setBounds(190,280,250,25);
		add(txtTime);

		lblNote=new JLabel("Use date format : YYYY-MM-DD");
		lblNote.setBounds(190,310,220,20);
		lblNote.setForeground(Color.BLUE);
		add(lblNote);

		btnUpdate=new JButton("UPDATE");
		btnUpdate.setBounds(210,340,100,25);
		btnUpdate.addActionListener(this);
		add(btnUpdate);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,390,450,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		loadCourse();

		setVisible(true);
	}

	private String escapeValue(String text)
	{
		return text.trim().replace("'","''");
	}

	private void loadCourse()
	{
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;

		try
		{	
			cmbCourse.removeAllItems();
			cmbCourse.addItem("Select Courses");

			con=DBConnection.getConnection();
			stmt=con.createStatement();

			rs = stmt.executeQuery("Select * from course");

			while(rs.next())
			{
				cmbCourse.addItem(rs.getInt("c_id")+" - "+rs.getString("course_name"));
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

	private int getSelectedCourseId()
	{
		String selected = (String) cmbCourse.getSelectedItem();

		if(selected == null || selected.equals("Select Courses"))
		{
			return -1;
		}

		String []parts=selected.split(" - ");
		return Integer.parseInt(parts[0]);
	}

	private void selectCourseInCombo(int courseId)
	{
		for (int i = 0; i < cmbCourse.getItemCount() ; i++ ) 
		{
			String item = cmbCourse.getItemAt(i);
			if( item.startsWith(courseId + " -"))
			{
				cmbCourse.setSelectedIndex(i);
				break;
			}
		}
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

			String query="Select * from batch Where b_id ="+batchId;
			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				int courseId = rs.getInt("c_id");
				selectCourseInCombo(courseId);
				txtBatchName.setText(rs.getString("batch_name"));
				txtStartDate.setText(rs.getString("batch_start_date"));
				txtEndDate.setText(rs.getString("batch_end_date"));
				txtTime.setText(rs.getString("batch_time"));
				lblMessage.setText("");	
			}
			else
			{
				cmbCourse.setSelectedIndex(0);
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

	private void updateBatch()
	{
		String idText = txtId.getText().trim();
		int cId=getSelectedCourseId();
		String batchName=txtBatchName.getText().trim();
		String startDate=txtStartDate.getText().trim();
		String endDate= txtEndDate.getText().trim();
		String batchTime = txtTime.getText().trim();

		if(idText.equals("") || cId == -1 || batchName.equals("") || startDate.equals("") || endDate.equals("") || batchTime.equals(""))
		{
			lblMessage.setText("All fields are required.");
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

		Connection con=null;
		Statement stmt=null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "UPDATE batch set c_id= "
			+cId+" , "
			+"batch_name = '"+escapeValue(batchName)+"', "
			+"batch_start_date ='"+escapeValue(startDate)+"', "
			+"batch_end_date ='"+escapeValue(endDate)+"', "
			+"batch_time ='"+escapeValue(batchTime)+"' "
			+"WHERE b_id = "+batchId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Batch Updated Successfully.");
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
			lblMessage.setText("Database error while Updating batch.");
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
		else if(ae.getSource() == btnUpdate)
		{
			updateBatch();
		}
	}
}
