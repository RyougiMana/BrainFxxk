package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Insets;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import cmd.Command;
import cmd.CommandImpl;
import cmd.CommandManager;
import rmi.RemoteHelper;
import service.ExecuteService;
import service.IOService;
import service.UserService;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private RemoteHelper remoteHelper;
	private IOService ioService;
	private UserService userService;
	private ExecuteService executeService;
	
	private boolean isLogin;
	private boolean toLogin;
	private String username;
	private String filename = null;
	
	private JPanel areaPanel;
	private JScrollPane codeScrollPane;
	private JScrollPane inputScrollPane;
	private JScrollPane outputScrollPane;
	public JTextArea textArea;
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
	
	private FileOpenFrame openFrame = null;
	int versionNum = 0;
	
	JMenu versionMenu;
	
	fThread thread;
	boolean exceed = true;
	CommandManager cm = new CommandManager();
	
	public MainFrame() {
		
		remoteHelper = RemoteHelper.getInstance();
		ioService = remoteHelper.getIOService();
		userService = remoteHelper.getUserService();
		executeService = remoteHelper.getExecuteService();
		
		// 鍒涘缓绐椾綋
		JFrame frame = new JFrame("BF Client");
		frame.setLayout(new BorderLayout());
		frame.setSize(500, 384);
		frame.setLocation(400, 200);
		
		usernameLabel = new JLabel("Username");
		passwordLabel = new JLabel("Password");
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		confirmButton = new JButton("Confirm");
		
		Dimension labelSize = new Dimension(65, 30);
		Dimension fieldSize = new Dimension(100, 30);
		Dimension buttonSize = new Dimension(120, 30);
		usernameLabel.setSize(labelSize);
		passwordLabel.setSize(labelSize);
		usernameField.setSize(fieldSize);
		passwordField.setSize(fieldSize);
		confirmButton.setSize(buttonSize);
		
		int startX = (int) ((frame.getWidth() - fieldSize.getWidth() - labelSize.getWidth()) / 2);
		int startY = (int) frame.getHeight() / 4;
		usernameLabel.setLocation(startX, startY);
		passwordLabel.setLocation(startX, (int) (startY + fieldSize.getHeight() + 10));
		usernameField.setLocation((int) (startX + labelSize.getWidth()), startY);
		passwordField.setLocation((int) (startX + labelSize.getWidth()), (int) (startY + fieldSize.getHeight() + 10));
		confirmButton.setLocation((int) (startX + labelSize.getWidth() / 2), (int) (startY + fieldSize.getHeight() * 2 + + 10 * 2));
		
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
					else{
						JOptionPane.showMessageDialog(null, "Wrong user name or password！");
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		frame.add(usernameLabel);
		frame.add(passwordLabel);
		frame.add(usernameField);
		frame.add(passwordField);
		frame.add(confirmButton);
		
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
		
		versionMenu = new JMenu("Version");
		menuBar.add(versionMenu);
		
		commandMenu = new JMenu("Cmd");
		menuBar.add(commandMenu);
		undoMenuItem = new JMenuItem("Undo");
		commandMenu.add(undoMenuItem);
		redoMenuItem = new JMenuItem("Redo");
		commandMenu.add(redoMenuItem);
		
		userMenu = new JMenu();
		menuBar.add(userMenu);
		logMenuItem = new JMenuItem();
		userMenu.add(logMenuItem);
		
		frame.setJMenuBar(menuBar);

		newMenuItem.addActionListener(new MenuItemActionListener());
		openMenuItem.addActionListener(new MenuItemActionListener());
		saveMenuItem.addActionListener(new MenuItemActionListener());
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
		statusLabel.setText("");
		frame.add(statusLabel, BorderLayout.SOUTH);
		
		isLogin = false;
		toLogin = true;
		username = "Welcome, guest!";
		setUserModule();
		setToLogin(toLogin);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		thread = new fThread();
		thread.start();
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
			if(cmd.equals("New")){
				filename = null;
				textArea.setText("Code Section. Your code goes here......");
				
				versionMenu.removeAll();
			}
			else if (cmd.equals("Open")) {
				try {
					if((filename != null && 
							(!textArea.getText().equals(ioService.readFile(username, filename))))
							||
						(filename == null) && 
							(!textArea.getText().equals("Code Section. Your code goes here......"))
							){
						int n = JOptionPane.showConfirmDialog(null, "The file has not been saved, save it?", "Save before open", JOptionPane.YES_NO_OPTION);   
						if (n == JOptionPane.YES_OPTION) {   
							if(null == filename){
								String inputValue = JOptionPane.showInputDialog("Please input the file name: ");
								try {
									ioService.writeFile(textArea.getText(), username, inputValue);
								} catch (RemoteException e1) {
									e1.printStackTrace();
								}
							}
							else{
								ioService.writeFile(textArea.getText(), username, filename);
							}
						}
					}
				} catch (HeadlessException | RemoteException e1) {
					e1.printStackTrace();
				}
				openFrame = new FileOpenFrame(MainFrame.this, ioService, username);
				openFrame.setVisible(true);
			}
			else if (cmd.equals("Save")) {
				String inputValue = null;
				boolean hasSpace = false;
				if(null == filename){
					inputValue = JOptionPane.showInputDialog("Please input the file name: ");
					for(int i=0; i<inputValue.length(); i++){
						if(inputValue.substring(i, i+1).equals(" ")){
							hasSpace = true;
							break;
						}
					}
					
				}
				else{
					inputValue = filename;
				}
				if(inputValue != null && !hasSpace){
					filename = inputValue;
					try {
						ioService.writeFile(textArea.getText(), username, filename);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "File name format is not correct！");
				}
			}
			else if (cmd.equals("Exit")) {
				System.exit(0);
			}
			else if (cmd.equals("Execute")) {
				String code = textArea.getText();
				String param = inputArea.getText();
				String output = new String();
				try {
					output = executeService.execute(code, param);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				outputArea.setText(output);
			}
			else if (cmd.equals("Log in")) {
				toLogin = true;
				setToLogin(toLogin);
			}
			else if (cmd.equals("Log out")){
				boolean logoutSuccess = false;
				try {
					logoutSuccess = userService.logout(username);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				if(logoutSuccess){
					toLogin = true;
					isLogin = false;
					username = "Welcome, guest!";
					setToLogin(toLogin);
					setUserModule();
					
					versionMenu.removeAll();
				}
			}
			else if (cmd.equals("Undo")){
				cm.undo();
			}
			else if (cmd.equals("Redo")){
				cm.redo();
			}
		}
	}

	public boolean writeFile(String fileName){
		String file = textArea.getText();
		try {
			return ioService.writeFile(file, username, fileName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void readFile(String content){
		String version;
		if(content.length() <= 4){
			version = content;
		}
		else{
			version = content.substring(content.length() - 5, content.length());
		}
		
		int index = 0;
		for(int i=0; i<version.length(); i++){
			if(version.substring(i, i+1).equals(" ")){
				index = i + 1;
			}
		}
		versionNum = Integer.parseInt(version.substring(index));
		int minusLength = version.substring(index).length();
		textArea.setText(content.substring(0, content.length() - minusLength - 1));

		versionMenu.removeAll();
		
		if(versionNum > 9){
			for(int i=versionNum - 9; i<versionNum + 1; i++){
				JMenuItem mItem = new JMenuItem(i+"");
				mItem.addActionListener(new VersionActionListener());
				versionMenu.add(mItem);
			}
		}
		else{
			for(int i=0; i<versionNum + 1; i++){
				JMenuItem mItem = new JMenuItem(i+"");
				mItem.addActionListener(new VersionActionListener());
				versionMenu.add(mItem);
			}
		}
		
		thread.setIndex(textArea.getText().length());
	}

	class VersionActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			try {
				String fileContent = ioService.readFile(username, filename + cmd);
				readFile(fileContent);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	class fThread extends Thread{
		String content = textArea.getText();
		String minusContent = new String();
		int index = textArea.getText().length();
		int length = 0;
		int minusLength = 0;
		
		public void setIndex(int i){
			index = i;
		}
		
		public void run(){
			while(true){
				try {
					fThread.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		//		System.out.println("running");
				if((length != Math.abs(textArea.getText().length() - index) &&
						(index != textArea.getText().length())
						)){
					if(exceed){
						/* increase */
						if(textArea.getText().length() > index + length){
							length = textArea.getText().length() - index;
							content = textArea.getText().substring(index, index + length);
				//			System.out.println(content);
							Command cmd = new CommandImpl(MainFrame.this, content, exceed);
							cm.executeCommand(cmd);
							content = textArea.getText();
						}
						else{
							index = textArea.getText().length() + 1;
							System.out.println("change direction");
							exceed = false;
							length = 0;
							minusLength = 1;
							minusContent = content.substring(content.length() - minusLength, content.length());
							Command cmd = new CommandImpl(MainFrame.this, minusContent, exceed);
							cm.executeCommand(cmd);
						}
					}
				}
				if((index != textArea.getText().length()) && 
						(index != (textArea.getText().length() + minusLength))){
					if(!exceed){
						/* decrease */
						if(textArea.getText().length() <= index - minusLength){
							/*
							 * index : length of text area before any operation
							 * length : length = 0
							 * minus length : length of eliminate words
							 * content : content of text area before any operation
							 * minus content : content of eliminate words
							 * */
							minusLength = index - textArea.getText().length();
					//		System.out.println("length " + minusLength);
							minusContent = content.substring(content.length() - minusLength, content.length());
					//		System.out.println("new cmd " + minusContent);
							Command cmd = new CommandImpl(MainFrame.this, minusContent, exceed);
							cm.executeCommand(cmd);
						}
						else{
							index = textArea.getText().length() - 1;
							System.out.println("change direction");
							exceed = true;
							minusLength = 0;
							length = 1;
							content = textArea.getText().substring(index, index + length);
							minusContent = new String();
							Command cmd = new CommandImpl(MainFrame.this, content, exceed);
							cm.executeCommand(cmd);
						}
						
					}
				}
			}
		}
	}
	
	public void setFilename(String fileName){
		filename = fileName;
	}
	
}
