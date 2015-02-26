/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*



 Edgardo Acosta Leal 
 A00813103
 */
package breakingbad;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.io.IOException;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BreakingBad extends JFrame implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    // Se declaran las variables.
    private int direccion;    // Direccion del objeto
    private int incX;    // Incremento en x
    private int incY;    // Incremento en y
    private int vidas;    // vidas del jugador.

    private Image dbImage;    // Imagen a proyectar
    private Image gameover;    //Imagen a desplegar al acabar el juego.
    private Image Ivida; // imagenes de la vida
    private Image INovida; //imagen de perder una vida
    private Graphics dbg;	// Objeto grafico
    private SoundClip musicaInicio;    // Objeto SoundClip
    private SoundClip anota;    // Objeto SoundClip
    private SoundClip bomb;    //Objeto SoundClip 
    private SoundClip point;  //Objeto SoundClip
    private Bola bola;    // Objeto de la clase Balon
    private BarraG barra; //Objeto de la clase barra
    //Variables de control de tiempo de la animacion
    private long tiempoActual;
    private long tiempoInicial;
    private boolean pause;
    private boolean choca;
    private boolean presionaI;
    private boolean bolaMove;
    private boolean ladoIzq;
    private boolean ladoDer;
    private boolean activaSonido;
    private boolean presionaG;
    private boolean presionaC;
    private boolean presionaEnter; // Al presionar enter empieza el juego
    private int velocI;
    private boolean bNuevo;
    private boolean bPausa;
    private boolean bFin;

    private int score; // puntaje del jugador
    private String nombreArchivo;    //Nombre del archivo.
    private Vector vec;    // Objeto vector para agregar el puntaje.
    private String[] arr;  //array para obtener lo guardado
    private Image fondo;
    private Image inicial;
    private Image won;

    private boolean moveL;
    private boolean moveA;
    private LinkedList<Barra> BarraList;

    /**
     * Metodo <I>init</I> sobrescrito de la clase <code>Applet</code>.<P>
     */
    public BreakingBad() {

        this.setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        bPausa = false;
        vidas = 3;    // Le asignamos un valor inicial a las vidas
        barra = new BarraG(getWidth() / 5, getHeight() - 38);
        bola = new Bola(barra.getAncho() / 2 + getWidth() / 5, getHeight() - 63);
        BarraList = new LinkedList<Barra>();
        URL tURL = this.getClass().getResource("fondo.jpg");
        URL tURL2 = this.getClass().getResource("inicio.jpg");
        URL tURL3 = this.getClass().getResource("win.jpg");
        URL tURL4 = this.getClass().getResource("perdiste.jpg");
        URL URLvidas = this.getClass().getResource("vida.png");
        URL URLNovida = this.getClass().getResource("novida.png");
        fondo = Toolkit.getDefaultToolkit().getImage(tURL); //imagen de fondo al iniciar juego
        inicial = Toolkit.getDefaultToolkit().getImage(tURL2); // imagen de fondo antes de inicial el juego
        won = Toolkit.getDefaultToolkit().getImage(tURL3); //imagen cuando ganas
        gameover = Toolkit.getDefaultToolkit().getImage(tURL4); //imagen cuando pierdes
        Ivida = Toolkit.getDefaultToolkit().getImage(URLvidas);
        INovida = Toolkit.getDefaultToolkit().getImage(URLNovida);
        bNuevo = true;
        bFin = false;
        //cargar los enemegios (barra)
        int x;
        int y;

        int l = 0;
        int t2 = 0;
        int s = 0;
        for (int i = 0; i < 15; i++) {

            if (i < 5) {

                x = (int) ((getWidth() / 6) + l - 17);    //posision x es tres cuartos del applet
                y = 100;    //posision y es tres cuartos del applet
                BarraList.add(new Barra(x, y));
                l += (int) (getWidth() / 6);
            }

            if (i > 4 && i < 10) {

                x = (int) ((getWidth() / 6) + t2 - 17);    //posision x es tres cuartos del applet
                y = 200;    //posision y es tres cuartos del applet
                BarraList.add(new Barra(x, y));
                t2 += (int) (getWidth() / 6);
            }

            if (i > 9) {

                x = (int) ((getWidth() / 6) + s - 17);    //posision x es tres cuartos del applet
                y = 300;    //posision y es tres cuartos del applet
                BarraList.add(new Barra(x, y));
                s += (int) (getWidth() / 6);
            }
        }
        addKeyListener(this);

        moveL = true;
        moveA = false;
        nombreArchivo = "Puntaje.txt";
        vec = new Vector();

        presionaI = false;
        ladoIzq = false;
        ladoDer = false;
        presionaG = false;
        presionaC = false;
        presionaEnter = false;
        activaSonido = true; // El sonido esta activado al iniciar el juego

        //Se cargan los sonidos.
        bomb = new SoundClip("drop.wav");

        musicaInicio = new SoundClip("Videogame.wav");
        point = new SoundClip("Jump.wav");
        velocI = (int) (Math.random() * (112 - 85)) + 85; //85 a 112

        start();
    }

    /**
     * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>Applet</code>
     *
     */
    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se
     * incrementa la posicion en x o y dependiendo de la direccion, finalmente
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     *
     */
    public void run() {
        try {
            grabaArchivo1();
        } catch (IOException ex) {
            Logger.getLogger(BreakingBad.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!presionaEnter) {
            musicaInicio.play();
        }
        tiempoActual = System.currentTimeMillis();

        while (bNuevo) { // entra hasta que el score es 30
            actualiza();
            checaColision();
            repaint();

            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }

    }

    /**
     * Metodo usado para actualizar la posicion de objetos elefante y raton.
     *
     */
    public void actualiza() {
        //Dependiendo de la direccion del elefante es hacia donde se mueve.
        if (!bPausa && !presionaI && vidas > 0) {

            if (bolaMove) {
                switch (direccion) {
                    case 3: {
                        if (!ladoIzq && bolaMove) {
                            barra.setPosX(barra.getPosX() - 10);
                            break;    //se mueve hacia izquierda
                        }
                    }
                    case 4: {
                        if (!ladoDer && bolaMove) {
                            barra.setPosX(barra.getPosX() + 10);
                            break;    //se mueve hacia derecha
                        }
                    }
                }
                // Checa que la barra no se salga del frame
                if (barra.getAncho() + barra.getPosX() >= getWidth()) {
                    ladoDer = true;
                } else {
                    ladoDer = false;
                }
                if (barra.getPosX() <= 0) {
                    ladoIzq = true;
                } else {
                    ladoIzq = false;
                }

                if (moveL) {
                    bola.setPosX(bola.getPosX() + 6);

                } else {
                    bola.setPosX(bola.getPosX() - 6);
                }
                if (moveA) {
                    bola.setPosY(bola.getPosY() + 6);
                } else {
                    bola.setPosY(bola.getPosY() - 6);
                }
                //Guarda el tiempo actual

                long tiempoTranscurrido
                        = System.currentTimeMillis() - tiempoActual;
                tiempoActual = (tiempoActual + tiempoTranscurrido);

                bola.actualiza(tiempoTranscurrido);
                barra.actualiza(tiempoTranscurrido);
                for (Barra a : BarraList) {
                    a.actualiza(tiempoTranscurrido);
                }

            }

        }

    }

    /**
     * Metodo usado para checar las colisiones del objeto elefante y raton con
     * las orillas del <code>Applet</code>.
     */
    public void checaColision() {

        if (bola.getPosY() > getHeight()) {
            bolaMove = false;
            moveA = false;
            moveL = true;

            // la bola se posiciona encima de la barra
            bola.setPosX(barra.getPosX() + barra.getAncho() / 2);
            bola.setPosY(barra.getPosY() - 25);

            if (activaSonido) {
                bomb.play();
            }
            vidas--;// se resta una vida cuando el bola cae
            barra = new BarraG(getWidth() / 5, getHeight() - 38);
            bola = new Bola(barra.getAncho() / 2 + getWidth() / 5, getHeight() - 63);

        }
        //checa si la bola esta dentro del applet
        if (bola.getPosY() < 20) {
            moveA = true;

        }
        if (bola.getPosX() > getWidth() - 20) {
            moveL = false;
        }
        if (bola.getPosX() < 0) {
            moveL = true;
        }

        //checa que la barra este dentro del applet
        if (barra.getPosX() < 0) {
            direccion = 0;
        }
        if (barra.getPosX() > getWidth() - barra.getPerimetro().getWidth()) {
            direccion = 0;
        }
        for (Barra a : BarraList) {
            if (bola.intersecta1(a) && a.isExist()) {
                if (moveA) {
                    moveA = false;
                } else {
                    moveA = true;
                }
                a.setExist(false);
                score = score + 2;
                if (activaSonido) {
                    point.play();
                }
            }
        }
        if (bola.intersecta(barra)) {
            moveA = false;

//            
        }

    }

    public void paint(Graphics g) {
        // Inicializan el DoubleBuffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            bPausa = !bPausa;

        } else {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono flecha izquierda
                direccion = 3;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
                direccion = 4;
            }
        }

        // Salen instrucciones del juego
        if (e.getKeyCode() == KeyEvent.VK_I) {
            presionaI = !presionaI;
        }

        // Quita el sonido
        if (e.getKeyCode() == KeyEvent.VK_S && !presionaI) {
            activaSonido = !activaSonido;

        }

        // Tecla para guardar archivo
        if (e.getKeyCode() == KeyEvent.VK_G && !presionaI) {
            vec.add(new Puntaje(score));    //Agrega el contenido del nuevo puntaje al vector.
            try {
                grabaArchivo();    //Graba el vector en el archivo.
            } catch (IOException ex) {
                Logger.getLogger(BreakingBad.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        // Tecla para cargar archivo
        if (e.getKeyCode() == KeyEvent.VK_C) {

            try {
                leeArchivo();    //lee el contenido del archivo 
            } catch (IOException ex) {
                Logger.getLogger(BreakingBad.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            bNuevo = true;
            tiempoActual = System.currentTimeMillis();
            try {
                leeArchivo1();
            } catch (IOException ex) {
                Logger.getLogger(BreakingBad.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Tecla para iniciar el juego
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            presionaEnter = true;
        }
        // Tecla para que la bola se mueva
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            bolaMove = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            bFin = true;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        direccion = 0;
    }

    public void paint1(Graphics g) {
        if (!presionaEnter) {
            g.drawImage(inicial, 0, 0, 1300, 700, this);
            g.setColor(Color.white);
            g.setFont(new Font("defalut", Font.BOLD, 46));
            g.drawString("Presiona ENTER para iniciar el juego", 300, 300);
            g.setColor(Color.white);
            g.setFont(new Font("defalut", Font.BOLD, 46));
            g.drawString("Presiona I Durante el juego ", 300, 400);
            g.drawString("para las instruciones", 350, 450);

        } else {
            //          g.drawImage(fondo.getImage(), 0, 0,1300,700, this);
            g.setFont(new Font("default", Font.BOLD, 16));

            g.setColor(Color.white);
            if (vidas > 0) {
                if (bola != null && BarraList != null) {
                    g.drawImage(fondo, 0, 0, 1300, 700, this);
                    if (bPausa) {
                        g.setFont(new Font("default", Font.BOLD, 50));
                        g.setColor(Color.red);
                        g.drawString("PAUSA", getWidth() / 2 - 50, 500);
                    }
                    g.setFont(new Font("default", Font.BOLD, 16));

                    g.setColor(Color.white);
                    if (vidas == 3) {
                        g.drawImage(Ivida, getWidth() / 2, 60, 50, 50, this);
                        g.drawImage(Ivida, getWidth() / 2 - 100, 60, 50, 50, this);
                        g.drawImage(Ivida, getWidth() / 2 + 100, 60, 50, 50, this);

                    }
                    if (vidas == 2) {
                        g.drawImage(Ivida, getWidth() / 2, 60, 50, 50, this);
                        g.drawImage(INovida, getWidth() / 2 - 100, 60, 50, 50, this);
                        g.drawImage(Ivida, getWidth() / 2 + 100, 60, 50, 50, this);

                    }
                    if (vidas == 1) {
                        g.drawImage(INovida, getWidth() / 2, 60, 50, 50, this);
                        g.drawImage(INovida, getWidth() / 2 - 100, 60, 50, 50, this);
                        g.drawImage(Ivida, getWidth() / 2 + 100, 60, 50, 50, this);

                    }
                    if (vidas == 0) {
                        g.drawImage(INovida, getWidth() / 2, 60, 50, 50, this);
                        g.drawImage(INovida, getWidth() / 2 - 100, 60, 50, 50, this);
                        g.drawImage(INovida, getWidth() / 2 + 100, 60, 50, 50, this);

                    }

                    if (activaSonido) {
                        g.drawString("Sonido: ON", 1200, 60);
                    } else {
                        g.drawString("Sonido: OF", 1200, 60);
                    }

                    //Dibuja la imagen en la posicion actualizada
                    g.drawImage(bola.getImagenI(), bola.getPosX(), bola.getPosY(), this);
                    //Dibuja la imagen en la posicion actualizada
                    g.drawImage(barra.getImagenI(), barra.getPosX(), barra.getPosY(), this);
                    for (Barra a : BarraList) {
                        if (a.isExist()) {
                            g.drawImage(a.getImagenI(), a.getPosX(), a.getPosY(), this);
                        }
                    }
                //Muestra las vidas
                    //g.drawString("Vidas: " + vidas, getWidth() / 2 - 10, 80);
                    //Muestra el puntaje
                    g.drawString("Score: " + score, bola.getAncho(), 80);

                    if (presionaI) {

                        g.drawString("Instrucciones:", getWidth() / 4 + getWidth() / 8, 200);

                        g.drawString("Mueve la camioneta con las flechas del teclado", getWidth() / 4 + getWidth() / 8, 220);
                        g.drawString("para que rebote la bola y destruya los objetos", getWidth() / 4 + getWidth() / 8, 240);
                        g.drawString("azules (metanfetaminas).", getWidth() / 4 + getWidth() / 8, 260);

                        g.drawString("Teclas: ", getWidth() / 4 + getWidth() / 8, 300);
                        g.drawString("Flecha izquierda - se mueve a la izquierda", getWidth() / 4 + getWidth() / 8, 320);
                        g.drawString("Flecha derecha - se mueve a la derecha", getWidth() / 4 + getWidth() / 8, 340);
                        g.drawString("I - muestra/oculta instrucciones", getWidth() / 4 + getWidth() / 8, 360);
                        g.drawString("G - guarda el juego", getWidth() / 4 + getWidth() / 8, 380);
                        g.drawString("C - carga el juego", getWidth() / 4 + getWidth() / 8, 400);
                        g.drawString("P - pausa el juego", getWidth() / 4 + getWidth() / 8, 420);
                        g.drawString("S - activa/desactiva el sonido del juego", getWidth() / 4 + getWidth() / 8, 440);
                        g.drawString("A - Reinicial el juego tras perder", getWidth() / 4 + getWidth() / 8, 460);

                    }
                } else {
                    //Da un mensaje mientras se carga el dibujo	
                    g.drawString("No se cargo la imagen..", 20, 20);
                }
            } else {
                g.drawImage(gameover, 0, 0, 1300, 700, this);
                g.setFont(new Font("defalut", Font.BOLD, 22));
                g.setColor(Color.white);
                g.drawString("GAME OVER", 500, 200);
                g.drawString("Score: " + score, 650, 510);
                g.setColor(Color.white);
                g.setFont(new Font("defalut", Font.BOLD, 46));
                g.drawString("Si deseas juagr de nuevo presiona A", 300, 300);

            }

            if (score == 30) {
                g.setFont(new Font("defalut", Font.BOLD, 30));
                g.setColor(Color.yellow);
                g.drawImage(won, 0, 0, 1300, 700, this);
                g.drawString("Ganaste!!!", getWidth() / 2, 150);
                g.drawString("Score: " + score, getWidth() / 2, 200);
                g.drawString("Para jugar de nuevo presiona A", getWidth() / 2 - 200, 400);
                vidas = 0;
            }

        }

    }

    public void leeArchivo() throws IOException {
        BufferedReader fileIn;
        try {
            presionaC = false;
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
            String dato = fileIn.readLine();
            setArr(dato.split(","));
            score = (Integer.parseInt(arr[0]));
            vidas = (Integer.parseInt(arr[1]));
            bola.setPosX(Integer.parseInt(arr[2]));
            bola.setPosY(Integer.parseInt(arr[3]));
            barra.setPosX(Integer.parseInt(arr[4]));
            barra.setPosY(Integer.parseInt(arr[5]));
            bolaMove = true;

            velocI = (Integer.parseInt(arr[7]));
            activaSonido = (Boolean.parseBoolean(arr[8]));
            fileIn.close();
            actualiza();
        } catch (IOException ioe) {
            System.out.println("Se arrojo una excepcion " + ioe.toString());
        }
    }

    public void grabaArchivo() throws IOException {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));

            fileOut.println("" + score + "," + vidas + "," + bola.getPosX()
                    + "," + bola.getPosY() + "," + barra.getPosX() + ","
                    + barra.getPosY() + "," + bolaMove + "," + velocI + "," + activaSonido);
            for (Barra a : BarraList) {
                fileOut.println("" + a.getPosX() + "," + a.getPosY());
            }
            fileOut.close();
        } catch (IOException ioe) {
            System.out.println("Se arrojo una excepcion " + ioe.toString());
        }
    }

    public void grabaArchivo1() throws IOException {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
            barra = new BarraG(getWidth() / 5, getHeight() - 38);
            bola = new Bola(barra.getAncho() / 2 + getWidth() / 5, getHeight() - 63);
            bolaMove = false;
            fileOut.println("" + score + "," + vidas + "," + bola.getPosX()
                    + "," + bola.getPosY() + "," + barra.getPosX() + ","
                    + barra.getPosY() + "," + bolaMove + "," + velocI + "," + activaSonido);
            for (Barra a : BarraList) {
                fileOut.println("" + a.getPosX() + "," + a.getPosY());
            }
            fileOut.close();
        } catch (IOException ioe) {
            System.out.println("Se arrojo una excepcion " + ioe.toString());
        }
    }

    public void leeArchivo1() throws IOException {
        BufferedReader fileIn;
        try {
            presionaC = false;
            fileIn = new BufferedReader(new FileReader(nombreArchivo));
            String dato = fileIn.readLine();
            setArr(dato.split(","));
            score = (Integer.parseInt(arr[0]));
            vidas = (Integer.parseInt(arr[1]));
            bola.setPosX(Integer.parseInt(arr[2]));
            bola.setPosX(Integer.parseInt(arr[3]));
            barra.setPosX(Integer.parseInt(arr[4]));
            barra.setPosY(Integer.parseInt(arr[5]));
            bolaMove = false;
            velocI = (Integer.parseInt(arr[7]));
            activaSonido = (Boolean.parseBoolean(arr[8]));
            BarraList = new LinkedList();
            for (int iI = 0; iI < 15; iI++) {
                dato = fileIn.readLine();
                setArr(dato.split(","));
                int posx = (Integer.parseInt(arr[0]));
                int posy = (Integer.parseInt(arr[1]));
                BarraList.add(new Barra(posx, posy));
            }
            fileIn.close();
            actualiza();
        } catch (IOException ioe) {
            System.out.println("Se arrojo una excepcion " + ioe.toString());
        }
    }

    /**
     * @return the arr
     */
    public String[] getArr() {
        return arr;
    }

    /**
     * @param arr the arr to set
     */
    public void setArr(String[] arr) {
        this.arr = arr;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        BreakingBad Juego = new BreakingBad();
        Juego.setVisible(true);
    }
}
