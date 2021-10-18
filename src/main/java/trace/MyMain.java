package trace;

public class MyMain {

	boolean state = false;
	static int myNumber = 40;


	public static void main(String[] args) {
		new MyMain().myMainMethod();
	}

	private void myMainMethod() {
		int x = 2;
		int y = 6;
		int z = addition(x, y);
		int z2 = addition(x, y);

		float a = percent(x, z2);

		System.out.println(z2);
	}

	private int percent(int x, int z) {
		return division(x, addition(x, z));
	}

	private int division(int x, int y) {
		return x / y;
	}

	private int addition(int x, int y) {
		return x + y;
	}
}
