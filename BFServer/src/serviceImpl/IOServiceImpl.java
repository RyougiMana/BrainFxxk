package serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import service.IOService;

public class IOServiceImpl implements IOService{

	FileInputStream fis;
	FileOutputStream fos;
	
	String path = "/Users/okamiji/git/BrainFxxk/BFServer/files";
	String fileRecord = "/Users/okamiji/git/BrainFxxk/BFServer/filelist.txt";
	ArrayList<String> recordList = null;
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		if(recordList == null){
			String[] record = readFile(fileRecord).split("\n");
			recordList = (ArrayList<String>) Arrays.asList(record);
		}

		String currentFile = path + userId + "_" + fileName;
		File f = new File(currentFile);
		
		if(!recordList.contains(currentFile)){
			recordList.add(currentFile);
			try {
				FileWriter fwRecord = new FileWriter(fileRecord, true);
				fwRecord.write(currentFile);
				fwRecord.flush();
				fwRecord.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	@Override
	public String readFile(String userId, String fileName) {
		String currentFile = path + userId + "_" + fileName;
		return readFile(currentFile);
	}

	@Override
	public String readFileList(String userId) {
		String content = readFile(fileRecord);
		String list[] = content.split("\n");
		String result = new String();
		for(String s : list){
			if(s.contains(userId)){
				result = result + s.substring(s.length() - userId.length(), s.length()) + "\n";
			}
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
	
}
