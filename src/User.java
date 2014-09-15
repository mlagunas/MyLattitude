



public class User {
	public String name;
	public Position position;
	
	public User(String n, Position p) { name=n; position=p; }
	
	public double distancia(User u1){
		double y=u1.position.latitude - position.latitude;
		double x=u1.position.longitude - position.longitude;
		Double res = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
		return res;	
	}
}
