public class ClassC {
	
	private String name="classC";
	private int value=100;
	
	public static void main(String arsg[]){
		
		ClassC clss_c=new ClassC();
		System.out.println(clss_c.name);
	}
	
	
	ClassC(){
		System.out.println("constractor ClassC");

	}
	
	public int getValue(){
		return value;
	}

	public String getName(){

		return name;
	}

}
