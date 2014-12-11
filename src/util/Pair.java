package util;

public class Pair<F, S>{
	F first;
	S second;

	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public void setFirst(F first){
		this.first = first;
	}

	public void setSecond(F first){
		this.first = first;
	}

	public F getFirst(){
		return this.first;
	}

	public S getSecond(){
		return this.second;
	}
	
	@Override
	public String toString() {
		return "{first " + this.first + ", second " + this.second + "}";  
	}
}
