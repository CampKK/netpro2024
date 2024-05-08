public class Calc3MethodTest {

	public static void main(String[] args){
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		printResult(func(x,y));
		// 上の1行は、 int z = func(x,y);
		//             printResult(z);
		//  の2行を縮めたもの
	}

	//返り値は整数型
	public static int func(int a, int b) {
		return 2 * a + b;
	}

	//返り値は無い
	public static void printResult(int z){
		System.out.println("計算結果は" + z + "です。");
	}

}