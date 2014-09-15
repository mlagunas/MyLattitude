

import java.util.Comparator;

public class ComparadorElementos<T extends User> implements Comparator<T> {
	
	T referencia;
	
	public ComparadorElementos(T ref){
		referencia=ref;
	}
	@Override
	public int compare(T e0, T e1) {
		if (e0.distancia(referencia) == e1.distancia(referencia)){
			return 0;
		}
		else if (e0.distancia(referencia) < e1.distancia(referencia)){
			return -1;
		}
		else{
			return 1;
		}
	}

}
