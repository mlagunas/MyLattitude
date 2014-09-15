

import java.util.Vector;

public class VectorCercanos <T extends User> extends Vector<T> implements InterfazCercanos<T>{
	
	public T referencia;
	private int nelementos,capacidad;
	

	public VectorCercanos(int cap, T ref){
		super();
		capacidad = cap;
		referencia=ref;
		nelementos = 0;
	}
	
	
	
	@Override
	public boolean add(T elemento){
		
		if(nelementos<capacidad){
			super.add(elemento);
			nelementos++;
			return true;
		}
		else{
			double aux = super.elementAt(0).distancia(referencia);
			int i = 0;
			int max = 0;
			//Buscamos el elemento mas alejado en el Vector
			
			while(i < capacidad){
				if(aux<super.elementAt(i).distancia(referencia)){
					aux=super.elementAt(i).distancia(referencia);
					max = i;
				}
				i++;
			}
			
		
			
			//Si el User mas alejado del Vector se encuentra a mayor distancia que
			//el que vamos a incluir en el mapa se añade el nuevo User, en caso contrario
			//se deja el que ya estaba
			
			if(aux > elemento.distancia(referencia)){
				super.remove(elementAt(max));
				super.add(elemento);
				return true;
			}
			else
				return false;
			
		}

	}



	@Override
	public VectorElementos<User> getVector() {
		VectorElementos<User> VE = new VectorElementos<User>();
		for(int i = 0; i< super.size(); i++){
			if(super.elementAt(i)!= null)
				VE.add(super.elementAt(i));
			
		}
		return VE;
	}
}









