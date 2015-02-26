/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package breakingbad;

import java.awt.Image;
import java.awt.Toolkit;


public class BarraG extends Base{
    private static int conteo = 0;
    //private Animacion anim;

    //Metodo constructor que hereda los atributos de la clase ObjetoEspacial
    // posX es la posicion en x del planeta
    // posY es la posicion en y del planeta
    
    public BarraG(int posX, int posY) {
        super(posX, posY);

	Image barra = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("camion.png"));
       
        
        //Se inicializa la animación
        anim = new Animacion();
	
	anim.sumaCuadro(barra, 100);
        
        conteo++;
    }
    
    public static int getConteo() {
        return conteo;
    }
    
    public static void setConteo(int cont) {
        conteo = cont;
    }
    
    //Método que actualiza la animacion
    public void actualiza(long tiempo) {
        anim.actualiza(tiempo);
    }
}
