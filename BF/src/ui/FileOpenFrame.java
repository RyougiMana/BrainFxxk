package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import service.IOService;

@SuppressWarnings("serial")
public class FileOpenFrame extends FileFrame {

	String fileContent = null;
	
	public FileOpenFrame(MainFrame mf, IOService io, String u) {
		
		super(mf, io, u);
		
		this.setTitle("Open");
		
		confirmButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				try {
					mframe.setFilename(selectedVal);
					fileContent = ioService.readFile(username, selectedVal);
					if(fileContent.equals("not exists!")){
						Object[] options = { "OK", "CANCEL" }; 
						JOptionPane.showOptionDialog(null, "This file does not exist!", "Warning", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, 
						null, options, options[0]);
					}
					mframe.readFile(fileContent);
					FileOpenFrame.this.setVisible(false);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
}
