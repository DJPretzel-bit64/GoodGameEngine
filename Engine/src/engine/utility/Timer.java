package engine.utility;

public class Timer {

	private final long duration;
	private final boolean loop;
	private long start = 0;

	public Timer(long duration, boolean loop) {
		this.duration = duration;
		this.loop = loop;
	}

	public void start() {
		this.start = System.currentTimeMillis();
	}

	public boolean expired() {
		boolean expired = System.currentTimeMillis() - start >= duration;
		if(loop)
			start += duration;
		return expired;
	}
}
