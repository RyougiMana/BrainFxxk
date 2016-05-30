package cmd;

import ui.MainFrame;

public class CommandImpl implements Command {

	private MainFrame mf;
	private String content;
	private boolean exceed;
	
	public CommandImpl(MainFrame m, String str, boolean e){
		mf = m;
		content = str;
		exceed = e;
	}
	
	@Override
	public void execute() {
		if(exceed){
			String tmp = mf.textArea.getText();
			mf.textArea.setText(tmp + content);	
		}
		else{
			String tmp = mf.textArea.getText();
			mf.textArea.setText(tmp.substring(0, tmp.length() - content.length()));
		}
		
	}

	@Override
	public void undo() {
		if(exceed){
			String tmp = mf.textArea.getText();
			mf.textArea.setText(tmp.substring(0, tmp.length() - content.length()));
		}
		else{
			String tmp = mf.textArea.getText();
			System.out.println(content);
			mf.textArea.setText(tmp + content);
		}
	}
	
}
