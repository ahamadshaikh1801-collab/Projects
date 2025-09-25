package GuiProject;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class MainApp {
	
    JFrame frame = new JFrame();
    JPanel panel1 = new JPanel();
    CardLayout cl = new CardLayout();
    JTextField tf1 = new JTextField();
    private String cuur_card;

    Userserv us = new Userserv();
    TransactionServ ts = new TransactionServ();

    public void createAndShowGUI() {
    	
        frame.setTitle("ATM Machine");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(new Color(220,220,220));
        panel1.setLayout(cl);
        panel1.add(createCardEntryPanel(), "CARD");
        panel1.add(createMainMenuPanel(), "MENU");
        
        frame.add(panel1);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JPanel createCardEntryPanel() {
    	
        JPanel panel2 = new JPanel(new GridLayout(4, 1));
        panel2.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel2.setBackground(new Color(225,225,235));
        
        Border border=BorderFactory.createLineBorder(Color.black,3);
        JLabel label1 = new JLabel("Enter Card Number:");
        label1.setFont(new Font("Araial",Font.BOLD,20));
        JButton nextb = new JButton("Proceed");
        nextb.setBackground(new Color(218,165,32));
        nextb.setOpaque(true);
        nextb.setFont(new Font("Arila",Font.BOLD,20));
        nextb.setBorder(border);
        tf1.setFont(new Font("Arial",Font.HANGING_BASELINE,20));
        tf1.setBorder(border);
        

        nextb.addActionListener(e -> {
            cuur_card = tf1.getText().trim();
            if (cuur_card.isEmpty()) {
                showMessage("Please Enter the Card Number:");
                return;
            }
            try {
                boolean verified = us.verifyCardGUI(cuur_card);
                if (verified) {
                    cl.show(panel1, "MENU");
                } else {
                    showMessage("Card Verification Failed..");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showMessage("Error verifying card.");
            }
        });

        panel2.add(label1);
        panel2.add(tf1);
        panel2.add(new JLabel(""));
        panel2.add(nextb);
        return panel2;
    }

    public JPanel createMainMenuPanel() {
    	Border bordern1=BorderFactory.createLineBorder(Color.black,3);
    	JPanel panel3 = new JPanel(new GridLayout(6, 1, 10, 10));
        panel3.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel3.setBackground(new Color(225,225,235));

        JLabel label2 = new JLabel("Select an Option..", SwingConstants.CENTER);
        label2.setForeground(Color.BLACK);
        label2.setBounds(200,10,100,30);
        label2.setFont(new Font("Californian FB",Font.BOLD,24));
        JButton withdraw = new JButton("Withdraw");
        withdraw.setBorder(bordern1);
        withdraw.setForeground(Color.BLACK);
        withdraw.setBackground(new Color(218,165,32));
        withdraw.setOpaque(true);
        withdraw.setFont(new Font("Arial",Font.BOLD,20));
        JButton deposit = new JButton("Deposit");
        deposit.setBorder(bordern1);
        deposit.setForeground(Color.WHITE);
        deposit.setBackground(new Color(64, 64, 64));
        deposit.setOpaque(true);
        deposit.setFont(new Font("Arial",Font.BOLD,20));
        JButton check = new JButton("Check Balance");
        check.setBorder(bordern1);
        check.setForeground(Color.BLACK);
        check.setBackground(new Color(218,165,32));
        check.setOpaque(true);
        check.setFont(new Font("Arial",Font.BOLD,20));
        JButton quick = new JButton("Quick Withdraw");
        quick.setBorder(bordern1);
        quick.setForeground(Color.WHITE);
        quick.setBackground(new Color(64, 64, 64));
        quick.setOpaque(true);
        quick.setFont(new Font("Arial",Font.BOLD,20));
        JButton EXIT = new JButton("EXIT");
        EXIT.setBorder(bordern1);
        EXIT.setForeground(Color.BLACK);
        EXIT.setBackground(new Color(218,165,32));
        EXIT.setOpaque(true);
        EXIT.setFont(new Font("Arial",Font.BOLD,20));
        
       
        withdraw.addActionListener(e -> {
        	ts.withdraw(cuur_card);
        	tf1.setText("");
        	cl.show(panel1, "CARD");
        	
        });
        
        deposit.addActionListener(e -> {
        	ts.deposit(cuur_card);
        	tf1.setText("");
        	cl.show(panel1, "CARD");
        	});
        check.addActionListener(e ->{
        	ts.checkBalance(cuur_card);
        	tf1.setText("");
        	cl.show(panel1, "CARD");
        	});
        quick.addActionListener(e ->{ 
        	ts.quickWithdraw(cuur_card);
        	tf1.setText("");
        	cl.show(panel1, "CARD");
        
        });
        EXIT.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
				
			}
		} );



        panel3.add(label2);
        panel3.add(withdraw);
        panel3.add(deposit);
        panel3.add(check);
        panel3.add(quick);
        panel3.add(EXIT);
        
        return panel3;
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }
    
}
