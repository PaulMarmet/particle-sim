import java.awt.Color;

public class Orb {
	//orb motion
	public Vector2 pos = new Vector2();
	public Vector2 speed = new Vector2();
	public Vector2 acceleration = new Vector2();
	public float radius; //the radius of the orbs
	public float mass;
	
	public static float gravity = Config.getF("FORCE_GRAVITY");
	public static int gravDirs = Config.getInt("GRAVITY_DIRECTION");
	public static String gravDirBool;
	public static float gravConst = Config.getF("GRAVITATIONAL_CONSTANT");
	public static boolean bounds = Config.getBool("BOUNDS");
	public static float rCoefficient = Config.getF("BOUNCE_COEFFICIENT"); //0 to 1
	
	//orb color
	public float r = 0f, g = 0f, b = 0.5f;
	public Color color = Color.white;
	public float highSpeed = 0f;
	
	//time stuff
	public static long lastTime = System.nanoTime(); //currentTimeMillis
	public static long currentTime = System.nanoTime();
	public static double passedTime;
	public static void updateTime() {
		currentTime = System.nanoTime(); //get the current time 
		//in millisecond(1e3) change to nanosecond(1e9) for large amount of orbs
		passedTime = (currentTime - lastTime) / 1e9; //finds how much time has passed
		//System.out.println(passedTime + " " +currentTime +" "+lastTime);
		lastTime = currentTime; //set the past time for the next update
		//System.out.println(passedTime);
	}
	
	//Orb creation, refreshing and radius changing
	public Orb()
	{
		pos = new Vector2(400, 100);
		color = Color.white;
		refresh();
		
	}
	public Orb(Vector2 _pos, float _radius, Color _color)
	{
		pos = _pos;
		if(_radius == 0)
			radius = Config.getF("DEFAULT_ORB_RADIUS");
		else
			radius = _radius;
		mass = (float) ((radius*radius)*Math.PI);
		color = _color;
		refresh();
	}
	public Orb(float x, float y, float _radius, Color _color)
	{
		pos = new Vector2(x, y);
		if(_radius == 0)
			radius = Config.getF("DEFAULT_ORB_RADIUS");
		else
			radius = _radius;
		mass = (float) ((radius*radius)*Math.PI);
		color = _color;
		refresh();
	}
	public static void refresh()
	{
		gravity = Config.getF("FORCE_GRAVITY");
		gravDirs = Config.getInt("GRAVITY_DIRECTION");
		gravConst = Config.getF("GRAVITATIONAL_CONSTANT");
		bounds = Config.getBool("BOUNDS");
		rCoefficient = Config.getF("BOUNCE_COEFFICIENT");
		if(Integer.toBinaryString(gravDirs).length() == 1)
		{gravDirBool = "00"+Integer.toBinaryString(gravDirs);}
		else if(Integer.toBinaryString(gravDirs).length() == 2)
		{gravDirBool = "0"+Integer.toBinaryString(gravDirs);}
		else
		{gravDirBool = Integer.toBinaryString(gravDirs);}
	}
	public void sizeR(float _radius)
	{
		radius = _radius;
		mass = (float) ((radius*radius)*Math.PI);
	}
	public void sizeM(float _mass)
	{
		mass = _mass;
		radius = (float) Math.sqrt(mass / Math.PI);
	}
	
	public void physicsUpdate(int id) {
		acceleration.x = 0;
		acceleration.y = 0;
		mass = (float) ((radius*radius)*Math.PI);
		applyGravity(id);
		updatePosition();
		applyCollision(id);
		if(bounds == true) {applyConstraints();}
		if(true) {speedColors();}
	}
	
	public void speedColors() {
		if(Math.abs(speed.x) > highSpeed) {highSpeed = Math.abs(speed.x);}
		if(Math.abs(speed.y) > highSpeed) {highSpeed = Math.abs(speed.y);}
		r = Math.abs(speed.x) / highSpeed;
		g = Math.abs(speed.y) / highSpeed;
		color = new Color(r, g, b);
	}
	
