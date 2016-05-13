package serviceImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import service.IOService;

public class IOServiceImpl implements IOService{

	String path = "/Users/okamiji/Desktop/BF/files";
	
	@Override
	public boolean writeFile(String file, String userId, String fileName) {
		File f = new File(userId + "_" + fileName);
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
	//	if()
		return "OK";
	}

	@Override
	public String readFileList(String userId) {
		return "OK";
	}
	
}
