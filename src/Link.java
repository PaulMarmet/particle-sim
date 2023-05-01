
public class Link {
	public Orb orb1;
	public Orb orb2;
	public int type; //0 for stuck against, 1 for max distance
	public float length;
	
	public Link(Orb _orb1, Orb _orb2, int type)
	{
		orb1 = _orb1;
		orb2 = _orb2;
		if(type == 0)
			length = orb1.radius + orb2.radius;
		else if(type == 1)
			length = Vector2.sDistance(orb1.pos, orb2.pos);
		orb1.links.add(this);
		orb2.links.add(this);
	}
}
