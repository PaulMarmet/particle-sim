import java.awt.Color;
import java.util.ArrayList;

public class Orb {
	//orb motion
	public Vector2 pos = new Vector2();
	public Vector2 speed = new Vector2();
	public Vector2 acceleration = new Vector2();
	public float radius; //the radius of the orbs
	public float mass;
	public boolean massType;
	public ArrayList<Link> links = new ArrayList<Link>();
	public boolean updatePos = true;
	//orb color
	public float r = 0f, g = 0f, b = 0.3f;
	public Color color = Color.white;
	public float highSpeed = 0f;
	
	public static float gravity = Config.getF("FORCE_GRAVITY");
	public static int gravDirs = Config.getInt("GRAVITY_DIRECTION");
	public static String gravDirBool;
	public static float gravConst = Config.getF("GRAVITATIONAL_CONSTANT");
	public static boolean bounds = Config.getBool("BOUNDS");
	public static float rCoefficient = Config.getF("BOUNCE_COEFFICIENT"); //0 to 1
	
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
		rAndM(Config.getF("DEFAULT_ORB_RADIUS"));
		refresh();
		
	}
	public Orb(Vector2 _pos, float _radius, Color _color)
	{
		pos = _pos;
		rAndM(_radius);
		color = _color;
		refresh();
	}
	public Orb(float x, float y, float _radius, Color _color)
	{
		pos = new Vector2(x, y);
		rAndM(_radius);
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
	public void rAndM(float _radius)
	{
		if(_radius == 0)
			GamePanel.orbs.remove(this);
		else
			radius = Math.abs(_radius);
		if(_radius > 0)
		{
			mass = (float) ((radius*radius)*Math.PI);
			massType = true;
		}
		else
		{
			mass = (float) (-(radius*radius)*Math.PI);
			massType = false;
		}
	}
	public void mAndR(float _mass)
	{
		if(_mass == 0)
			GamePanel.orbs.remove(this);
		else
			mass = _mass;
		radius = (float) Math.sqrt(Math.abs(_mass) / Math.PI);
	}
	
	public void physicsUpdate(int id) {
		acceleration.x = 0;
		acceleration.y = 0;
		if(updatePos) {applyGravity(id);}
		if(updatePos) {updatePosition();}
		applyCollision(id);
		if(bounds == true) {applyConstraints();}
		if(updatePos) {speedColors();}
	}
	
	public void speedColors() {
		if(Math.abs(speed.x) > highSpeed) {highSpeed = Math.abs(speed.x);}
		if(Math.abs(speed.y) > highSpeed) {highSpeed = Math.abs(speed.y);}
		r = Math.abs(speed.x) / highSpeed;
		g = Math.abs(speed.y) / highSpeed;
		if(massType)
			color = new Color(r, g, b);
		else
			color = new Color(1 - ((g / 2) + ((1 - b) / 2)), 1 - ((r / 2) + ((1 - b) / 2)), 1 - ((r / 2) + (g / 2)));
		highSpeed -= 0.1 * passedTime;
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
					float distance = Vector2.distance(pos, target.pos);
					if(Math.abs(distance) > target.radius + radius) {
						Vector2 orbDist = new Vector2();
						orbDist.x = pos.x-target.pos.x;
						orbDist.y = pos.y-target.pos.y;
						float grav = (gravConst * target.mass)/distance;
						
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
		if(links.size() > 0)
			doLinks();
		if(GamePanel.orbs.size() >= 2) {
			for(int i = id+1; i < GamePanel.orbs.size(); i++) {
				Orb target = GamePanel.orbs.get(i);
				float distance = Vector2.distance(pos, target.pos);
				//System.out.println(pos.x+" : "+target.pos.x+" : "+distance);
				if(Math.abs(distance) < target.radius + radius) {
					//position based collision
					float totRadius = radius+target.radius;
					float totOverlap = (float) (totRadius-distance);
					float overlapRatio = totOverlap/totRadius;
					
					Vector2 overlap = new Vector2((pos.x-target.pos.x)*overlapRatio, (pos.y-target.pos.y)*overlapRatio);
					
					if(updatePos)
					{
						pos.x += (target.mass/(mass + target.mass))*overlap.x;
						pos.y += (target.mass/(mass + target.mass))*overlap.y;
					}
					if(target.updatePos)
					{
						target.pos.x -= (mass/(mass + target.mass))*overlap.x;
						target.pos.y -= (mass/(mass + target.mass))*overlap.y;
					}
					
					//modify the speed
					/*Vector2 newSpeed = new Vector2((speed.x+target.speed.x)/2, (speed.y+target.speed.y)/2);
					
					speed.x = newSpeed.x;
					target.speed.x = newSpeed.x;
					speed.y = newSpeed.y;
					target.speed.y = newSpeed.y;*/
					Vector2 normal = new Vector2(target.pos.x-pos.x, target.pos.y-pos.y);
					float magnitude = normal.magnitude();
					Vector2 unitNormal = normal.unitVector(magnitude);
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
					Vector2 v_pV1N = unitNormal.multiply(pV1N);
					Vector2 v_pV1T = unitTangent.multiply(v1T);
					Vector2 v_pV2N = unitNormal.multiply(pV2N);
					Vector2 v_pV2T = unitTangent.multiply(v2T);

					// orb coefficient
					Vector2 o1C1 = new Vector2(v_pV1N.x + v_pV1T.x, v_pV1N.y + v_pV1T.y);
					Vector2 o2C1 = new Vector2(v_pV2N.x + v_pV2T.x, v_pV2N.y + v_pV2T.y);
					Vector2 oC0 = new Vector2(((mass * speed.x) + (target.mass * target.speed.x)) / (mass + target.mass), ((mass * speed.y) + (target.mass * target.speed.y)) / (mass + target.mass));

					
					
					//apply new speed
					speed = Vector2.add(o1C1.multiply(rCoefficient), oC0.multiply(1 - rCoefficient));
					target.speed = Vector2.add(o2C1.multiply(rCoefficient), oC0.multiply(1 - rCoefficient));
					
					if(massType != target.massType)
					{
						if(Math.abs(mass) > Math.abs(target.mass))
						{
							mAndR(mass + target.mass);
							target.mAndR(0);
						} else if(Math.abs(mass) == Math.abs(target.mass))
						{
							mAndR(0);
							target.mAndR(0);
						} else
						{
							mAndR(0);
							target.mAndR(target.mass + mass);
						}
					}
				}
			}
		}
	}
	public void doLinks()
	{
		for(int i = 0; i < links.size(); i++)
		{
			//get the  link
			Link link = links.get(i);
			//if the link is broken
			if(!GamePanel.orbs.contains(link.orb1) || !GamePanel.orbs.contains(link.orb2))
			{
				link.orb1.links.remove(link);
				link.orb2.links.remove(link);
				break;
			}
			
			Orb o;
			if(this == link.orb1) {o = link.orb2;}
			else {o = link.orb1;}
			
			//do the stuff
			float dist = Vector2.distance(pos, o.pos);
			if(dist > link.length)
			{
				//fix position
				float overstrech = dist - link.length;
				float overstrechRatio = overstrech/link.length;
				
				Vector2 moving = new Vector2((pos.x-o.pos.x)*overstrechRatio, (pos.y-o.pos.y)*overstrechRatio);
				
				if(updatePos)
				{
					pos.x -= (mass /(mass + o.mass)) * moving.x;
					pos.y -= (mass /(mass + o.mass)) * moving.y;
				}
				if(o.updatePos)
				{
					o.pos.x += (o.mass /(mass + o.mass)) * moving.x;
					o.pos.y += (o.mass /(mass + o.mass)) * moving.y;
				}
				
				//fix speed
				Vector2 normal = new Vector2(pos.x-o.pos.x, pos.y-o.pos.y);
				float magnitude = normal.magnitude();
				Vector2 unitNormal = normal.unitVector(magnitude);
				Vector2 unitTangent = new Vector2(-unitNormal.y, unitNormal.x);
				
				//Velocity Normal & Velocity Tangent
				float v1N = unitNormal.dotProduct(unitNormal, speed);
				float v1T = unitNormal.dotProduct(unitTangent, speed);
				float v2N = unitNormal.dotProduct(unitNormal, o.speed);
				float v2T = unitNormal.dotProduct(unitTangent, o.speed);
				
				//Prime velocity normal
				float pV1N = (v1N * (mass - o.mass) + 2 * o.mass * v2N) / (mass + o.mass);
				float pV2N = (v2N * (o.mass - mass) + 2 * mass * v1N) / (o.mass + mass);
				
				//new speed vectors
				Vector2 v_pV1N = unitNormal.multiply(pV1N);
				Vector2 v_pV1T = unitTangent.multiply(v1T);
				Vector2 v_pV2N = unitNormal.multiply(pV2N);
				Vector2 v_pV2T = unitTangent.multiply(v2T);

				// orb coefficient
				Vector2 o1C1 = new Vector2(v_pV1N.x + v_pV1T.x, v_pV1N.y + v_pV1T.y);
				Vector2 o2C1 = new Vector2(v_pV2N.x + v_pV2T.x, v_pV2N.y + v_pV2T.y);
				Vector2 oC0 = new Vector2(((mass * speed.x) + (o.mass * o.speed.x)) / (mass + o.mass), ((mass * speed.y) + (o.mass * o.speed.y)) / (mass + o.mass));

				
				
				//apply new speed
				speed = Vector2.add(o1C1.multiply(rCoefficient), oC0.multiply(1 - rCoefficient));
				o.speed = Vector2.add(o2C1.multiply(rCoefficient), oC0.multiply(1 - rCoefficient));
			}
		}
	}
}
