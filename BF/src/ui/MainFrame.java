package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rmi.RemoteHelper;
import service.IOService;
import service.UserService;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private IOService ioService;
	private UserService userService;
	
	private boolean isLogin;
	private boolean toLogin;
	private String username;
	
	private JTextArea textArea;
	private JLabel resultLabel;
	
	private JMenu userMenu;
	private JMenuItem logMenuItem;

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton confirmButton;
	private JButton cancelButton;
	
	public MainFrame() {
		
		// 鍒涘缓绐椾綋
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 400);
		frame.setLocation(400, 200);

		usernameField = new JTextField("Username");
		passwordField = new JPasswordField("Password");
		confirmButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");
		
		Dimension fieldSize = new Dimension(150, 30);
		Dimension buttonSize = new Dimension(75, 30);
		usernameField.setSize(fieldSize);
		passwordField.setSize(fieldSize);
		confirmButton.setSize(buttonSize);
		cancelButton.setSize(buttonSize);
		
		int startX = (int) ((frame.getWidth() - fieldSize.getWidth()) / 2);
		int startY = (int) frame.getHeight() / 4;
		usernameField.setLocation(startX, startY);
		passwordField.setLocation(startX, (int) (startY + fieldSize.getHeight() + 10));
		confirmButton.setLocation(startX, (int) (startY + fieldSize.getHeight() * 2 + 10 * 2));
		cancelButton.setLocation((int) (startX + buttonSize.getWidth()), 
				(int) (startY + fieldSize.getHeight() * 2 + 10 * 2));
		confirmButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				String name = usernameField.getText();
				String psw = passwordField.getPassword().toString();
				try {
					isLogin = userService.login(name, psw);
					if(isLogin){
						
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		frame.add(usernameField);
		frame.add(passwordField);
		frame.add(confirmButton);
		frame.add(cancelButton);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		JMenu runMenu = new JMenu("Run");
		menuBar.add(runMenu);
		JMenuItem executeMenuItem = new JMenuItem("Execute");
		runMenu.add(executeMenuItem);
		
		JMenu versionMenu = new JMenu("Version");
		menuBar.add(versionMenu);
		
		userMenu = new JMenu();
		menuBar.add(userMenu);
		logMenuItem = new JMenuItem();
		userMenu.add(logMenuItem);
		
		frame.setJMenuBar(menuBar);

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new SaveActionListener());
		exitMenuItem.addActionListener(new MenuItemActionListener());
		executeMenuItem.addActionListener(new MenuItemActionListener());
		logMenuItem.addActionListener(new MenuItemActionListener());
		
		textArea = new JTextArea();
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setBackground(Color.LIGHT_GRAY);
		frame.add(textArea, BorderLayout.CENTER);

		isLogin = false;
		toLogin = false;
		username = "Welcome, Guest!";
		setUserModule();
		setToLogin(toLogin);
		
		// 鏄剧ず缁撴灉
		resultLabel = new JLabel();
		resultLabel.setText("result");
		frame.add(resultLabel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void setUserModule(){
		userMenu.setText(username);
		if(isLogin){
			logMenuItem.setText("Log out");
		}
		else{
			logMenuItem.setText("Log in");
		}
	}
	
	private void setToLogin(boolean _t){
		usernameField.setVisible(_t);
		passwordField.setVisible(_t);
		confirmButton.setVisible(_t);
		cancelButton.setVisible(_t);
		textArea.setVisible(!_t);
		getContentPane().revalidate();
	}
	

	class MenuItemActionListener implements ActionListener {
		/**
		 * 瀛愯彍鍗曞搷搴斾簨浠�
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("Open")) {
				textArea.setText("Open");
			}
			else if (cmd.equals("Save")) {
				textArea.setText("Save");
			}
			else if (cmd.equals("Run")) {
				resultLabel.setText("Hello, result");
			}
			else if (cmd.equals("Log in")){
				toLogin = true;
				setToLogin(toLogin);
			}
			else if (cmd.equals("Log out")){
				toLogin = false;
				setToLogin(toLogin);
			}
		}
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String code = textArea.getText();
			try {
				RemoteHelper.getInstance().getIOService().writeFile(code, "admin", "code");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

	}
}
