package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import service.IOService;

@SuppressWarnings("serial")
public class FileFrame extends JFrame{
	
	protected final int WIDTH = 200;
	protected final int HEIGHT = 290;
	
	protected MainFrame mframe;
	protected IOService ioService;
	protected String username;
	protected String[] fileList;
	
	protected JScrollPane fileListPane;
	protected JPanel fileListPanel;
	protected JList<String> list;
	protected JTextArea titleArea;
	protected JButton confirmButton;
	protected JButton cancelButton;
	
	protected String selectedVal = null;
	
	public FileFrame(MainFrame mf, IOService io, String u){
		
		mframe = mf;
		ioService = io;
		username = u;
		
		this.setLayout(null);
		
		fileListPanel = new JPanel();
		fileListPanel.setLayout(null);
		fileListPanel.setLocation(0, 0);
		fileListPanel.setSize(200, 200);
		this.add(fileListPanel);
		
		initList();
		
		fileListPane = new JScrollPane(list,
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
		
		cancelButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				FileFrame.this.dispose();
			}
		});
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(550, 250);
		this.setVisible(true);
	}
	
	private void initList(){
		String returnList = null;
		try {
			returnList = ioService.readFileList(username);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(returnList == null || returnList.length() == 0){
			list = new JList<String>(new String[]{});
		}
		else{
			fileList = returnList.split("\n");
			list = new JList<String>(fileList);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//			list.setVisibleRowCount(4);
			list.setLocation(20, 210);
			list.setSize(200, 200);
//			list.setFixedCellWidth(200);
			list.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					selectedVal = list.getSelectedValue();
					titleArea.setText(selectedVal);
				}
			});
			this.add(list);	
		}
	}
	
}
