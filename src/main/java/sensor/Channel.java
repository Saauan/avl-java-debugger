package sensor;
import java.util.Random;

public class Channel {
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int read() {
		return (new Random()).nextInt(1023);
	}
	
	
}
