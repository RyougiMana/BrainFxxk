package serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import service.IOService;

public class IOServiceImpl implements IOService{

	FileInputStream fis;
	FileOutputStream fos;
	
	String path = "/Users/okamiji/git/BrainFxxk/BFServer/files/";
	String recordPath = "/Users/okamiji/git/BrainFxxk/BFServer/";
	
	String username;
	String filename;
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) throws RemoteException {
		username = userId;
		filename = fileName;
		
        File folder = new File(path + username + "/" + filename);
        if(!folder.exists() || !folder.isDirectory()){
        	folder.mkdirs();
        }
        
        String recordStr = readFile(recordPath + username);
        boolean hasHistory = false;
        int historyCount = -1;
		if(recordStr != null && recordStr.length() != 0){
			String[] recordInfo = recordStr.split("\n");
			for(String s : recordInfo){
				if(s.split(" ")[0].equals(filename)){
					historyCount = Integer.parseInt(s.split(" ")[1]);
					hasHistory = true;
					break;
				}
			}
		}
		
		historyCount ++;
		String filePath = path + username + "/" + filename + "/" + historyCount;
		
		String recordContent = readFile(recordPath + username);
		String writeContent = new String();
		
		if(hasHistory){
			/* If the current version has the same content with the previous one. */
			historyCount --;
			if(readFile(path + username + "/" + filename + "/" + historyCount).equals(file)){
				return true;
			}
			historyCount ++;
			
			String[] records = recordContent.split("\n");
			
			for(int i=0; i<records.length; i++){
				if(records[i].split(" ")[0].equals(filename)){
					if(i == 0){
						writeContent = filename + " " + historyCount;
					}
					else{
						writeContent = writeContent + "\n" + filename + " " + historyCount;
					}
				}
				else{
					if(i == 0){
						writeContent = records[i];
					}
					else{
						writeContent = writeContent + "\n" + records[i];
					}
				}
			}
		}
		else{
			writeContent = recordContent;
			if(writeContent == null || writeContent.length() == 0){
				writeContent = filename + " " + historyCount;
			}
			else{
				writeContent = writeContent + "\n" + filename + " " + historyCount;
			}
		}

		writeFile(writeContent, recordPath + username, false);
		
		return writeFile(file, filePath, false);
	}

	@Override
	public String readFile(String userId, String fileName) throws RemoteException {
		/* Include file content and the latest version number */
		
		username = userId;
		filename = fileName;
		
		String filePath = path + username + "/" + filename + "/";
		
		String recordStr = readFile(recordPath + username);
		if(recordStr != null && recordStr.length() != 0){
			String[] recordInfo = recordStr.split("\n");
			for(int i=0; i<recordInfo.length; i++){
				String[] fileInfo = recordInfo[i].split(" ");
//TODO				System.out.println(fileInfo[0] + "  " + filename);
				
				if(filename.length() >= fileInfo[0].length() && fileInfo[0].equals(filename.substring(0, fileInfo[0].length()))){
					if(fileInfo[0].length() == filename.length()){
//TODO						System.out.println(filename);
						return readFile(filePath + fileInfo[1]) + " " + fileInfo[1];
					}
					else{
						int version = Integer.parseInt(filename.substring(fileInfo[0].length()));/* current version */
						filePath = path + username + "/" + filename.substring(0, fileInfo[0].length()) + "/";
						return readFile(filePath + version) + " " + fileInfo[1];/* The later version represent all versions. */
					}
				}
			}	
		}
		
		return null;
	}

	@Override
	public String readFileList(String userId) throws RemoteException {
		username = userId;
		String recordStr = readFile(recordPath + username);
		String returnStr = null;
		if(recordStr != null && recordStr.length() != 0){
			String[] recordInfo = recordStr.split("\n");
			returnStr = recordInfo[0].split(" ")[0];
			if(recordInfo.length >= 1){
				for(int i=1; i<recordInfo.length; i++){
					String[] fileInfo = recordInfo[i].split(" ");/* file name ; version number */
					returnStr = returnStr + "\n" + fileInfo[0];
				}	
			}
		}
		else{
			return null;
		}
		return returnStr;
	}

	
	private String readFile(String file){
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		byte[] inputByte = null;
		try {
			inputByte = new byte[fis.available()];
			fis.read(inputByte);
			fis.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(inputByte == null || inputByte.length == 0){
			return null;
		}
		return new String(inputByte);
	}
	
	private boolean writeFile(String content, String filePath, boolean pending){
		try {
			FileWriter fw = new FileWriter(filePath, pending);
			fw.write(content);
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
}