public class Calc1 {

	public static void main(String[] args) { 
	// C言語では、 main(int argc, char *argv) と書いていたはず
		System.out.println(args[0] + args[1]);
		System.out.println("これでは、文字列がつながるはず。");
		System.out.println("整数に直してみる");

		System.out.println(Integer.parseInt(args[0]) +
								Integer.parseInt(args[1]));



	}
}
