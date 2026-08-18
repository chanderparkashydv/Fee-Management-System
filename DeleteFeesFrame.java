import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteFeesFrame extends JFrame implements ActionListener
{
	JLabel lblTitle, lblId, lblStudent, lblBatch, lblCourse, lblFees, lblMessage;
	JTextField txtId, txtStudent, txtBatch, txtCourse, txtFees;
	JButton btnLoad, btnDelete;

	public DeleteFeesFrame()
	{
		setTitle("Delete Fee");
		setSize(580,430);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Delete Fee");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(210,20,180,30);
		add(lblTitle);

		lblId = new JLabel("Fee ID:");
		lblId.setBounds(60,70,120,25);
		add(lblId);

		txtId = new JTextField();
		txtId.setBounds(190,70,120,25);
		add(txtId);

		btnLoad = new JButton("Load");
		btnLoad.setBounds(330,70,80,25);
		btnLoad.addActionListener(this);
		add(btnLoad);

		lblStudent = new JLabel("Student:");
		lblStudent.setBounds(60,120,120,25);
		add(lblStudent);

		txtStudent = new JTextField();
		txtStudent.setBounds(190,120,250,25);
		txtStudent.setEditable(false);
		add(txtStudent);

		lblBatch = new JLabel("Batch:");
		lblBatch.setBounds(60,160,120,25);
		add(lblBatch);

		txtBatch = new JTextField();
		txtBatch.setBounds(190,160,250,25);
		txtBatch.setEditable(false);
		add(txtBatch);

		lblCourse = new JLabel("Course:");
		lblCourse.setBounds(60,200,120,25);
		add(lblCourse);

		txtCourse = new JTextField();
		txtCourse.setBounds(190,200,250,25);
		txtCourse.setEditable(false);
		add(txtCourse);

		lblFees = new JLabel("Fees:");
		lblFees.setBounds(60,240,120,25);
		add(lblFees);

		txtFees = new JTextField();
		txtFees.setBounds(190,240,250,25);
		txtFees.setEditable(false);
		add(txtFees);

		btnDelete = new JButton("Delete");
		btnDelete.setBounds(210,290,100,25);
		btnDelete.addActionListener(this);
		add(btnDelete);

		lblMessage = new JLabel("");
		lblMessage.setBounds(60,340,450,25);
		lblMessage.setForeground(Color.RED);
		add(lblMessage);

		setVisible(true);
	}

	private void loadFee()
	{
		String idText = txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please Enter Fee Id.");
			return;
		}

		int feeId = 0;
		try
		{
			feeId = Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid Fee Id");
			return;
		}

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "select fs.fs_id, s.firstname, s.lastname, b.batch_name, c.course_name, fs.fees "
			+ "from fees_student fs, student s, batch b, course c "
			+ "where fs.s_id = s.s_id "
			+ "AND fs.b_id = b.b_id "
			+ "AND fs.c_id = c.c_id "
			+ "AND fs.fs_id = " + feeId;

			rs = stmt.executeQuery(query);

			if(rs.next())
			{
				txtStudent.setText(rs.getString("firstname")+" "+rs.getString("lastname"));
				txtBatch.setText(rs.getString("batch_name"));
				txtCourse.setText(rs.getString("course_name"));
				txtFees.setText(rs.getString("fees"));
				lblMessage.setText("");
			}
			else
			{
				txtStudent.setText("");
				txtBatch.setText("");
				txtCourse.setText("");
				txtFees.setText("");
				lblMessage.setText("Fee record Not Found");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Error while loading fee.");
		}
		finally
		{
			try { if(rs != null) rs.close(); } catch(Exception e){}
			try { if(stmt != null) stmt.close(); } catch(Exception e){}
			try { if(con != null) con.close(); } catch(Exception e){}
		}
	}

	private void deleteFee()
	{
		String idText = txtId.getText().trim();

		if(idText.equals(""))
		{
			lblMessage.setText("Please Enter Fee Id.");
			return;
		}

		int feeId = 0;
		try
		{
			feeId = Integer.parseInt(idText);
		}
		catch (Exception e) 
		{
			lblMessage.setText("Please Enter Valid Fee Id.");
			return;
		}

		if(txtStudent.getText().trim().equals(""))
		{
			lblMessage.setText("Please load fee record first.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
			this,
			"Are you sure you want to delete this fee record?",
			"Confirm Delete ",
			JOptionPane.YES_NO_OPTION
		);

		if(confirm != JOptionPane.YES_OPTION)
		{
			return;
		}

		Connection con = null;
		Statement stmt = null;

		try
		{
			con = DBConnection.getConnection();
			stmt = con.createStatement();

			String query = "DELETE FROM fees_student WHERE fs_id = " + feeId;

			int result = stmt.executeUpdate(query);

			if(result > 0)
			{
				JOptionPane.showMessageDialog(this,"Fee record deleted successfully.");
				txtId.setText("");
				txtStudent.setText("");
				txtBatch.setText("");
				txtCourse.setText("");
				txtFees.setText("");
				lblMessage.setText("");
			}
			else
			{
				lblMessage.setText("Fee id not found");
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			lblMessage.setText("Database error while deleting fee.");
		}
		finally
		{
			try { if(stmt != null) stmt.close(); } catch(Exception ex){}
			try { if(con != null) con.close(); } catch(Exception ex){}
		}
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnLoad)
		{
			loadFee();
		}
		else if(ae.getSource() == btnDelete)
		{
			deleteFee();
		}
	}
}
