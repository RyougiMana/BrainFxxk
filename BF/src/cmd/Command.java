package cmd;

public interface Command {  
    public void execute(); // 执行命令和重做  
    public void undo();  // 执行撤销操作  
} 