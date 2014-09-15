

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.awt.geom.*;

public class Map extends Component {
          
	private static final long serialVersionUID = 1L;
	BufferedImage image; // mapa con puntos de coordenadas
    BufferedImage source_image; // mapa original sin puntos coordenadas
	double minlongitude, maxlongitude, minlatitude, maxlatitude;
	
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), // scaled dimension
        				   0, 0, image.getWidth(null), image.getHeight(null), // original dimension
        				 null);
    }

    public Map(String filename, double minlat, double minlon,
    		double maxlat, double maxlon) {
    	minlongitude = minlon; minlatitude = minlat;
    	maxlongitude = maxlon; maxlatitude = maxlat;
       try {
            image = ImageIO.read(new File(filename));
			// Hacemos una copia para cuando haya que actualizar el mapa
            source_image = deepCopy(image);
       } catch (IOException e) {
       }
    }   
       
    public Dimension getPreferredSize() {
        if (image == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(image.getWidth(), image.getHeight());
       }
    }

	public void reset() {
		image = deepCopy(source_image);
	}
	
	Position positionFromPixelCoordinates(int x, int y)
	{
		return new Position(
				maxlatitude-((double)y/(double)image.getHeight())
								*(maxlatitude-minlatitude),
				maxlongitude-((double)x/(double)image.getWidth())
								*(maxlongitude-minlongitude));
	}
	
	public void mark(Position position, Color color,String txt)
	{
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(color);
		
		
		int x = (int)(((maxlongitude-position.longitude)/
						(maxlongitude-minlongitude))*(double)image.getWidth());
		int y = (int)(((maxlatitude-position.latitude)/
				(maxlatitude-minlatitude))*(double)image.getHeight());
		g2d.draw(new Ellipse2D.Double(x-2,y-2,4,4));
		g2d.drawString(txt, x+2, y+2);
	
	}
	
	public void markDistance(User usr1, User usr2, Color color){
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(color);
		
		
		int x = (int)(((maxlongitude-usr2.position.longitude)/
						(maxlongitude-minlongitude))*(double)image.getWidth());
		int y = (int)(((maxlatitude-usr2.position.latitude)/
				(maxlatitude-minlatitude))*(double)image.getHeight());
		g2d.draw(new Ellipse2D.Double(x-2,y-2,4,4));
		Double dist = usr2.distancia(usr1);
		if(dist!= 0.0)
		g2d.drawString(dist.toString(), x+2, y+2);
	}
	
	public void lines(Position pos1, Position pos2, Color color){
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(color);
		int x1 = (int)(((maxlongitude-pos1.longitude)/
				(maxlongitude-minlongitude))*(double)image.getWidth());
		int y1 = (int)(((maxlatitude-pos1.latitude)/
				(maxlatitude-minlatitude))*(double)image.getHeight());
		int x2 = (int)(((maxlongitude-pos2.longitude)/
						(maxlongitude-minlongitude))*(double)image.getWidth());
		int y2 = (int)(((maxlatitude-pos2.latitude)/
				(maxlatitude-minlatitude))*(double)image.getHeight());
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawLine(x1, y1, x2, y2);


	}
	
	 public void cambiarImagen(String filen){
	    	try {
	            image = ImageIO.read(new File(filen));
				// Hacemos una copia para cuando haya que actualizar el mapa
	            source_image = deepCopy(image);
	       } catch (IOException e) {
	       }
	    	
	    }

	static BufferedImage deepCopy(BufferedImage bi) {
 		ColorModel cm = bi.getColorModel();
 		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
 		WritableRaster raster = bi.copyData(null);
 		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}
