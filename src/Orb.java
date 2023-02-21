import java.awt.Color;
import java.util.ArrayList;

public class Orb {
	//orb motion
	public Vector2 pos = new Vector2(400f, 100f);
	public Vector2 speed = new Vector2();
	public Vector2 acceleration = new Vector2();
	public float radius = 10; //the radius of the orbs
	public float mass = (float) ((radius*radius)*Math.PI);
	
	public float gravity = 98;
	
	//orb appearance
	public Color color = Color.white;
	
	//time stuff
	public static long lastTime = System.nanoTime();
	public static long currentTime = System.nanoTime();
	public static double passedTime;
	
	//other
	public boolean updating = false;
	
	public static void updateTime() {
		currentTime = System.currentTimeMillis(); //get the current time 
		//in millisecond(1e3) change to nanosecond(1e9) for large amount of orbs
		passedTime = (currentTime - lastTime) / 1e3; //finds how much time has passed
		//System.out.println(passedTime + " " +currentTime +" "+lastTime);
		lastTime = currentTime; //set the past time for the next update
		//System.out.println(passedTime);
	}
	
	public void physicsUpdate(int id) {
		acceleration.x = 0;
		acceleration.y = 0;
		applyGravity();
		updatePosition();
		applyCollision(id);
		//applyConstraints();
	}
	
	public void applyGravity() {
		//downward gravity
//		if(y <= GamePanel.screenHeight - radius)
//		{
//			acceleration.y -= 98;
//		}
		
		//central gravity
		Vector2 distanceV = new Vector2();
		distanceV.x = pos.x-GamePanel.screenWidth/2;
		distanceV.y = pos.y-GamePanel.screenHeight/2;
		float distance = (float) Math.hypot(distanceV.x, distanceV.y);
		//System.out.println(pos.x+" and "+distance);
		if(distance > radius) {
			acceleration.x = (distanceV.x/distance)*gravity;
			acceleration.y = (distanceV.y/distance)*gravity;
		}
	}
	
	public void updatePosition() {
		speed.x += acceleration.x * passedTime;
		speed.y += acceleration.y * passedTime;
		//System.out.print("");
		
		pos.x -= speed.x * passedTime;
		pos.y -= speed.y * passedTime;
		//System.out.print("");
	}
	
	public void applyConstraints() {
		if(pos.y <= radius) //top constraint
		{
			pos.y = radius;
			if(speed.y > 0) {
				speed.y = -speed.y*0.3f;
			}
		}
		if(pos.y >= GamePanel.screenHeight - radius) //bottom constraint
		{
			pos.y = GamePanel.screenHeight - radius;
			if(speed.y < 0) {
				speed.y = -speed.y*0.3f;
			}
		}
		if(pos.x <= radius) //top constraint
		{
			pos.x = radius;
			if(speed.x > 0) {
				speed.x = -speed.x*0.3f;
			}
		}
		if(pos.x >= GamePanel.screenWidth - radius) //bottom constraint
		{
			pos.x = GamePanel.screenWidth - radius;
			if(speed.x < 0) {
				speed.x = -speed.x*0.3f;
			}
		}
	}
	
	public void applyCollision(int id) {
		if(GamePanel.orbs.size() >= 2) {
			for(int i = id+1; i < GamePanel.orbs.size(); i++) {
				Orb target = GamePanel.orbs.get(i);
				Vector2 vecteur = new Vector2(); 
				float distance = vecteur.distance(pos, target.pos);
				//System.out.println(pos.x+" : "+target.pos.x+" : "+distance);
				if(Math.abs(distance) < target.radius + radius) {
					//position based collision
					float totRadius = radius+target.radius;
					float totOverlap = (float) (totRadius-distance);
					float overlapRatio = totOverlap/totRadius;
					
					Vector2 overlap = new Vector2((pos.x-target.pos.x)*overlapRatio, (pos.y-target.pos.y)*overlapRatio);
					
					pos.x += (radius/totRadius)*overlap.x;
					target.pos.x -= (target.radius/totRadius)*overlap.x;
					pos.y += (radius/totRadius)*overlap.y;
					target.pos.y -= (target.radius/totRadius)*overlap.y;
					
					//modify the speed
					Vector2 newSpeed = new Vector2((speed.x+target.speed.x)/2, (speed.y+target.speed.y)/2);
					
					speed.x = newSpeed.x;
					target.speed.x = newSpeed.x;
					speed.y = newSpeed.y;
					target.speed.y = newSpeed.y;
//					float normalX = target.x-x;
//					float normalY = target.y-y;
//					
//					float magnitude = (float) Math.sqrt((normalX*normalX)+(normalY*normalY));
//					float unitNormalX = normalX / magnitude;
//					float unitNormalY = normalY / magnitude;
					
					/*Vector2D v_n = b2.pos() - b1.pos(); // v_n = normal vec. - a vector normal to the collision surface
	Vector2D v_un = v_n.unitVector(); // unit normal vector
	Vector2D v_ut(-v_un.y(), v_un.x()); // unit tangent vector
	
	// Compute scalar projections of velocities onto v_un and v_ut
	double v1n = v_un * b1.v(); // Dot product
	double v1t = v_ut * b1.v();
	double v2n = v_un * b2.v();
	double v2t = v_ut * b2.v();
	
	// Compute new tangential velocities
	double v1tPrime = v1t; // Note: in reality, the tangential velocities do not change after the collision
	double v2tPrime = v2t;
	
	// Compute new normal velocities using one-dimensional elastic collision equations in the normal direction
	// Division by zero avoided. See early return above.
	double v1nPrime = (v1n * (b1.m() - b2.m()) + 2. * b2.m() * v2n) / (b1.m() + b2.m());
	double v2nPrime = (v2n * (b2.m() - b1.m()) + 2. * b1.m() * v1n) / (b1.m() + b2.m());
	
	// Compute new normal and tangential velocity vectors
	Vector2D v_v1nPrime = v1nPrime * v_un; // Multiplication by a scalar
	Vector2D v_v1tPrime = v1tPrime * v_ut;
	Vector2D v_v2nPrime = v2nPrime * v_un;
	Vector2D v_v2tPrime = v2tPrime * v_ut;
	
	// Set new velocities in x and y coordinates
	b1.setVX(v_v1nPrime.x() + v_v1tPrime.x());
	b1.setVY(v_v1nPrime.y() + v_v1tPrime.y());
	b2.setVX(v_v2nPrime.x() + v_v2tPrime.x());
	b2.setVY(v_v2nPrime.y() + v_v2tPrime.y());
					*/
				}
			}
		}
		
	}
	
	
	
}
