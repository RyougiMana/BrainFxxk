//璇蜂笉瑕佷慨鏀规湰鏂囦欢鍚�
package serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import service.ExecuteService;
import service.UserService;

public class ExecuteServiceImpl implements ExecuteService {

	/**
	 * 璇峰疄鐜拌鏂规硶
	 */
	@Override
	public String execute(String code, String param) throws RemoteException {
		byte[] input = code.getBytes();
		byte[] para = param.getBytes();
		
		//Initialization
		int size = 100;
		byte[] output = new byte[size];
		for(int i=0; i<size; i++){
			output[i] = 0;
		}
		
		//Output stream initialization
		StringBuffer sb = new StringBuffer();
		//Input stream initialization
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Code analysis
		int ptr = 0;/*data pointer*/
		int inputSize = input.length;
		int codePtr = 0;/*code pointer*/
		int paramPtr = 0;/*parameter pointer*/
		while(codePtr != input.length){
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
				output[ptr] = para[paramPtr];
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
					boolean hasRightParenthesis = false;
					int tmpPtr = codePtr;
					for(; tmpPtr < inputSize; tmpPtr ++){
						if(input[tmpPtr] == ']'){
							hasRightParenthesis = true;
							break;
						}
					}
					if(!hasRightParenthesis){
						System.out.println("No matching right parenthesis, execution terminated!");
						codePtr = tmpPtr --;
					}
				}
				break;
			case ']':
				if(output[ptr] != 0){
					int tmpPtr = codePtr;
					for(; tmpPtr >= 0; tmpPtr --){
						if(input[tmpPtr] == '['){
							codePtr = tmpPtr --;
							break;
						}
					}
					if(tmpPtr < 0){
						System.out.println("No matching left parenthesis, execution terminated!");
					}
				}
				break;
			default:
				if(!((input[codePtr] == 10) || (input[codePtr] == 13))){
					System.out.println(codePtr + " " + Byte.toString(input[codePtr]));
					System.out.println("There exists wrong in code!");
				}
			}
			codePtr ++;
		}
		return sb.toString();
	}

}
