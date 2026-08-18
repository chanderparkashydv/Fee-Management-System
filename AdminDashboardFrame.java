import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboardFrame extends JFrame implements ActionListener
{
	JLabel lblTitle;
	JButton btnCourse, btnBatch, btnStudent, btnEnroll, btnShowEnroll, btnDeleteEnroll, btnFees, btnLogout;

	public AdminDashboardFrame()
	{
		setTitle("Admin Dashboard");
		setSize(500,450);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lblTitle = new JLabel("Fee Management System");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(90,20,350,30);
		add(lblTitle);

		btnCourse = new JButton("Manage Course");
		btnCourse.setBounds(60,80,180,35);
		btnCourse.addActionListener(this);
		add(btnCourse);

		btnBatch = new JButton("Manage Batch");
		btnBatch.setBounds(260,80,180,35);
		btnBatch.addActionListener(this);
		add(btnBatch);

		btnStudent = new JButton("Manage Student");
		btnStudent.setBounds(60,130,180,35);
		btnStudent.addActionListener(this);
		add(btnStudent);

		btnEnroll = new JButton("Enroll Student");
		btnEnroll.setBounds(260,130,180,35);
		btnEnroll.addActionListener(this);
		add(btnEnroll);

		btnShowEnroll = new JButton("Show Enrollments");
		btnShowEnroll.setBounds(60,180,180,35);
		btnShowEnroll.addActionListener(this);
		add(btnShowEnroll);

		btnDeleteEnroll = new JButton("Remove Enrollment");
		btnDeleteEnroll.setBounds(260,180,180,35);
		btnDeleteEnroll.addActionListener(this);
		add(btnDeleteEnroll);

		btnFees = new JButton("Manage Fees");
		btnFees.setBounds(60,230,180,35);
		btnFees.addActionListener(this);
		add(btnFees);

		btnLogout = new JButton("Logout");
		btnLogout.setBounds(260,230,180,35);
		btnLogout.addActionListener(this);
		add(btnLogout);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnCourse)
		{
			new ManageCourseFrame();
		}
		else if(ae.getSource() == btnBatch)
		{
			new ManageBatchFrame();
		}
		else if(ae.getSource() == btnStudent)
		{
			new ManageStudentFrame();
		}
		else if(ae.getSource() == btnEnroll)
		{
			new EnrollStudentFrame();
		}
		else if(ae.getSource() == btnShowEnroll)
		{
			new ShowEnrollmentFrame();
		}
		else if(ae.getSource() == btnDeleteEnroll)
		{
			new DeleteEnrollmentFrame();
		}
		else if(ae.getSource() == btnFees)
		{
			new ManageFeesFrame();
		}
		else if(ae.getSource() == btnLogout)
		{
			dispose();
			new AdminLoginFrame();
		}
	}
}
