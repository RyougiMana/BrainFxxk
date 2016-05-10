package analyzer;

import java.io.BufferedOutputStream;
import java.io.IOException;

public abstract class Analyzer {
	
	public abstract String analysis(String inputStr) throws IOException;

}
