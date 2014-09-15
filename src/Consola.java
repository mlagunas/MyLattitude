

import java.util.*;
import java.sql.SQLException;
import java.util.Scanner;


public class Consola {
	/**
	 * @param args
	 */
	
	public static void main(String[] args) throws SQLException {

		System.out.println("Accediendo a Hendrix-oracle.. ");
		Scanner sc=new Scanner(System.in);
		String owner="a504185";
		String password="manu8900529";
		
		DatabaseConnection datab = new DatabaseConnection("jdbc:oracle:thin:@hendrix-oracle.cps.unizar.es:1521:vicious",
		   	    										owner, password);
		System.out.print("Introduzca nombre de usuario: ");
		String user=sc.nextLine();
		System.out.println();
		datab.DatabaseUpdate();
		try { if (!datab.loginUser(user))
			   datab.addUser(user, new Position(0.0,0.0));
			datab.setPropietario(user);
	    	} catch (SQLException e1) {
	    	 e1.printStackTrace();
	    	}

		String dummy, dummy2;
		VectorElementos<User> vdummy;
		Position dummypos = new Position(0.0,0.0);
		
      	boolean quit = false;
        int menuItem;
        do {
    	    // Text menu
    	    System.out.println("1. Listar usuarios");
    	    System.out.println("2. Anadir usuario");
    	    System.out.println("3. Mostrar posicion");
    	    System.out.println("4. Actualizar posicion");
    	    System.out.println("5. Obtener N mas cercanos");
    	    System.out.println("0. Salir y guardar cambios");
            System.out.print("Elige una opcion: ");
            System.out.println();
            menuItem = sc.nextInt(); dummy=sc.nextLine();
            switch (menuItem) {
              case 1:
                    System.out.println("Listado de usuarios");
            		try {
            		  vdummy = datab.getUsers();
            		  for (int i=0; i< vdummy.size(); i++){
            			  System.out.println("Usuario: "+vdummy.get(i).name.trim()+"; posicion: "
            					  +vdummy.get(i).position.longitude+" E; "+vdummy.get(i).position.latitude+" N");
            		  }
            		} catch (SQLException e1) {
            		  e1.printStackTrace();
    		        }
                    break;
              case 2:
                    Position aux;
            	  	System.out.print("Usuario a anadir: ");
                    dummy=sc.nextLine();
            	    System.out.print("Nueva longitud: ");
            	    dummy2=sc.nextLine();
            	    try {
            	    	double longitude = Double.valueOf(dummy2).doubleValue();
	            	    
	            	    System.out.print("Nueva latitud: ");
	            	    dummy2=sc.nextLine();
            	    
	            	    double latitude = Double.valueOf(dummy2).doubleValue();
	            	    
	            	    aux = new Position(latitude, longitude);
            	    } catch (NumberFormatException nfe) {
            	      System.out.println("Formato incorrecto");
            	      continue;
            	    }

           		    try {
           		      datab.addUser(dummy,aux);
           		    } catch (SQLException e1) {
           		     e1.printStackTrace();
   		            }
           		    break;
              case 3:
         		    try {
           		      dummypos = datab.getPosition(user);
          		      System.out.println("Longitud: "+dummypos.longitude+" E; Latitud: "+dummypos.latitude+" N");
         		    } catch (SQLException e1) {
         		     e1.printStackTrace();
 		            }
                  break;
              case 4:
            	      System.out.print("Nueva longitud: ");
            	      dummy=sc.nextLine();
            	      try {
            	    	  dummypos.longitude = Double.valueOf(dummy).doubleValue();
            	      } catch (NumberFormatException nfe) {
            	        System.out.println("Formato incorrecto");
            	        continue;
            	      }
            	      System.out.print("Nueva latitud: ");
            	      dummy=sc.nextLine();
            	      try {
            	    	  dummypos.latitude = Double.valueOf(dummy).doubleValue();
            	      } catch (NumberFormatException nfe) {
            	        System.out.println("Formato incorrecto");
            	        continue;
            	      }
       		      try { 
       		          datab.updatePosition(user, dummypos);
       		      } catch (SQLException e1) {
       		        e1.printStackTrace();
		      }
                break;
              case 5:
            	  int num;
            	  try{
            		  System.out.print("Introduzca el numero de personas mas cercanas a ver: ");
            		  num = sc.nextInt();
            	  } catch (NumberFormatException nfe) {
          	        System.out.println("Formato incorrecto");
          	        continue;
          	      }
            	  try{
            		  
            		  System.out.println("0-Vector");
            		  System.out.println("1-Monticulo");
            		  System.out.println("Elija la estructura donde almacenar los datos: ");
            		  int choice = sc.nextInt();
            		  VectorElementos<User> VC = datab.getClosestUsers(num,choice);
            		  for(int i = 0; i < VC.size(); i++){
            			  System.out.println("Usuario: " + VC.get(i).name.trim() +" posicion: " + VC.get(i).position.longitude+" E " +
            					  VC.get(i).position.latitude+" N");
            		  }
            	  } catch (SQLException se) {
            		  se.printStackTrace();
            	  }
            	  break;
            	  
            case 0:
            	  System.out.println("Hasta luego");
                  quit = true;
                  datab.DatabaseUpload();
                  break;
            default:
                  System.out.println("Opcion invalida");
            }
       } while (!quit);
		
       }
	
}
