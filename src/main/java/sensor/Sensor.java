package sensor;
public class Sensor {
	private String name;
	private float value;
	private int rawValue;
	private Channel channel;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public int getRawValue() {
		return rawValue;
	}
	
	public void setRawValue(int rawValue) {
		this.rawValue = rawValue;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void addToChannel(int channelIndex) {
		Channel chan = new Channel();
		chan.setIndex(channelIndex);
		this.setChannel(chan);
	}
	
	public void read() {
		setRawValue(getChannel().read());
		setValue(computeValue());
	}
	
	public float computeValue() {
		return getRawValue()/100;
	}
	
	

}
