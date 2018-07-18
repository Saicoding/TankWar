package Main;

import java.util.Date;

public class Timer {
	long startTime = 0;
	boolean running = false;
	int elapsedTime = 0;
	
	public void start() {
	      this.startTime = new Date().getTime();
	      this.elapsedTime = 0;
	      this.running = true;
	}
	
	public void stop() {
	      this.elapsedTime = (int)(new Date().getTime() - this.startTime);
	      this.running = false;
	}
	
	public int getElaplseTime() {
	      if (this.running) return ((int)(new Date().getTime() - this.startTime));
	      else              return this.elapsedTime;
	}
	
	public void reset() {
	      this.elapsedTime = 0;
	      this.startTime = 0;
	      this.running = false;
	}
}
