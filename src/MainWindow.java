


import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


import java.sql.SQLException;
import java.text.*;


public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static final String mapname = "data/Iberian_Peninsula.jpg";
	// Los limites geograficos del mapa son: N: 44.4º N; S: 34.7º N; W: 9.9º; E: 4.8ºE
	public static final double minlongitude = -4.19;
	public static final double maxlongitude = 9.18; 
	public static final double minlatitude = 34.7; 
	public static final double maxlatitude = 44.4;
	public static int tipo=-1; //1 Vector 2 Monticulo
    public static int num,mostrando ;
	
    PanelPosition panelposition;
    Map	 imagen;
    PanelCursor panelcursor;
    PanelCoordenadas panelCoordenadas;
    PanelDistancias panelDistancias;
    DatabaseConnection database;
    JSlider sl;
    JFrame elegir;
    
    String username;
    
    public MainWindow(DatabaseConnection db)
    {
    	database = db;
    	database.DatabaseUpdate();
    	num = database.users.size();
    	mostrando=num;
        imagen = new Map(mapname,minlatitude,minlongitude,maxlatitude,maxlongitude);
        panelposition = new PanelPosition();
		panelcursor = new PanelCursor();
		panelCoordenadas=new PanelCoordenadas();
		panelDistancias=new PanelDistancias();
    }
    
    public void start() throws SQLException
    {
    	String s = (String)JOptionPane.showInputDialog(
                this,
                "Nombre de usuario:",
                "Login",
                JOptionPane.PLAIN_MESSAGE);
    	
	    this.setTitle("Mapa de Localizaciones");
	    
	    
		// Si se obtuvo una cadena...
		if ((s != null) && (s.length() > 0)) {
			this.setTitle("MyLatitude - "+s);
			WindowListener exitListener = new WindowAdapter() {

	            @Override
	            public void windowClosing(WindowEvent e) {
	                int confirm = JOptionPane.showOptionDialog(null, "¿Estar seguro de salir de MyLatitude?", "Exit Confirmation",
	                						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
	                System.out.println(confirm);
	                if (confirm == 0) {
	                	database.DatabaseUpload();
	                    System.exit(0);
	                }
	                
	                	
	            }
	        };
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        this.addWindowListener(exitListener);
			if (!database.loginUser(s))
				database.addUser(s, new Position(41.39,0.53));
			database.setPropietario(s);
			username=s;
			
			//Menu seleccion vector o monticulo
			elegir = new JFrame("Elegir Estructura");
			elegir.setLayout(new GridBagLayout());
			elegir.setSize(220, 100);
			elegir.setBounds(100,150,200,100);
			elegir.setLocationRelativeTo(null);

	        JButton bot = new JButton("Vector");
	        bot.setBounds(10,10,90,30);
	        JButton bot2 = new JButton ("Monticulo");
	        bot2.setBounds(100,10,90,30);
	        elegir.add(bot);
	        elegir.add(bot2);
	        elegir.setVisible(true);
	        
	        bot.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		tipo=0;
		    		elegir.setVisible(false);
		    		try {
						distribute();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    });
	        
	        bot2.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		tipo=1;
		    		elegir.setVisible(false);
		    		try {
						distribute();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}
		    });
		}
		else
			this.dispose();
     }

	private void distribute() throws SQLException 
	{
		panelposition.setPosition(database.getPosition(username));
		panelCoordenadas.setLista(database,tipo,mostrando);
		panelDistancias.setLista(database, tipo, mostrando, new User(username, panelposition.getPosition()) );

	    //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //Creamos un panel para la derecha, en el centro esta el mapa
	    BorderLayout layout = new BorderLayout();
	    
	    JPanel right = new JPanel(); 
	    JPanel data = new JPanel();
	    right.add(data,BorderLayout.CENTER);
	    
	    
	    
	    //Creamos el Bot�n actualizar
	    
	    JButton boton = new JButton("Ver usuarios cercanos");
	    
	    //Creamos bot�n salida
	    
	    JButton botonS = new JButton("Salir sin guardar");
	    JButton botonSs = new JButton("Guardar y salir");
	    
	    //En el panel de la derecha, donde estan los datos, vamos a ponerlos uno debajo de
	    //otro (BoxLayout)
	    BoxLayout dataLayout = new BoxLayout(data,BoxLayout.Y_AXIS);
	    data.setLayout(dataLayout);
	    
	    JPanel viewportPanel = new JPanel();
	    viewportPanel.add(imagen,BorderLayout.CENTER);
	    
	    this.getContentPane().setLayout(layout);
	    this.getContentPane().add(new JScrollPane(viewportPanel),BorderLayout.CENTER);
	    this.getContentPane().add(right,BorderLayout.EAST);
	    
	    
	    //Create the radio buttons.
	    JRadioButton normalBot = new JRadioButton("Normal");
	    normalBot.setMnemonic(KeyEvent.VK_B);
	    normalBot.setActionCommand("Normal");
	    normalBot.setSelected(true);

	    JRadioButton fisicoBot = new JRadioButton("Fisico");
	    fisicoBot.setMnemonic(KeyEvent.VK_C);
	    fisicoBot.setActionCommand("Fisico");

	    JRadioButton poliBot = new JRadioButton("Politico");
	    poliBot.setMnemonic(KeyEvent.VK_D);
	    poliBot.setActionCommand("Politico");

	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(normalBot);
	    group.add(fisicoBot);
	    group.add(poliBot);
	    
	    JPanel mapas = new JPanel();
		mapas.setLayout(new GridBagLayout());
	    TitledBorder titleMapas = BorderFactory.createTitledBorder("Mapas disponibles");
	    mapas.setBorder(titleMapas);
	    
	    mapas.add(normalBot);
	    mapas.add(fisicoBot);
	    mapas.add(poliBot);
	    
	    data.add(mapas);
	    data.add(panelposition);
	    TitledBorder titleCoordenadas = BorderFactory.createTitledBorder("Coordenadas locales");
	    panelposition.setBorder(titleCoordenadas);
	    data.add(panelCoordenadas);
	    TitledBorder titleCoordenadas2 = BorderFactory.createTitledBorder("Coordenadas cercanos");
	    panelCoordenadas.setBorder(titleCoordenadas2); 
	    JPanel nCercanos = new JPanel(new GridLayout());
	    intializeSlider(num);
	    nCercanos.add(sl); 
	    JPanel act = new JPanel(new GridBagLayout());
	    act.add(boton);
	    data.add(act);
	    data.add(nCercanos);
	    TitledBorder titleSlider = BorderFactory.createTitledBorder("Numero cercanos");
	    sl.setBorder(titleSlider);
	    JButton dist = new JButton("Mostrar distancias");
	    JPanel dt = new JPanel(new GridBagLayout());
	    dt.add(dist);
	    data.add(dt);
	    data.add(panelDistancias);
	    TitledBorder titleDistancias = BorderFactory.createTitledBorder("Distancias cercanos");
	    panelDistancias.setBorder(titleDistancias); 
	    data.add(panelcursor);
	    TitledBorder titleCursor = BorderFactory.createTitledBorder("Coordenadas del cursor");
	    panelcursor.setBorder(titleCursor);
	    JPanel salida = new JPanel (new GridLayout());
	    salida.add(botonS); 
	    salida.add(botonSs);
	    data.add(salida);
	    
	    dist.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ac) {
	    		VectorElementos<User> v; 
	    		imagen.reset();
				imagen.mark(panelposition.getPosition(), Color.red, "YO");
				
		 		try {
					if(mostrando != 0){
						v = database.getClosestUsers(mostrando, tipo);
						for(User u: v){
							imagen.markDistance(new User(username,panelposition.getPosition()), u, Color.blue);
							imagen.lines(panelposition.getPosition(), u.position, Color.lightGray);
						}
					}
						
				} catch (SQLException e) {
					e.printStackTrace();
				}
	 			imagen.repaint();
	    	}
	    });
	    
	    boton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		try {
					panelCoordenadas.setLista(database,tipo,mostrando);
					panelDistancias.setLista(database, tipo, mostrando, new User(username, panelposition.getPosition()));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    		updateImage(true);
	    	}
	    });
	    
	    botonS.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(0);
	    	}
	    });
	    
	    botonSs.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		database.DatabaseUpload();
	    		System.exit(0);
	    	}
	    });
	    
	    normalBot.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		imagen.cambiarImagen("data/Iberian_Peninsula.jpg");
	    		updateImage(false);

	    	}
	    });
	   
	   fisicoBot.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		imagen.cambiarImagen("data/fisico.jpg");
	    		updateImage(false);

	    	}
	    });
	   
	   poliBot.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		imagen.cambiarImagen("data/politico.jpg");
	    		updateImage(false);
	    	}
	    });
	    
		// Listeners
		// Mapa
	    imagen.addMouseListener(new MouseAdapter() {
	    	public void mousePressed(MouseEvent e)
	    	{	
	    		updateCoordinatesFromMap(e.getX(),e.getY());
	    		
	    	}
	    });
	   
	    imagen.addMouseMotionListener(new MouseMotionAdapter() {
	    	public void mouseMoved(MouseEvent e)	
	    	{	
	    		updateStatusPanel(e.getX(),e.getY());
	    	}
	    });
	    
	    panelposition.addChangeListener(new ChangeListener() {
	    	public void stateChanged(ChangeEvent e) 
	    	{  
	    	     updateDatabaseFromCoordinates();
	    	     updateImage(false);
	    	}
	    });
	    
	    
		this.pack();
	    this.setVisible(true);
	    updateImage(false);
		// Redibujar mapa y circulo
	}
	
	
    public void updateStatusPanel(int x, int y) {
    	Position position = imagen.positionFromPixelCoordinates(x,y);
		DecimalFormat df = new DecimalFormat("#.###");
		panelcursor.cursorLatitud.setText(df.format(position.latitude));
		panelcursor.cursorLongitud.setText(df.format(position.longitude));
     }
    
    public void updateDatabaseFromMap(int x, int y)
    {
    	try {
			database.updatePosition(username,imagen.positionFromPixelCoordinates(x,y));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }
     
    public void updateDatabaseFromCoordinates()
    {
    	try {
    		database.updatePosition(username,panelposition.getPosition());
    		panelCoordenadas.setLista(database,tipo,mostrando);
			panelDistancias.setLista(database, tipo, mostrando, new User(username, panelposition.getPosition()));

    	} catch (SQLException e1) {
			e1.printStackTrace();
		}
    }

    public void updateCoordinatesFromMap(int x, int y)
    {
    	panelposition.setPosition(imagen.positionFromPixelCoordinates(x,y));
    	
    }
    
    private void intializeSlider(int num){
		sl = new JSlider(JSlider.HORIZONTAL,0,num,num);
		sl.addChangeListener(
				
				new ChangeListener(){

					@Override
					public void stateChanged(ChangeEvent arg0) {
						mostrando = sl.getValue();
						try {
							panelCoordenadas.setLista(database, tipo, mostrando);
							panelDistancias.setLista(database, tipo, mostrando, new User(username, panelposition.getPosition()));

							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			);
		sl.setMajorTickSpacing(5); sl.setMinorTickSpacing(1);
		sl.setPaintTicks(true);
		sl.setPaintLabels(true);
		
	}
    
    public void updateImage(boolean actualizar)
     {
    		
    		VectorElementos<User> v; 
			imagen.reset();
			imagen.mark(panelposition.getPosition(), Color.red, "YO");
			if(actualizar){
	 			try {
					if(mostrando != 0){
						v = database.getClosestUsers(mostrando, tipo);
					
						for(User u: v){
							imagen.mark(u.position, Color.blue,u.name);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
 			imagen.repaint();
     }
}
