package cmd;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

	private List<Command> undoList = new ArrayList<Command>();
	private List<Command> redoList = new ArrayList<Command>();
	
	// �ɳ����Ĳ�����-1ʱ���޲�
	private int undoCount = -1;
	
	public CommandManager() {
		
		// ��ͨ�������ļ����ó�������
		undoCount = 5;
	}

	/**
	 * ִ���²���
	 */
	public void executeCommand(Command cmd) {
		
		// ִ�в���
//		cmd.execute();
		
		undoList.add(cmd);
		System.out.println("undolist" + undoList.size());
		// �������undoCount�β�����ɾ���������
		if (undoCount != -1 && undoList.size() > undoCount) {
			undoList.remove(0);
		}
		
		// ִ���²��������redoList����Ϊ��Щ�������ָܻ���
		redoList.clear();
	}
	
	/**
	 * ִ�г�������
	 */
	public void undo() {
		if (undoList.size() <= 0) {
			return;
		}
		
		Command cmd = ((Command)(undoList.get(undoList.size() - 1)));
		cmd.undo();
		
		undoList.remove(cmd);
		redoList.add(cmd);
	}

	/**
	 * ִ������
	 */
	public void redo() {
		if (redoList.size() <= 0) {
			return;
		}
		
		System.out.println("redo");
		
		Command cmd = ((Command)(redoList.get(redoList.size() - 1)));
		cmd.execute();
		
		redoList.remove(cmd);
		undoList.add(cmd);
	}
}