

import java.util.Vector;

public class VectorElementos <T extends User> extends Vector<T>{
	
	public VectorElementos (){
		super();
	}
	
	public VectorElementos<User> getClosest (InterfazCercanos<T> vc,int n){
		if(n != 0){
			for (int i = 0; i < super.size(); i++) {
				
				if(!super.elementAt(i).equals(n)){	
					vc.add(super.elementAt(i));
				}
			}
			return vc.getVector();
		}
		else
			return null;
	}
	
}
