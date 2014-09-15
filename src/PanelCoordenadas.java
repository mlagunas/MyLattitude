

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.SQLException;

import javax.swing.*;


public class PanelCoordenadas extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JList lista = new JList();
	
	
	
	public void setLista(DatabaseConnection datab,int choice,int value) throws SQLException{		
		VectorElementos<User> VC = null;
		String[] list = new String[value];
		if(value!=0){
			VC = datab.getClosestUsers(value,choice);
		
			for(int i = 0; i < VC.size(); i++){
				if(VC.get(i)!=null){
					list[i]=VC.get(i).name.trim()+":  "+ VC.get(i).position.longitude+" N; "+VC.get(i).position.latitude+" O";	
				}
			}
		}	
			lista.setListData(list);
	}
	
	public PanelCoordenadas(){
		
		JScrollPane Scroll= new JScrollPane(lista); 
        this.add(Scroll,BorderLayout.CENTER);
        this.setLayout(new FlowLayout());
        
        
		
	}
	
	

}
