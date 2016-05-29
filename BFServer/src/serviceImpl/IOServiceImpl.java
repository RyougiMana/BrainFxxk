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
//TODO
				if(fileInfo[0].equals(filename.substring(0, fileInfo[0].length()))){
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
//
//	
//	ArrayList<String> recordList = null;
//	ArrayList<String> filterList = null;
//	String filterId = null;
//	
//	@Override
//	public boolean writeFile(String file, String userId, String fileName) {
//		initRecordList();
//		initFilterList(userId);
//		
//		String currentFile = path + userId + "_" + fileName;
//		File f = new File(currentFile);
//		
//		if(!filterList.contains(fileName)){
//			if(!f.exists()){
//				try {
//					f.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			
//			try {
//				FileWriter fwRecord = new FileWriter(fileRecord, true);
//				if(recordList.size() != 0){
//					fwRecord.write("\n");
//				}
//				fwRecord.write(path + userId + "_" + fileName);
//				fwRecord.flush();
//				fwRecord.close();	
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			recordList.add(currentFile);
//			if(filterId == userId){
//				filterList.add(fileName);
//			}
//			else{
//				filterId = userId;
//				filterList = new ArrayList<String>();
//				filterList.add(fileName);
//			}
//			
//			try {
//				FileWriter fw = new FileWriter(f, false);
//				fw.write(file);
//				fw.flush();
//				fw.close();
//				return true;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//		else{
//			try {
//				FileWriter fw = new FileWriter(f, false);
//				fw.write(file);
//				fw.flush();
//				fw.close();
//				return true;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
//
//	}
//
//	@Override
//	public String readFile(String userId, String fileName) {
//		String currentFile = path + userId + "_" + fileName;
//		
//		initRecordList();
//		initFilterList(userId);
//		
//		if(filterList.contains(fileName)){
//			return readFile(currentFile);
//		}
//		else{
//			return "not exists!";
//		}
//	}
//
//	@Override
//	public String readFileList(String userId) {
//		initRecordList();
//		initFilterList(userId);
//		
//		String result = new String();
//		if(filterList.size() == 0){
//			return "";
//		}
//		else{
//			for(int i=0; i<filterList.size()-1; i++){
//				result = result + filterList.get(i) + "\n";
//			}
//			result = result + filterList.get(filterList.size() - 1);
//		}
//		
//		return result;
//	}
//	

//	
//	private void initRecordList(){
//		if(null == recordList){
//			String content = readFile(fileRecord);
//			if(content != null && content.trim().length() != 0){
//				String[] record = content.split("\n");
//				recordList = new ArrayList<String>();
//				for(String s : record){
//					recordList.add(s);
//				}
//			}
//			else{
//				recordList = new ArrayList<String>();
//			}
//		}
//	}
//	
//	private void initFilterList(String userId){
//		if(recordList.size() == 0){
//			filterList = new ArrayList<String>();
//		}
//		if(filterId != userId || null == filterList){
//			filterId = userId;
//			filterList = new ArrayList<String>();
//			int idLength = userId.length();
//			for(String s : recordList){
//				if(s.length() >= userId.length() && s.substring(path.length()).substring(0, idLength).equals(userId)){
//					filterList.add(s.substring(path.length() + idLength + 1));
//				}
//			}
//		}
//	}
//	
//}
