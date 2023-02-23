
public class Vector2 {
	public float x;
	public float y;
	
	//Constructors
	public Vector2() {
	this.x = 0.0f;
	this.y = 0.0f;
	}
	public Vector2(float x, float y) {
	this.x = x;
	this.y = y;
	}
	
	public float magnitude(Vector2 vec) {
		float magnitude = (float) Math.sqrt((vec.x*vec.x)+(vec.y*vec.y));
		return magnitude;
	}
	
	public Vector2 unitVector(Vector2 vector, float magnitude) {
		Vector2 unit = new Vector2(vector.x / magnitude, vector.y / magnitude);
		return unit;
	}
	
	public Vector2 add(Vector2 a, Vector2 b) {
		Vector2 result = new Vector2(a.x+b.x, a.y+b.y);
		return result;
	}
	public Vector2 subtract(Vector2 a, Vector2 b) {
		Vector2 result = new Vector2(a.x-b.x, a.y-b.y);
		return result;
	}
	public Vector2 multiply(Vector2 vector, float scalar) {
		Vector2 result = new Vector2(vector.x * scalar, vector.y * scalar);
		return result;
	}
	public Vector2 divide(Vector2 vector, float scalar) {
		Vector2 result = new Vector2(vector.x / scalar, vector.y / scalar);
		return result;
	}
	
	public float dotProduct(Vector2 a, Vector2 b) {
		float dot = (a.x * b.x) + (a.y * b.y);
		return dot;
	}
	
	public float distance(Vector2 a, Vector2 b) {
		float dist = (float) Math.sqrt(((a.x - b.x)*(a.x - b.x)) + ((a.y - b.y)*(a.y - b.y)));
		return dist;
	}
}
