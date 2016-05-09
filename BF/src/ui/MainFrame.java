package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import analyzer.Analyzer;
import analyzer.AnalyzerImpl;
import rmi.RemoteHelper;
import service.IOService;
import service.UserService;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private RemoteHelper remoteHelper;
	private IOService ioService;
	private UserService userService;
	
	private Analyzer analyzer;
	
	private boolean isLogin;
	private boolean toLogin;
	private String username;
	
	private JPanel areaPanel;
	private JScrollPane codeScrollPane;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;
	private JTextArea textArea;
	private JTextArea inputArea;
	private JTextArea outputArea;
	private JLabel statusLabel;
	
	private JMenu commandMenu;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;
	
	private JMenu userMenu;
	private JMenuItem logMenuItem;

	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton confirmButton;
	private JButton cancelButton;
	
	public MainFrame() {
		
		remoteHelper = RemoteHelper.getInstance();
		ioService = remoteHelper.getIOService();
		userService = remoteHelper.getUserService();
		
		analyzer = new AnalyzerImpl(this);
		
		// 鍒涘缓绐椾綋
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 400);
		frame.setLocation(400, 200);
		
		usernameLabel = new JLabel("Username");
		passwordLabel = new JLabel("Password");
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		confirmButton = new JButton("Confirm");
		cancelButton = new JButton("Cancel");
		
		Dimension labelSize = new Dimension(65, 30);
		Dimension fieldSize = new Dimension(100, 30);
		Dimension buttonSize = new Dimension(80, 30);
		usernameLabel.setSize(labelSize);
		passwordLabel.setSize(labelSize);
		usernameField.setSize(fieldSize);
		passwordField.setSize(fieldSize);
		confirmButton.setSize(buttonSize);
		cancelButton.setSize(buttonSize);
		
		int startX = (int) ((frame.getWidth() - fieldSize.getWidth() - labelSize.getWidth()) / 2);
		int startY = (int) frame.getHeight() / 4;
		usernameLabel.setLocation(startX, startY);
		passwordLabel.setLocation(startX, (int) (startY + fieldSize.getHeight() + 10));
		usernameField.setLocation((int) (startX + labelSize.getWidth()), startY);
		passwordField.setLocation((int) (startX + labelSize.getWidth()), (int) (startY + fieldSize.getHeight() + 10));
		confirmButton.setLocation(startX, (int) (startY + fieldSize.getHeight() * 2 + + 10 * 2));
		cancelButton.setLocation((int) (startX + buttonSize.getWidth()), (int) (startY + fieldSize.getHeight() * 2 + + 10 * 2));
		confirmButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				String name = usernameField.getText();
				String psw = new String(passwordField.getPassword());
				try {
					isLogin = userService.login(name, psw);
					if(isLogin){
						toLogin = false;
						setToLogin(toLogin);
						username = name;
						setUserModule();
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		cancelButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				toLogin = false;
				setToLogin(toLogin);
			}
		});
		
		frame.add(usernameLabel);
		frame.add(passwordLabel);
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
		
		JMenu commandMenu = new JMenu("Cmd");
		menuBar.add(commandMenu);
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		commandMenu.add(undoMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		commandMenu.add(redoMenuItem);
		
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
		undoMenuItem.addActionListener(new MenuItemActionListener());
		redoMenuItem.addActionListener(new MenuItemActionListener());
		
		areaPanel = new JPanel();
		areaPanel.setLayout(null);
		areaPanel.setSize(500, 300);
		areaPanel.setLocation(0, 100);
		frame.add(areaPanel, BorderLayout.CENTER);
		
		textArea = new JTextArea("Code Section. Your code goes here......");
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		codeScrollPane = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar codeScroll=new JScrollBar();
		codeScrollPane.setVerticalScrollBar(codeScroll);
		
		codeScrollPane.setSize(500, 200);
		codeScrollPane.setLocation(0, 0);
		codeScrollPane.setVisible(true);
		codeScrollPane.setOpaque(false);
		codeScrollPane.getViewport().setOpaque(false);
		areaPanel.add(codeScrollPane);
		
		inputArea = new JTextArea("Input Section");
		inputArea.setMargin(new Insets(10, 10, 10, 10));
		inputArea.setSize(250, 140);
		inputArea.setLocation(0, 200);
		inputArea.setLineWrap(true);
		inputArea.setWrapStyleWord(true);
		areaPanel.add(inputArea);
		
		inputScrollPane = new JScrollPane(inputArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar inputScroll=new JScrollBar();
		inputScrollPane.setVerticalScrollBar(inputScroll);
		
		inputScrollPane.setSize(250, 140);
		inputScrollPane.setLocation(0, 200);
		inputScrollPane.setVisible(true);
		inputScrollPane.setOpaque(false);
		inputScrollPane.getViewport().setOpaque(false);
		areaPanel.add(inputScrollPane);
		
		outputArea = new JTextArea("Output Section");
		outputArea.setMargin(new Insets(10, 10, 10, 10));
		outputArea.setSize(250, 140);
		outputArea.setLocation(250, 200);
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		areaPanel.add(outputArea);
		
		outputScrollPane = new JScrollPane(outputArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar outputScroll=new JScrollBar();
		outputScrollPane.setVerticalScrollBar(outputScroll);
		
		outputScrollPane.setSize(250, 140);
		outputScrollPane.setLocation(250, 200);
		outputScrollPane.setVisible(true);
		outputScrollPane.setOpaque(false);
		outputScrollPane.getViewport().setOpaque(false);
		areaPanel.add(outputScrollPane);
		
		// 鏄剧ず缁撴灉
		statusLabel = new JLabel();
		statusLabel.setText("result");
		frame.add(statusLabel, BorderLayout.SOUTH);
		
		isLogin = false;
		toLogin = true;
		username = "Welcome, guest!";
		setUserModule();
		setToLogin(toLogin);

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
		usernameLabel.setVisible(_t);
		passwordLabel.setVisible(_t);
		usernameField.setVisible(_t);
		passwordField.setVisible(_t);
		confirmButton.setVisible(_t);
		cancelButton.setVisible(_t);
		areaPanel.setVisible(!_t);
		statusLabel.setVisible(!_t);
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
			else if (cmd.equals("Execute")) {
				String input = textArea.getText();
				String output;
				try {
					output = analyzer.analysis(input);
					outputArea.setText(output);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if (cmd.equals("Log in")){
				toLogin = true;
				setToLogin(toLogin);
			}
			else if (cmd.equals("Log out")){
				toLogin = true;
				isLogin = false;
				username = "Welcome, guest!";
				setToLogin(toLogin);
				setUserModule();
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
	
	public byte getByte() throws IOException{
		BufferedReader br = new BufferedReader(new StringReader(inputArea.getText()));
		String input = br.readLine();
		return input.getBytes()[0];
	}
	
}
