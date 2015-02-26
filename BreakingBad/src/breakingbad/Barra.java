/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package breakingbad;

import java.awt.Image;
import java.awt.Toolkit;

public class Barra extends Base {

    private final static String PAUSE = "PAUSADO";
    private final static String DISP = "DESAPARECE";
    private boolean exist;

    /**
     * @return the DISP
     */
    public static String getDISP() {
        return DISP;
    }

    /**
     * @return the PAUSE
     */
    public static String getPAUSE() {
        return PAUSE;
    }

    

    /**
     * Metodo constructor que hereda los atributos de la clase
     * <code>Base</code>.
     *
     * @param posX es la <code>posiscion en x</code> del objeto elefante.
     * @param posY es el <code>posiscion en y</code> del objeto elefante.
     * @param elefN es la <code>imagen</code> del los objetos elefs.
     * @param anim es la <code>Animacion</code> del objeto elefante.
     * @param num es la cantidad de elefes <code>Int</code> del objeto elefante.
     */
    public Barra(int posX, int posY) {
        super(posX, posY);
        exist= true;
        //Se cargan las imágenes(cuadros) para la animación

        Image barr1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("1.png"));
        Image barr2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("2.png"));
        Image barr3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("3.png"));
        Image barr4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("4.png"));
        Image barr5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("5.png"));
        Image barr6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("6.png"));
        Image barr7 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("7.png"));
        Image barr8 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("8.png"));
        Image barr9 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("9.png"));
        Image barr10 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("10.png"));

        //Se crea la animación
        anim = new Animacion();

        anim.sumaCuadro(barr1, 120);
        anim.sumaCuadro(barr2, 120);
        anim.sumaCuadro(barr3, 120);
        anim.sumaCuadro(barr4, 120);
        anim.sumaCuadro(barr5, 120);
        anim.sumaCuadro(barr6, 120);
        anim.sumaCuadro(barr7, 120);
        anim.sumaCuadro(barr8, 120);
        anim.sumaCuadro(barr9, 120);
        anim.sumaCuadro(barr10, 120);
    }
    /**
     * Metodo que hace llamada al metodo de anim para actualizar la imagen segun el tiempo
     * <code>Animacion</code>.
     *
     * @param tiempo es el tiempo <code>Int</code> del objeto Animacion.
     */
    public void actualiza(long tiempo) {
        anim.actualiza(tiempo);
    }

    /**
     * @return the exist
     */
    public boolean isExist() {
        return exist;
    }

    /**
     * @param exist the exist to set
     */
    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