	public void applyGravity(int id) {
		if(gravDirBool.charAt(2) == '1') {downGravity();}//downward gravity
		if(gravDirBool.charAt(1) == '1') {centerGravity();} //central gravity
		if(gravDirBool.charAt(0) == '1') {orbGravity(id);} //orb related gravity
	}
	public void downGravity() {
		if(pos.y <= GamePanel.screenHeight - radius) {
			acceleration.y -= gravity;
		}
	}
	public void centerGravity() {
		Vector2 distanceV = new Vector2();
		distanceV.x = pos.x-GamePanel.screenWidth/2;
		distanceV.y = pos.y-GamePanel.screenHeight/2;
		float distance = (float) Math.hypot(distanceV.x, distanceV.y);
		
		if(distance > radius) {
			acceleration.x += (distanceV.x/distance)*gravity;
			acceleration.y += (distanceV.y/distance)*gravity;
		}
	}
	public void orbGravity(int id) {
		if(GamePanel.orbs.size() >= 2) {
			for(int i = 0; i < GamePanel.orbs.size(); i++) {
				if(i != id) {
					Orb target = GamePanel.orbs.get(i);
					Vector2 vecteur = new Vector2(); 
					float distance = vecteur.distance(pos, target.pos);
					if(Math.abs(distance) > target.radius + radius) {
						Vector2 orbDist = new Vector2();
						orbDist.x = pos.x-target.pos.x;
						orbDist.y = pos.y-target.pos.y;
						float grav = -(gravConst * target.mass)/distance;
						
						acceleration.x += (orbDist.x/distance)*grav;
						acceleration.y += (orbDist.y/distance)*grav;
					}
				}
			}
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
					/*Vector2 newSpeed = new Vector2((speed.x+target.speed.x)/2, (speed.y+target.speed.y)/2);
					
					speed.x = newSpeed.x;
					target.speed.x = newSpeed.x;
					speed.y = newSpeed.y;
					target.speed.y = newSpeed.y;*/
					Vector2 normal = new Vector2(target.pos.x-pos.x, target.pos.y-pos.y);
					float magnitude = normal.magnitude(normal);
					Vector2 unitNormal = normal.unitVector(normal, magnitude);
					Vector2 unitTangent = new Vector2(-unitNormal.y, unitNormal.x);
					
					//Velocity Normal & Velocity Tangent
					float v1N = unitNormal.dotProduct(unitNormal, speed);
					float v1T = unitNormal.dotProduct(unitTangent, speed);
					float v2N = unitNormal.dotProduct(unitNormal, target.speed);
					float v2T = unitNormal.dotProduct(unitTangent, target.speed);
					
					//Prime velocity normal
					float pV1N = (v1N * (mass - target.mass) + 2 * target.mass * v2N) / (mass + target.mass);
					float pV2N = (v2N * (target.mass - mass) + 2 * mass * v1N) / (target.mass + mass);
					
					//new speed vectors
					Vector2 v_pV1N = unitNormal.multiply(unitNormal, pV1N);
					Vector2 v_pV1T = unitTangent.multiply(unitTangent, v1T);
					Vector2 v_pV2N = unitNormal.multiply(unitNormal, pV2N);
					Vector2 v_pV2T = unitTangent.multiply(unitTangent, v2T);

					// orb coefficient
					Vector2 o1C1 = new Vector2(v_pV1N.x + v_pV1T.x, v_pV1N.y + v_pV1T.y);
					Vector2 o2C1 = new Vector2(v_pV2N.x + v_pV2T.x, v_pV2N.y + v_pV2T.y);
					Vector2 oC0 = new Vector2(((mass * speed.x) + (target.mass * target.speed.x)) / (mass + target.mass), ((mass * speed.y) + (target.mass * target.speed.y)) / (mass + target.mass));

					
					
					//apply new speed
					speed = speed.add(speed.multiply(o1C1, rCoefficient), speed.multiply(oC0, (1 - rCoefficient)));
					target.speed = target.speed.add(target.speed.multiply(o2C1, rCoefficient), target.speed.multiply(oC0, (1 - rCoefficient)));
				}
			}
		}
	}
}
