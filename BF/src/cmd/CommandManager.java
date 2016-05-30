package cmd;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

	private List<Command> undoList = new ArrayList<Command>();
	private List<Command> redoList = new ArrayList<Command>();
	
	// 可撤销的步数，-1时无限步
	private int undoCount = -1;
	
	public CommandManager() {
		
		// 可通过配置文件配置撤销步数
		undoCount = 5;
	}

	/**
	 * 执行新操作
	 */
	public void executeCommand(Command cmd) {
		
		// 执行操作
//		cmd.execute();
		
		undoList.add(cmd);
		System.out.println("undolist" + undoList.size());
		// 保留最近undoCount次操作，删除最早操作
		if (undoCount != -1 && undoList.size() > undoCount) {
			undoList.remove(0);
		}
		
		// 执行新操作后清空redoList，因为这些操作不能恢复了
		redoList.clear();
	}
	
	/**
	 * 执行撤销操作
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
	 * 执行重做
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