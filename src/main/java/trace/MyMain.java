package trace;

public class MyMain {
	public static void main(String[] args) {
		myMainMethod();
	}

	private static void myMainMethod() {
		int x = 2;
		int y = 6;
		int z = addition(x, y);

		float a = percent(x, z);

		System.out.println(z);
	}

	private static int percent(int x, int z) {
		return division(x, addition(x, z));
	}

	private static int division(int x, int y) {
		return x / y;
	}

	private static int addition(int x, int y) {
		return x + y;
	}
}
