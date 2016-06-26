package UML;
import java.util.ArrayList;

import javax.swing.JFrame;
@SuppressWarnings("serial")
public class UMLMain extends JFrame {
	private static int p = 0;
	private static StringBuffer token = new StringBuffer("");
	private static char ch;
	private static String prog = null;
	private static int syn;
	private static String[] rwtab = {
		"class"
	};
	private static int nextq;
	private static int sum;
	private static ArrayList<FourCom> al = new ArrayList<FourCom>();
	private static ArrayList<String>classNames=new ArrayList<String>();
	private static ArrayList<String>functionNames=new ArrayList<String>();
	public static void main(String[] args) {
		prog = "class class1 {"
				+ "x=45;"
				+ "y=56;"
				+ "z=34;"
				+ "b(){"
				+ "m=12;"
				+ "n=89;"
				+ "}"
			+ "}#";
		p = 0;
		scanner();
		lrparser();
		output();
	}
	private static void output() {
		showLine();
		System.out.println("类名:\t     "+ classNames.get(0) + " |");
		showLine();
		for(int i = 1;i<nextq;i++){
			System.out.println("成员变量:\t\t" + "(" +
//			al.get(i-1).arg1 +
//			al.get(i-1).opera +
//			al.get(i-1).arg2 +
			al.get(i-1).result+") |");
			showLine();
		}
		System.out.println("成员函数:\t\t("+functionNames.get(0)+") |");
		showLine();
	}
	private static void showLine() {
		System.out.println("---------------------");
	}
	private static void lrparser() {//类名的处理
		nextq = 1;//表示编译语句的个数->喜欢下标的表示方法
		int[] nChain = {0};
		if(syn == 1) {//找到class
			scanner();
			classNames.add(token.toString());
			if(syn == 10) {
				scanner();
				staBlock(nChain);
			} else {
				System.out.println("expected classname here");
			}
		} else {
			System.out.println("expected 'class' here");
		}
	}
	private static void staBlock(int[] nChain) {
		if(syn == 28) {//如果是'{'
			scanner();
			staString(nChain);//类里面的语句串分析函数
			if(syn == 0) {
			} else {
				System.out.println("expeted } 67 here");
			}
		} else {
			System.out.println("expected { 70 here");
		}
	}
	private static void staString(int[] nChain) {
		sta(nChain);//语句分析函数
		while(true) {
			if(syn==31) {
				scanner();
				if(syn==10)
				sta(nChain);
				else {
					break;
				}
			}
			if(syn==29) {
				scanner();//扫到第二个'}'
				if(syn==10) {
					sta(nChain);
				} else {
					break;
				}
			}
			
		}
		
	}
	private static void sta(int[] nChain) {
		if(syn==10){//如果是变量或者函数名
			funorvar();
		}
	}
	private static void funorvar() {//类里面既可以有变量,也可以有函数
		StringBuffer res = new StringBuffer();
		StringBuffer num = new StringBuffer();
		if(syn==10){
			res.append( token.toString());//将变量或者函数名保存在res中
			scanner();
			if(syn == 21) {//如果是'='
				scanner();
				num = E();
				emit(res.toString(), num.toString(), "=", "");
				if(syn == 31) {
					//继续下一条语句的分析
				} else {
					System.out.println("expected ; line 106");
				}
			} else {
				if(syn == 26){//如果是'(',即确定是函数
					//将函数画出来
					functionNames.add(res.toString());
					scanner();
					if(syn == 27) {//如果是')'
						scanner();
						funStaBlo();
					} else {
						System.out.println("expected ) here");
					}
				}
			}
			
		}
	}
	
	private static void funStaBlo() {
		if(syn == 28) {//如果是'{'
			scanner();if(syn == 10)
			funStaString();//函数里面的语句串分析函数
			if(syn == 29) {
				scanner();
			} else {
				System.out.println("expeted } 127 here");
			}
		} else {
			System.out.println("expected { 130 here");
		}
	}
	private static void funStaString() {//函数里面不可以嵌套函数
		StringBuffer res = new StringBuffer();
		StringBuffer num = new StringBuffer();
		if(syn==10){//如果是变量
			res.append(token.toString());
			scanner();
			if(syn == 21) {//如果是'='
				scanner();
				num = E();
				emit(res.toString(), num.toString(), "=", "");
				if(syn == 31) {
					scanner();
					if(syn == 10) {
						funStaString();
					} else {
						
					}
				} else {
					System.out.println("expected ; line 106"); 
				}
			} else {
				System.out.println("expected = line 154");
			}
		}
	}
	private static void emit(String res, String num1, String op,
			String num2) {
		FourCom fc = new FourCom();
		fc.result = res;//保存变量名
		fc.arg1 = num1;//待赋值的数
		fc.arg2 = num2;//空
		fc.opera = op;//赋值号
		al.add(fc);
	}
	private static StringBuffer E() {
		StringBuffer num1 = new StringBuffer();
		num1 = T();
		nextq++;
		return num1;
	}
	private static StringBuffer T() {
		StringBuffer num1 = new StringBuffer();
		num1 = F(); 
		return num1;
	}
	private static StringBuffer F() {
		StringBuffer res = new StringBuffer();
		if(syn==20) {
			Integer i = new Integer(sum);
			res.append(i.toString());
			scanner();
		}
		return res;
	}
	private static void scanner() {
		token.delete(0, token.length());//将之前的词清除
		ch = prog.charAt(p);
		p++;
		while(ch==' '||ch=='\n'){
			ch = prog.charAt(p);
			p++;
		}
		if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) {//处理以字母开头的字符串 
			while (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= '0') && (ch <= '9')))
			{
				//token[m++] = ch;
				token.append(ch);
				ch = prog.charAt(p);
				p++;
			}
			p--;//指向即将要扫描的字母
			syn = 10;//变量种类符
			for(int i=0; i<1; i++) {
				if(token.toString().equals(rwtab[0].toString())) {
					syn = i+1;
					break;
				}
			}
		} else if ((ch >= '0') && (ch <= '9')) {
			sum = 0;
			while(ch >= '0' && ch <= '9') {
				sum = sum*10+ch-'0';
				ch = prog.charAt(p++);
			}
			p--;
			syn = 20;//表示数字种类
		} else switch(ch) {
		case '(':
			syn = 26;
			break;
		case ')':
			syn = 27;
			break;
		case '{':
			syn = 28;
			token.append(ch);
			break;
		case '}':
			syn = 29;
			token.append(ch);
			break;
		case '#':
			syn = 0;
			token.append(ch);
			break;
		case ';':
			syn = 31;
			token.append(ch);
			break;
		case '=':
			token.delete(0, token.length());//将之前的函数名或者变量清空
			token.append(ch);//'='符号被保存到token中
			ch = prog.charAt(p++);//ch中重新得到下个字符的值,并且p指向下个位置
			if(ch== '=') {//如果是==
				syn = 36;
				token.append(ch);
				
			} else {//如果是赋值符号
				syn = 21;
				p--;//如果是赋值号,还是要指向赋值号的下个位置
			}
			break;
		default:
			syn = -1;
		}
	}
}