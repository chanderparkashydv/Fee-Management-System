import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ManageFeesFrame extends JFrame implements ActionListener
{
	JLabel lblTitle;
	JButton btnAssign, btnDeposit, btnDelete, btnStatus, btnHistory;

	public ManageFeesFrame()
	{
		setTitle("Manage Fees");
		setSize(450,350);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		lblTitle = new JLabel("Manage Fees");
		lblTitle.setFont(new Font("Arial",Font.BOLD,22));
		lblTitle.setBounds(140,20,220,30);
		add(lblTitle);

		btnAssign = new JButton("Assign Fee");
		btnAssign.setBounds(70,80,150,35);
		btnAssign.addActionListener(this);
		add(btnAssign);

		btnDeposit = new JButton("Deposit Fee");
		btnDeposit.setBounds(230,80,150,35);
		btnDeposit.addActionListener(this);
		add(btnDeposit);

		btnDelete = new JButton("Delete Fee");
		btnDelete.setBounds(70,140,150,35);
		btnDelete.addActionListener(this);
		add(btnDelete);

		btnStatus = new JButton("Show Fee Status");
		btnStatus.setBounds(230,140,150,35);
		btnStatus.addActionListener(this);
		add(btnStatus);

		btnHistory = new JButton("Deposit History");
		btnHistory.setBounds(150,200,150,35);
		btnHistory.addActionListener(this);
		add(btnHistory);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnAssign)
		{
			new AssignFeeFrame();
		}
		else if(ae.getSource() == btnDeposit)
		{
			new DepositFeeFrame();
		}
		else if(ae.getSource() == btnDelete)
		{
			new DeleteFeesFrame();
		}
		else if(ae.getSource() == btnStatus)
		{
			new ShowFeeStatusFrame();
		}
		else if(ae.getSource() == btnHistory)
		{
			new ShowFeeDepositHistoryFrame();
		}
	}
}
