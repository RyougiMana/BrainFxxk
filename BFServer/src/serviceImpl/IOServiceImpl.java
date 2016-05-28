package serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import service.IOService;

public class IOServiceImpl implements IOService{

	FileInputStream fis;
	FileOutputStream fos;
	
	String path = "/Users/okamiji/git/BrainFxxk/BFServer/files/";
	String fileRecord = "/Users/okamiji/git/BrainFxxk/BFServer/filelist.txt";
	
	ArrayList<String> recordList = null;
	ArrayList<String> filterList = null;
	String filterId = null;
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		initRecordList();
		initFilterList(userId);
		
		String currentFile = path + userId + "_" + fileName;
		File f = new File(currentFile);
		
		if(!filterList.contains(fileName)){
			if(!f.exists()){
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				FileWriter fwRecord = new FileWriter(fileRecord, true);
				if(recordList.size() != 0){
					fwRecord.write("\n");
				}
				fwRecord.write(path + userId + "_" + fileName);
				fwRecord.flush();
				fwRecord.close();	
			} catch (IOException e) {
				e.printStackTrace();
			}
			recordList.add(currentFile);
			if(filterId == userId){
				filterList.add(fileName);
			}
			else{
				filterId = userId;
				filterList = new ArrayList<String>();
				filterList.add(fileName);
			}
			
			try {
				FileWriter fw = new FileWriter(f, false);
				fw.write(file);
				fw.flush();
				fw.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		else{
			try {
				FileWriter fw = new FileWriter(f, false);
				fw.write(file);
				fw.flush();
				fw.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	@Override
	public String readFile(String userId, String fileName) {
		String currentFile = path + userId + "_" + fileName;
		if(filterList.contains(fileName)){
			return readFile(currentFile);
		}
		else{
			return "not exists!";
		}
	}

	@Override
	public String readFileList(String userId) {
		initRecordList();
		initFilterList(userId);
		
		String result = new String();
		if(filterList.size() == 0){
			return "";
		}
		else{
			for(int i=0; i<filterList.size()-1; i++){
				result = result + filterList.get(i) + "\n";
			}
			result = result + filterList.get(filterList.size() - 1);
		}
		
		return result;
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
	
	private void initRecordList(){
		if(null == recordList){
			String content = readFile(fileRecord);
			if(content != null && content.trim().length() != 0){
				String[] record = content.split("\n");
				recordList = new ArrayList<String>();
				for(String s : record){
					recordList.add(s);
				}
			}
			else{
				recordList = new ArrayList<String>();
			}
		}
	}
	
	private void initFilterList(String userId){
		if(recordList.size() == 0){
			filterList = new ArrayList<String>();
		}
		if(filterId != userId || null == filterList){
			filterId = userId;
			filterList = new ArrayList<String>();
			int idLength = userId.length();
			for(String s : recordList){
				if(s.length() >= userId.length() && s.substring(path.length()).substring(0, idLength).equals(userId)){
					filterList.add(s.substring(path.length() + idLength + 1));
				}
			}
		}
	}
	
}
