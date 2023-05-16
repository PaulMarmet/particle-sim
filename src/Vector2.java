
public class Vector2 {
	public float x;
	public float y;
	public float angle;
	
	//Constructors
	public Vector2() {
	this.x = 0.0f;
	this.y = 0.0f;
	}
	public Vector2(float x, float y) {
	this.x = x;
	this.y = y;
	}
	public Vector2(float x, float y, float angle) {
	this.x = x;
	this.y = y;
	this.angle = angle;
	}
	
	public float magnitude() {
		float magnitude = (float) Math.sqrt((x*x)+(y*y));
		return magnitude;
	}
	
	public static Vector2 unitVector(Vector2 vector, float magnitude) { //static version
		Vector2 unit = new Vector2(vector.x / magnitude, vector.y / magnitude);
		return unit;
	}
	public Vector2 unitVector(float magnitude) { //non static version
		Vector2 unit = new Vector2(x / magnitude, y / magnitude);
		return unit;
	}
	
	public static Vector2 add(Vector2 vector1, Vector2 vector2) { //static version
		Vector2 result = new Vector2(vector1.x+vector2.x, vector1.y+vector2.y);
		return result;
	}
	public Vector2 add(Vector2 vector) { //non static version
		Vector2 result = new Vector2(x+vector.x, y+vector.y);
		return result;
	}
	public static Vector2 subtract(Vector2 vector1, Vector2 vector2) { //static version
		Vector2 result = new Vector2(vector1.x-vector2.x, vector1.y-vector2.y);
		return result;
	}
	public Vector2 subtract(Vector2 vector1) { //non static version
		Vector2 result = new Vector2(x-vector1.x, y-vector1.y);
		return result;
	}
	public Vector2 multiply(float scalar) {
		Vector2 result = new Vector2(x * scalar, y * scalar);
		return result;
	}
	public Vector2 divide(float scalar) {
		Vector2 result = new Vector2(x / scalar, y / scalar);
		return result;
	}
	
	public float dotProduct(Vector2 vector1, Vector2 vector2) {
		float dot = (vector1.x * vector2.x) + (vector1.y * vector2.y);
		return dot;
	}
	
	public static float distance(Vector2 a, Vector2 b) {
		float dist = (float) Math.sqrt(((a.x - b.x)*(a.x - b.x)) + ((a.y - b.y)*(a.y - b.y)));
		return dist;
	}
	public float distance(Vector2 vector) {
		float dist = (float) Math.sqrt(((x - vector.x)*(x - vector.x)) + ((y - vector.y)*(y - vector.y)));
		return dist;
	}
}
