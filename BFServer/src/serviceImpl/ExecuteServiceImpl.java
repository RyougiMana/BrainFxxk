//璇蜂笉瑕佷慨鏀规湰鏂囦欢鍚�
package serviceImpl;

import java.rmi.RemoteException;
import java.util.Arrays;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 璇峰疄鐜拌鏂规硶
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		byte[] input = code.getBytes();
		byte[] para = param.getBytes();
		
		//Initialization
		int size = 2000;
		byte[] output = new byte[size];
		for(int i=0; i<size; i++){
			output[i] = 0;
		}
		
		//Output stream initialization
		StringBuffer sb = new StringBuffer();
		
		//Code analysis
		int ptr = 0;/*data pointer*/
		int inputSize = input.length;
		int codePtr = 0;/*code pointer*/
		int paramPtr = 0;/*parameter pointer*/
		while(codePtr < input.length){
			switch(input[codePtr]){
			case '>':
				ptr ++;
				break;
			case '<':
				ptr --;
				break;
			case '+':
				output[ptr] ++;
				break;
			case '-':
				output[ptr] --;
				break;
			case ',':
				output[ptr] = paramPtr < para.length ? 
						para[paramPtr] : 0;
				paramPtr ++;
				break;
//				String tmpStr = br.readLine();
//				output[ptr] = tmpStr.getBytes()[0];
//				break;
			case '.':
				sb.append((char) output[ptr]);
				//TODO
//				System.out.println("test");
//				bos.write(output[ptr]);
				break;
			case '[':
				if(output[ptr] == 0){
					int tmpPtr = codePtr;
					int level = 0;
					for(; tmpPtr < inputSize; tmpPtr ++){
						if(input[tmpPtr] == '[') {
							level ++;	
						}
						else if(input[tmpPtr] == ']'){
							level --;
							if(level == 0)
								break;
						}
					}
					if(level != 0 || input[tmpPtr] != ']'){
						System.out.println("No matching right parenthesis, execution terminated!");
						codePtr = input.length;
					}
					codePtr = tmpPtr + 1;
				}
				break;
			case ']':
				if(output[ptr] != 0){
					int tmpPtr = codePtr;
					int level = 0;
					for(; tmpPtr >= 0; tmpPtr --){
						if(input[tmpPtr] == ']') {
							level ++;
						}
						else if(input[tmpPtr] == '['){
							level --;
							if(level == 0) {
								codePtr = tmpPtr --;
								break;
							}
						}
					}
					if(tmpPtr < 0){
						System.out.println("No matching left parenthesis, execution terminated!");
						codePtr = input.length;
					}
				}
				break;
			default:
				break;
			}
			codePtr ++;
		}
		return sb.toString();
	}

}
