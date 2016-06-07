/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
 
import javax.swing.ImageIcon;
import javax.swing.JPanel;
 
/**
 * 
 * Clase que extiende de JPanel y permite poner una imagen como fondo.
 * 
 * @author Guille Rodriguez Gonzalez ( http://www.driverlandia.com )
 * @version 1.0 | 05-2014
 * 
 */
 
public class JPanelBackground extends JPanel {
 
	// Atributo que guardara la imagen de Background que le pasemos.
	private Image background;
        
        
 
	// Metodo que es llamado automaticamente por la maquina virtual Java cada vez que repinta
	public void paintComponent(Graphics g) {
 
		/* Obtenemos el tamaño del panel para hacer que se ajuste a este
		cada vez que redimensionemos la ventana y se lo pasamos al drawImage */
		int width = this.getSize().width;
		int height = (int)(this.getSize().height*0.9);
 
		// Mandamos que pinte la imagen en el panel
		if (this.background != null) {
			g.drawImage(this.background, 0, 0, width, height, null);
		}
 
		super.paintComponent(g);
                
/*                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                Line2D lin = new Line2D.Float(5, 10, 5,550);
                
                g2.draw(lin);*/
	}
 
	// Metodo donde le pasaremos la dirección de la imagen a cargar.
	public void setBackground(String imagePath) {
		
		// Construimos la imagen y se la asignamos al atributo background.
		this.setOpaque(false);
		this.background = new ImageIcon(getClass().getResource(imagePath)).getImage();
		repaint();
	}
        
        public void setBackground(BufferedImage img){
            this.setOpaque(false);
            this.background = img;
            repaint();
        }
 
}