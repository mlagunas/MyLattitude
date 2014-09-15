


import java.sql.*;




public class DatabaseConnection {
	
	private String db_driver;
	private String db_username;
	private String db_password;
	
	private String propietario;
	public VectorElementos<User> users;
	
	public void DatabaseUpdate(){
		 Connection connection;
		 try {
			 connection = DriverManager.getConnection(db_driver, db_username,db_password);
			 users = new VectorElementos<User>();
	 
			 // Get a statement from the connection
			 Statement stmt = connection.createStatement() ;
			 // Execute the query
			 ResultSet rs = stmt.executeQuery("SELECT * FROM users") ;

			 while(rs.next()){
				 String nombre =rs.getString(1).trim();
				 double latitude = rs.getDouble(2);
				 double longitude = rs.getDouble(3);
				 
				 Position p = new Position(latitude,longitude);
				 User usuario= new User(nombre,p);
				 users.add(usuario);
			 }
			 
			 // Close the statement and the connection
			 stmt.close() ;
			 connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DatabaseUpload(){
		Connection connection;
		try {
			connection = DriverManager.getConnection(db_driver, db_username,db_password);
			Statement stmt = connection.createStatement();
	        stmt.executeUpdate(
	            "DROP TABLE users ");
	        stmt.executeUpdate(
		            "CREATE TABLE users "+
		            "( Nombre char(100) UNIQUE NOT NULL,"+
		            " Latitud float NOT NULL,"+
		            " Longitud float NOT NULL)");
	        VectorElementos<User> aux = new VectorElementos<User>();
	        for(User u: users){
	        	if(!aux.contains(u)){
	        		aux.add(u);
	        		stmt.executeUpdate( "INSERT INTO users VALUES "+
	        				"('"+u.name+"',"+u.position.latitude+","+u.position.longitude+")");
	        	}
	        }
	        stmt.close();
	        connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	    	  
	}
	
	public DatabaseConnection(String driver, String username, String password) 
	{
		db_driver = driver;
		db_username = username;
		db_password = password;
		
		if (driver.contains("oracle"))
		{
			try {
				Class.forName ("oracle.jdbc.OracleDriver");
			} catch (ClassNotFoundException e) {
				System.err.println("Oracle driver not found");
				e.printStackTrace();
			}
		}
	}
	
	public void addUser(String user, Position position) throws SQLException
	{
		users.add(new User(user,position));
		
	}
	
	public void setPropietario(String user){
		propietario=user;
	}

	public Position getPosition(String user) throws SQLException
	{
		for(User u: users)
	    	  if(u.name.trim().equals(user))
	    		  return u.position;
	      
	      return null;
	}

	public boolean loginUser(String user) throws SQLException
	{

	      for(User u: users)
	    	  if(u.name.equals(user))
	    		  return true;
	      
	      return false;
	}

	public void updatePosition(String user, Position position) throws SQLException
	{
		for(User u: users)
	    	  if(u.name.trim().equals(user))
	    		  u.position = position;
	      
	}
	
	public VectorElementos<User> getUsers() throws SQLException
	{
	     VectorElementos<User> result = new VectorElementos<User>();
	     for(User u: users){
	    	 if(!u.name.trim().equals(propietario)){
	    		  result.add(u);
	    	 }
	     }
	      
	      return result;
	}
	
	public VectorElementos<User> getClosestUsers(int n, int choice) throws SQLException
	{
		if(n!=0){
			User aux = null;  
			VectorElementos<User> result = new VectorElementos<User>();
		    for(User u: users){
		    	  if(!u.name.trim().equals(propietario))
		    		  result.add(u);
		    	 else 
		    		 aux = new User(u.name, u.position);
		    	 
		     }
		     
		     InterfazCercanos<User> estructura = StructureFactory.structureFactoryMethod(choice, n, aux);
		     //VectorCercanos<common.User> vc = new VectorCercanos<common.User>(n, aux);
		     
		     // Close the statement and the connection
		     return result.getClosest(estructura, n);
		}
		else 
			return null;
	}
	
	
}
