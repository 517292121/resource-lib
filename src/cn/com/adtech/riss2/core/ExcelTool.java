package cn.com.adtech.riss2.core;

public class ExcelTool {

	final public static int getColumnIndex(String columnTitle){
		if(columnTitle == null || columnTitle.length() < 1){
			return 0;
		}
		columnTitle = columnTitle.toUpperCase();
		char[] chars = null;
		try {
			chars = columnTitle.toCharArray();
			if(chars == null || chars.length < 1){
				return 0;
			}
			if(chars.length == 1){
				return convertCharToIndex(chars[0]);
			}if(chars.length == 2){
				int one = convertCharToIndex(chars[0]);
				one = one * 26;
				int two = convertCharToIndex(chars[1]);
				return one + two;
			}else{
				System.out.println(columnTitle + " 过长，转换列下标失败！");
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	final public static int convertCharToIndex(char c){
		int index = 0;
		switch (c) {
		case 'A':
			index = 1;
			break;
		case 'B':
			index = 2;
			break;
		case 'C':
			index = 3;
			break;
		case 'D':
			index = 4;
			break;
		case 'E':
			index = 5;
			break;
		case 'F':
			index = 6;
			break;
		case 'G':
			index = 7;
			break;
		case 'H':
			index = 8;
			break;
		case 'I':
			index = 9;
			break;
		case 'J':
			index = 10;
			break;
		case 'K':
			index = 11;
			break;
		case 'L':
			index = 12;
			break;
		case 'M':
			index = 13;
			break;
		case 'N':
			index = 14;
			break;
		case 'O':
			index = 15;
			break;
		case 'P':
			index = 16;
			break;
		case 'Q':
			index = 17;
			break;
		case 'R':
			index = 18;
			break;
		case 'S':
			index = 19;
			break;
		case 'T':
			index = 20;
			break;
		case 'U':
			index = 21;
			break;
		case 'V':
			index = 22;
			break;
		case 'W':
			index = 23;
			break;
		case 'X':
			index = 24;
			break;
		case 'Y':
			index = 25;
			break;
		case 'Z':
			index = 26;
			break;
		default:
			index = 0;
			break;
		}
		return index;
	}
}
