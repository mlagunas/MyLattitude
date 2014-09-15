


import java.util.Comparator;
import java.util.PriorityQueue;


public class Monticulo<T extends User> implements InterfazCercanos<T> {
	
	public PriorityQueue<T> PQ;
	public T referencia;
	int capacidad;
	
	public Monticulo(int cap, T ref){
		capacidad = cap;
		Comparator<T> comparador = new ComparadorElementos<T>(ref);
		PQ = new PriorityQueue<T>(cap,comparador);	
	}

	@Override
	public boolean add(T elemento) {
		return PQ.add(elemento);
	}

	@Override
	public VectorElementos<User> getVector() {
		VectorElementos<User> VE = new VectorElementos<User>();
		for (int i = 0; i< capacidad;i++){
			if(PQ.peek()!=null)
				VE.add(PQ.poll());
		}
		return VE;
	}
}
