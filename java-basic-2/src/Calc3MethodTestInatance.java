public class Calc3MethodTestInatance {

	double PAI=3.14;

	public static void main(String[] args){
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);

		Calc3MethodTestInatance calc3=new Calc3MethodTestInatance();
		calc3.printResult(calc3.func(x,y));
		// 上の1行は、 int z = func(x,y);
		//             printResult(z);
		//  の2行を縮めたもの
	}

	//static
	Calc3MethodTestInatance(){

	}


	//返り値は整数型
	private int func(int a, int b) {
		return (int) (2 * a + b*PAI);
	}

	//返り値は無い
	private void printResult(int z){
		System.out.println("計算結果は" + z + "です。");
	}

}