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
		System.out.println("����:\t     "+ classNames.get(0) + " |");
		showLine();
		for(int i = 1;i<nextq;i++){
			System.out.println("��Ա����:\t\t" + "(" +
//			al.get(i-1).arg1 +
//			al.get(i-1).opera +
//			al.get(i-1).arg2 +
			al.get(i-1).result+") |");
			showLine();
		}
		System.out.println("��Ա����:\t\t("+functionNames.get(0)+") |");
		showLine();
	}
	private static void showLine() {
		System.out.println("---------------------");
	}
	private static void lrparser() {//�����Ĵ���
		nextq = 1;//��ʾ�������ĸ���->ϲ���±�ı�ʾ����
		int[] nChain = {0};
		if(syn == 1) {//�ҵ�class
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
		if(syn == 28) {//�����'{'
			scanner();
			staString(nChain);//���������䴮��������
			if(syn == 0) {
			} else {
				System.out.println("expeted } 67 here");
			}
		} else {
			System.out.println("expected { 70 here");
		}
	}
	private static void staString(int[] nChain) {
		sta(nChain);//����������
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
				scanner();//ɨ���ڶ���'}'
				if(syn==10) {
					sta(nChain);
				} else {
					break;
				}
			}
			
		}
		
	}
	private static void sta(int[] nChain) {
		if(syn==10){//����Ǳ������ߺ�����
			funorvar();
		}
	}
	private static void funorvar() {//������ȿ����б���,Ҳ�����к���
		StringBuffer res = new StringBuffer();
		StringBuffer num = new StringBuffer();
		if(syn==10){
			res.append( token.toString());//���������ߺ�����������res��
			scanner();
			if(syn == 21) {//�����'='
				scanner();
				num = E();
				emit(res.toString(), num.toString(), "=", "");
				if(syn == 31) {
					//������һ�����ķ���
				} else {
					System.out.println("expected ; line 106");
				}
			} else {
				if(syn == 26){//�����'(',��ȷ���Ǻ���
					//������������
					functionNames.add(res.toString());
					scanner();
					if(syn == 27) {//�����')'
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
		if(syn == 28) {//�����'{'
			scanner();if(syn == 10)
			funStaString();//�����������䴮��������
			if(syn == 29) {
				scanner();
			} else {
				System.out.println("expeted } 127 here");
			}
		} else {
			System.out.println("expected { 130 here");
		}
	}
	private static void funStaString() {//�������治����Ƕ�׺���
		StringBuffer res = new StringBuffer();
		StringBuffer num = new StringBuffer();
		if(syn==10){//����Ǳ���
			res.append(token.toString());
			scanner();
			if(syn == 21) {//�����'='
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
		fc.result = res;//���������
		fc.arg1 = num1;//����ֵ����
		fc.arg2 = num2;//��
		fc.opera = op;//��ֵ��
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
		token.delete(0, token.length());//��֮ǰ�Ĵ����
		ch = prog.charAt(p);
		p++;
		while(ch==' '||ch=='\n'){
			ch = prog.charAt(p);
			p++;
		}
		if (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z'))) {//��������ĸ��ͷ���ַ��� 
			while (((ch >= 'a') && (ch <= 'z')) || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= '0') && (ch <= '9')))
			{
				//token[m++] = ch;
				token.append(ch);
				ch = prog.charAt(p);
				p++;
			}
			p--;//ָ�򼴽�Ҫɨ�����ĸ
			syn = 10;//���������
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
			syn = 20;//��ʾ��������
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
			token.delete(0, token.length());//��֮ǰ�ĺ��������߱������
			token.append(ch);//'='���ű����浽token��
			ch = prog.charAt(p++);//ch�����µõ��¸��ַ���ֵ,����pָ���¸�λ��
			if(ch== '=') {//�����==
				syn = 36;
				token.append(ch);
				
			} else {//����Ǹ�ֵ����
				syn = 21;
				p--;//����Ǹ�ֵ��,����Ҫָ��ֵ�ŵ��¸�λ��
			}
			break;
		default:
			syn = -1;
		}
	}
}