package tester;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BFtester {
	public static void main(String[] args) throws IOException{
		String inputStr = "++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.";
		byte[] input = inputStr.getBytes();
		
		//Initialization
		int size = 100;
		byte[] output = new byte[size];
		for(int i=0; i<size; i++){
			output[i] = 0;
		}
		
		//Output stream initialization
		BufferedOutputStream bos = new BufferedOutputStream(System.out);
		//Input stream initialization
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		//Code analysis
		int ptr = 0;/*data pointer*/
		int inputSize = input.length;
		int codePtr = 0;/*code pointer*/
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
				String tmpStr = br.readLine();
				output[ptr] = tmpStr.getBytes()[0];
				break;
			case '.':
				bos.write(output[ptr]);
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
				System.out.println(codePtr + " " + input[codePtr]);
				System.out.println("There exists wrong in code!");
			}
			codePtr ++;
		}
		
		bos.flush();
	}
}
