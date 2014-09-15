

public class StructureFactory {
	public static InterfazCercanos<User> structureFactoryMethod(int choice, int cap, User ref) {
		InterfazCercanos<User> product = null;
		switch (choice) {
			case 0:
				product = new VectorCercanos<User>(cap,ref);
				break;
			case 1:
				product = new Monticulo<User>(cap,ref);
				break;
	
		}
		return product;	
		}
	}
