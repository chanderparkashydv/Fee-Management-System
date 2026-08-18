import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ManageCourseFrame extends JFrame implements ActionListener
{	
	JLabel lblTitle;
	JButton btnAdd,btnUpdate,btnDelete,btnShow;
	public ManageCourseFrame()
	{
		setTitle("Manage Course");
		setSize(450,300);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


		lblTitle=new JLabel("Manage Course");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(130,25,200,30);
		add(lblTitle);

		btnAdd=new JButton("Add");
		btnAdd.setBounds(70,90,120,35);
		btnAdd.addActionListener(this);
		add(btnAdd);

		btnUpdate=new JButton("Update");
		btnUpdate.setBounds(230,90,120,35);
		btnUpdate.addActionListener(this);
		add(btnUpdate);

		btnDelete=new JButton("Delete");
		btnDelete.setBounds(70,150,120,35);
		btnDelete.addActionListener(this);
		add(btnDelete);

		btnShow=new JButton("Show");
		btnShow.addActionListener(this);
		btnShow.setBounds(230,150,120,35);
		add(btnShow);
		
		setVisible(true);
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnAdd)
		{
			new AddCourseFrame();
		}
		else if(ae.getSource() == btnUpdate)
		{
			new UpdateCourseFrame();
		}
		else if(ae.getSource() == btnDelete)
		{
			new DeleteCourseFrame();
		}
		else if(ae.getSource() == btnShow)
		{
			new ShowCourseFrame();
		}
	}
}