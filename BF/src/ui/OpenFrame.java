package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import service.IOService;

@SuppressWarnings("serial")
public class OpenFrame extends JFrame{
	
	private final int WIDTH = 200;
	private final int HEIGHT = 290;
	
	private IOService ioService;
	private String username;
	private String[] fileList;
	
	private JScrollPane fileListPane;
	private JPanel fileListPanel;
	private JTextArea listArea;
	private JTextArea titleArea;
	private JButton confirmButton;
	private JButton cancelButton;
	
	public OpenFrame(IOService io, String u){
		
		ioService = io;
		username = u;
		
		this.setLayout(null);
		
		fileListPanel = new JPanel();
		fileListPanel.setLayout(null);
		fileListPanel.setLocation(0, 0);
		fileListPanel.setSize(200, 200);
		this.add(fileListPanel);
		
		listArea = new JTextArea();
		listArea.setLocation(0, 0);
		listArea.setSize(0, 200);
		listArea.setLineWrap(true);
		listArea.setWrapStyleWord(true);
		fileListPanel.add(listArea);
		
		initList();
		
		fileListPane = new JScrollPane(listArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JScrollBar inputScroll=new JScrollBar();
		fileListPane.setVerticalScrollBar(inputScroll);

		fileListPane.setLocation(0, 0);
		fileListPane.setSize(200, 200);
		fileListPane.setVisible(true);
		fileListPane.setOpaque(false);
		fileListPane.getViewport().setOpaque(false);
		fileListPanel.add(fileListPane);
		
		titleArea = new JTextArea();
		titleArea.setLocation(20, 210);
		titleArea.setSize(160, 20);
		this.add(titleArea);

		confirmButton = new JButton("Confirm");
		confirmButton.setLocation(15, 235);
		confirmButton.setSize(85, 30);
		this.add(confirmButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setLocation(100, 235);
		cancelButton.setSize(85, 30);
		this.add(cancelButton);
		
		confirmButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				
			}
		});
		
		cancelButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				OpenFrame.this.dispose();
			}
		});
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(550, 250);
		this.setVisible(true);
	}
	
	private void initList(){
		try {
			String list = ioService.readFileList(username);
			fileList = list.split(",");
			if(fileList != null && fileList.length != 0){
				String listToShow = "";
				for(int i=0; i<fileList.length; i++){
					listToShow = listToShow + fileList[i]; 
				}
				listArea.setText(listToShow);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}
	
}
