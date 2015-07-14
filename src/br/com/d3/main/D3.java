package br.com.d3.main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;


public class D3 {
	
	private static final Integer TIMEOUT_START = 4000;
	
	private static final Integer TIMEOUT = 100000000;
	
	private static Integer RESETGAME = 120000 + (int) (Math.random() * 1200000);
	
	private static final Integer INTERVAL = 100 ; // 10 FPS
	
	public static	FileWriter 		fstream = null;
	
	public static 	BufferedWriter 	out 	= null;
	
	public static 	Frame 			f 		= null; 
	
	private Robot 					robot 	= null;
	
	private boolean 				debug 	= false;
	
	private boolean 				colletct = false;
	
	private int   						posAtualPath = 0;
	private static String   			arquivoPath = "";
	private static ArrayList<Posicao>	posicaoList = new ArrayList<Posicao>();
	
	
	private	Color 	corAntigaWalk 	= null;
	private double 	tempoAntigaWalk = 0;
	
	private String action = "walk";
	private double 	tempoAntigAction = 0;
	
	private double mainTimer = 0;
	private int anguloWalk = 350;
	
	//Monsters
	ArrayList<Posicao> pontos = new ArrayList();
	ArrayList<Posicao> pontosPos = new ArrayList();
	private double timeMonsterSearch = 0;
	
	private double zerarJogo = System.currentTimeMillis();
	
	//attack
	private Posicao posAttack = null;
	
	static {
		try{
			fstream = new FileWriter("debugge.txt");
			out = new BufferedWriter(fstream);
			
			f = new Frame("Debugger");
			f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			
			if( !arquivoPath.isEmpty() ){
				FileReader fr = new FileReader( arquivoPath); 
				BufferedReader br = new BufferedReader(fr); 
				String s; 
				while((s = br.readLine()) != null) { 
					String[] p = s.split(",");
					Posicao pos = new Posicao( Integer.valueOf( p[0].trim() ), Integer.valueOf( p[1].trim() ), null);
					posicaoList.add( pos );
				} 
				fr.close(); 
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public D3( Robot robot, boolean debug ){
		this.robot = robot;
		this.debug = debug;
	}
	
	public static void main(String[] args) {

		try{
			
			D3 d3 = new D3( new Robot(), false );
		
			d3.mainTimer = System.currentTimeMillis();
			
			out.write("\n Inicio... espserando, reset em: " + RESETGAME );
			Thread.sleep( TIMEOUT_START );
			
			//main loop...
			do {
				out.write("\n " + new Date().toString()  + "loop..." );
				d3.debug();
				
//				int[] mp1 = movePos();
//				Color 	co 			= d3.robot.getPixelColor(245, 418);
//				out.write("\n verificaNoJogo Dist: " + " x: " + mp1[0] + " y: " + mp1[1] + " - " +  co.getRed() + " G: " + co.getGreen() + " B: " + co.getBlue() );

				if( d3.colletct == false ){
					if( !d3.debug )
						if( d3.verificaNoMenuResumeGame() ){
							d3.clickResumeScreen();
						}
					
					if( !d3.debug )
						if( !d3.verificaNoJogo() ){
							out.write("\n Travou...mais un teste " );
		
							d3.robot.mouseMove(100, 100);
							d3.robot.mouseClick();
							
							Thread.sleep( 2000 );
							
							if( !d3.verificaNoJogo() ){
								out.write("\n Verificou Travou... saiu do jogo.. desligando "  + new Date().toString() );
								out.close();
								System.exit(0);
							}
						}
					
					if( !d3.debug )
						if(  ( System.currentTimeMillis() - d3.zerarJogo ) > RESETGAME && posicaoList.isEmpty() ){
							out.write("\n Zerando..." );
							d3.initGame();
							out.write("\n Fim Zerando..." );
						}
					
					if( d3.verificaLifeDown() ) {
						out.write("\n Life Down..... usando vida " );
						d3.robot.keyPress( KeyEvent.VK_Q );
						d3.robot.keyRelease(KeyEvent.VK_Q  );
					}
					
					//So entra para procurar monstros se estiver andando, caso faca outra acao nao entra para nao parar essa acao mais importante
					if( d3.tempoAntigAction == 0 || ( (System.currentTimeMillis() - d3.tempoAntigAction ) > 6000 && d3.action.equals("walk") ) ){
						d3.tempoAntigAction = System.currentTimeMillis();
						d3.action = "searchMonster";
						d3.anguloWalk = (int) (Math.random() * 360);
						
						d3.robot.keyPress( KeyEvent.VK_3);
						d3.robot.keyRelease(KeyEvent.VK_3  );
					}
					
					out.write("\n acao " + d3.action );
					
					//Main
					if( d3.action.equals("walk") ){
						if( posicaoList.isEmpty() ){
							d3.andar();
						}else{
							d3.andarPath();
						}
							
					}
					
					if( d3.action.equals("attack") ){
						d3.attack();
						if( (System.currentTimeMillis() - d3.tempoAntigAction ) > 2000 ){
	//						d3.action = "searchMonster";
							d3.tempoAntigAction = System.currentTimeMillis();
							d3.action = "walk";
							d3.corAntigaWalk = null;
						}
					}
					
					if( d3.action.equals("searchMonster") || d3.action.equals("searchMonsterP2") ){
						
						if( d3.corAntigaWalk != null  ){
							double d = d3.colourDistance(d3.corAntigaWalk, d3.robot.getPixelColor(520, 540) );
							if(  d < 7 || d3.action.equals("searchMonsterP2") ){
								
								d3.action = "searchMonsterP2";
								
								out.write("\n Parado, pode veiricar atack " + d + " < 7" );
								Posicao p = d3.searchMonsterMouse();
								if( p != null ){
									out.write("\n Atacou INIT ");
									d3.initAttack( p.x, p.y );
								}else{
									d3.tempoAntigAction = System.currentTimeMillis();
									d3.action = "walk";
									d3.corAntigaWalk = null;
								}
							}
						}
						
						//so se estiver parado
						if( ( d3.corAntigaWalk == null || (System.currentTimeMillis() - d3.tempoAntigaWalk ) > 500 ) &&  (d3.action.equals("searchMonster") || d3.action.equals("searchMonsterP2") )  ){
							d3.corAntigaWalk = d3.robot.getPixelColor(520, 540);
							d3.tempoAntigaWalk = System.currentTimeMillis();
							out.write("\n Pegou cor " + d3.tempoAntigaWalk);
						}
						
					}
					
				}else{
					if( d3.verificaNoJogo() ){
						int[] mp = movePos();
						Posicao p = new Posicao(mp[0], mp[1], null);
						d3.posicaoList.add( p );
					}else{
						String s = "";
						for(Posicao po:d3.posicaoList){
							s +=  po.x + "," + po.y + " \n";
						}
//						System.out.println( s );
						System.exit(0);
					}
					
				}
					
				out.write("\n ACAO: " + d3.action );
				
				Thread.sleep( INTERVAL );
			} while ( (System.currentTimeMillis() - d3.mainTimer) < TIMEOUT );
			
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	private void initGame() throws Exception {
		this.zerarJogo = System.currentTimeMillis();

		//tele
		this.robot.keyPress( KeyEvent.VK_T );
		this.robot.keyRelease(KeyEvent.VK_T  );
		Thread.sleep( 11000 );

		//escape
		this.robot.keyPress( KeyEvent.VK_ESCAPE );
		this.robot.keyRelease(KeyEvent.VK_ESCAPE  );
		Thread.sleep( 1000 );
		
		//click leave game
		this.robot.mouseMove( 1000, 580);
		this.robot.mouseClick();
		Thread.sleep( 7000 );
		
		//click resume
		this.robot.mouseMove( 200, 415);
		this.robot.mouseClick();
		Thread.sleep( 11000 );
		
		if( !this.verificaNoJogo() ){
			Thread.sleep( 5000 );
		}
		
		this.anguloWalk = 350;
		this.tempoAntigAction = System.currentTimeMillis();
		this.action = "walk";
	}
	
	private void clickResumeScreen() throws Exception {
		//click leave game
		this.robot.mouseMove( 245, 418 );
		this.robot.mouseClick();
		Thread.sleep( 7000 );
		
		this.zerarJogo = System.currentTimeMillis();
		this.anguloWalk = 350;
		this.tempoAntigAction = System.currentTimeMillis();
		this.action = "walk";

	}
	
	private void initAttack( int x, int y) throws InterruptedException{
		//init attack
		this.robot.keyPress( KeyEvent.VK_1);
		this.robot.keyRelease(KeyEvent.VK_1  );
		
		Thread.sleep( 200 );
		this.robot.keyPress( KeyEvent.VK_2 );
		this.robot.keyRelease(KeyEvent.VK_2  );
		this.robot.mouseClick();
		
		Thread.sleep( 200 );
		this.robot.keyPress( KeyEvent.VK_3);
		this.robot.keyRelease(KeyEvent.VK_3  );
		
		Thread.sleep( 200 );
		this.robot.keyPress( KeyEvent.VK_4);
		this.robot.keyRelease(KeyEvent.VK_4  );
		
		this.tempoAntigAction = System.currentTimeMillis();
		this.action = "attack";
		this.posAttack = new Posicao(x, y, null);
		
		double vX = x - ( this.robot.getScreenWidth() / 2 );
		double vY = y - ( this.robot.getScreenWidth() / 2 );
		
		double ang = Math.atan2(vY, vX);
		
		if( ang < 0){
			ang += Math.toRadians( 360 );
		}
		
		this.anguloWalk = (int) ang;
	}
	
	/**
	 * Debug init.
	 */
	private void debug(){
		if( this.debug ){
			int[] m = this.movePos();
			
			f.removeAll();
			f.add(new Label("x: " + m[0] + "y: " + m[1] ));
			f.setSize(new Dimension(300, 200));
			f.setVisible(true);
		}
	}
	
	public Posicao searchMonsterMouse() throws Exception {
		
		int xfator = 140;
		int yfator = 140;
		
		pontos.clear();
		for(int i=0; i<(this.robot.getScreenWidth()/xfator); i++){
			int xPos = (i*xfator);
			for(int e=0; e<( (this.robot.getScreenHeight()-200)/yfator); e++){	
				int yPos = (e*yfator)+50;
				if( (     xPos >550 && xPos <1380 && yPos >200 && yPos <750   ) ){
					Thread.sleep( 20 );
					out.write("\n	ADD " + xPos + " e " + yPos  );
					Posicao pos = new Posicao(xPos, yPos, this.robot.getPixelColor( xPos, yPos ));
					
					this.robot.mouseMove( pos.x, pos.y);
					if( this.verificaMonstro() || this.verificaGold(pos.x, pos.y) ){
						out.write("\n	achouuu " + pos.x + " e " + pos.y );
						return pos;
					}
					
				}
			}
		}
		
		out.write("\n M2" + System.currentTimeMillis()  );
		
		return null;
		
	}

	public Posicao searchMonster() throws Exception {
		
		int xfator = 100;
		int yfator = 90;
		
		if( this.timeMonsterSearch == 0 ){
			pontos.clear();
			for(int i=0; i<(this.robot.getScreenWidth()/xfator); i++){
				int xPos = (i*xfator);
				for(int e=0; e<( (this.robot.getScreenHeight()-200)/yfator); e++){	
					int yPos = (e*yfator)+50;
					if( !(     xPos >850 && xPos <1080 && yPos >250 && yPos <650   ) ){
//						out.write("\n	ADD " + xPos + " e " + yPos  );
						Posicao pos = new Posicao(xPos, yPos, this.robot.getPixelColor( xPos, yPos ));
						pontos.add( pos );
					}
				}
			}
			out.write("\n M2" + System.currentTimeMillis()  );
			this.timeMonsterSearch = System.currentTimeMillis();
		}
		
		if( (System.currentTimeMillis() - this.timeMonsterSearch ) > 500 ){
			double ini = System.currentTimeMillis() ;
			pontosPos.clear(); 
			for(int i=0; i<(this.robot.getScreenWidth()/xfator); i++){
				int xPos = (i*xfator);
				for(int e=0; e<( (this.robot.getScreenHeight()-200) /yfator); e++){	
					int yPos = (e*yfator)+50;
					if( !(     xPos >850 && xPos <1080 && yPos >250 && yPos <650   ) ){
						Posicao pos = new Posicao(xPos, yPos, this.robot.getPixelColor( xPos, yPos ));
						pontosPos.add( pos );						
					}
				}
			}
			
			for(int i=0; i<pontos.size(); i++){
				Posicao p1 = pontos.get( i );
				Posicao p2 = pontosPos.get( i );
				if( p1 != null && p2 != null && p1.x == p2.x && p1.y == p2.y){ //&&    ( p1.x > 1080 || p1.x < 850 ) && ( p1.y > 650 || p1.y < 280 ) 
					double distColor = this.colourDistance( p1.cor, p2.cor);
					out.write("\n	SX " + p1.x + " My: " + p1.y + " DISt: " + distColor );
					if( distColor > 100){
						out.write("\n		ACHOOOO ");
						
						out.write("\n FIM: " + (System.currentTimeMillis()-ini) );
						
						pontos.clear();
						pontosPos.clear();
						
						this.timeMonsterSearch = 0;
						
						return p1;
					}
					
				}
			}
			out.write("\n FIM: " + (System.currentTimeMillis()-ini) );
			pontos.clear();
			pontosPos.clear();
			this.timeMonsterSearch = 0;
			
		}else{
			out.write("\n Fora tempo");
		}
		
		return null;
		
	}
	
	public void attack() throws Exception {
		if( this.debug == false){
			this.robot.mouseMove( posAttack.x, posAttack.y);
			this.robot.mousePress( InputEvent.BUTTON3_MASK );
			Thread.sleep( 6000 );
			this.robot.mouseRelease( InputEvent.BUTTON3_MASK );
		}
	}
	
	/**
	 * Andar Path do file.
	 * 
	 * @throws Exception
	 */
	public void andarPath() throws Exception {
		if( this.debug == false){
			
			if( posAtualPath >= posicaoList.size() ){
				out.write("\n Zerando Jogo... FIM PATH..." );
				posAtualPath = 0;
				this.initGame();
				out.write("\n Fim Zerando Jogo... FIM PATH..." );
			}else{
				Posicao  p = posicaoList.get( posAtualPath );
				out.write("\n Andando Path..." + posAtualPath + " x: " +  p.x );
				
				this.robot.mouseMove( p.x, p.y);
				
				if( this.verificaMonstro() ){
					this.initAttack( p.x, p.y );
				}else{
					this.robot.mouseClick();
					this.posAtualPath++;
				}
				
			}
		}
	}
	
	/**
	 * Andar.
	 * 
	 * @throws Exception
	 */
	public void andar() throws Exception {
		
		Double x = this.robot.getScreenWidth() / 2;
		Double y = this.robot.getScreenHeight() / 2;
		
		/*double vX = MouseInfo.getPointerInfo().getLocation().x - x;
		double vY = MouseInfo.getPointerInfo().getLocation().y - y;
		
		double ang = Math.atan2(vY, vX);
		
		if( ang < 0){
			ang += Math.toRadians( 360 );
		}*/
		
		int xNovo = (int) (x + ( 400 * Math.cos( Math.toRadians( anguloWalk ) ) ));
		int yNovo = (int) (y + ( 400 * Math.sin( Math.toRadians( anguloWalk )  ) ));
		
		out.write("\n Adar Mx: " + xNovo + " My: " + yNovo );
		
		Color corAtual = this.robot.getPixelColor(520, 540);
		
		if( corAntigaWalk != null && colourDistance(corAntigaWalk, corAtual) < 10 ){
			corAtual = this.robot.getPixelColor(520, 540);
			if( colourDistance(corAntigaWalk, corAtual) < 10 ){
				out.write(" -- Abaixa Mouse pq ta parado: " + colourDistance(corAntigaWalk, corAtual) );
				int vadd = 15 * M;
				anguloWalk += vadd;
				
				if( this.debug == false){
					this.robot.mouseMove( xNovo, yNovo);
					this.robot.mouseClick();
					if( this.verificaMonstro() ){
						this.initAttack( xNovo, yNovo );
					}
				}
				
				if( cont++ >= 3 ){
					this.anguloWalk = (int) (Math.random() * 360);
					cont = 0;
				}
				corAntigaWalk = this.robot.getPixelColor(520, 540);
			}else{
				M *= -1;
				out.write(" -- Mantem alterou valor: ");
				if( this.debug == false){
					this.robot.mouseMove( xNovo, yNovo);
					this.robot.mouseClick();
					if( this.verificaMonstro() ){
						this.initAttack( xNovo, yNovo );
					}
				}
				
				cont = 0;
			}
		}else{
			M *= -1;
			out.write(" -- Mantem alterou valor: ");
			if( this.debug == false){
				this.robot.mouseMove( xNovo, yNovo);
				this.robot.mouseClick();
				if( this.verificaMonstro() ){
					this.initAttack( xNovo, yNovo );
				}
			}
			
			cont = 0;
		}
		
		if( corAntigaWalk == null || (System.currentTimeMillis() - tempoAntigaWalk ) > 500 ){
			corAntigaWalk = this.robot.getPixelColor(520, 540);
			tempoAntigaWalk = System.currentTimeMillis();
			out.write("\n Atualizou Cor R " + corAntigaWalk.getRed() + " G: " + corAntigaWalk.getGreen() + " B: " + corAntigaWalk.getBlue() );
		}
		
		Thread.sleep(50);
	}
	
	private int cont = 1;
	private int M = 1;
	
	/**
	 * True se achar vermelho no topo, indicando monstro.
	 * 
	 * @return
	 */
	public boolean verificaNoJogo() throws Exception {
		Color 	co 			= this.robot.getPixelColor(606, 1049); // Posicao inicio barra vermelha TOPO
		double 	distColor 	= this.colourDistance(co, new Color(113, 47, 35) ); // diferenca de cor entre topo com cor Vermelho 
		
		out.write("\n verificaNoJogo Dist: " + distColor+ " -- " + co.getRed() + " G: " + co.getGreen() + " B: " + co.getBlue() );
		System.out.println( " verificaNoJogo Dist: " + distColor+ " -- " + co.getRed() + " G: " + co.getGreen() + " B: " + co.getBlue() );
		if( distColor < 200 ){
			
			return true;
		}
		
		System.out.println( "\n Saiu " + new Date().getDate() );
		
		return false;
	}
	
	/**
	 * True se achar vermelho no topo, indicando monstro.
	 * 
	 * @return
	 */
	public boolean verificaLifeDown() throws Exception {
		Color 	co 			= this.robot.getPixelColor(540, 1010); // Posicao inicio barra vermelha TOPO
		double 	distColor 	= this.colourDistance(co, new Color(140, 40, 18) ); // diferenca de cor entre topo com cor Vermelho 
		
		out.write("\n verificaLife Dist: " + distColor+ " -- " + co.getRed() + " G: " + co.getGreen() + " B: " + co.getBlue() );

		if( distColor < 120 ){
			Thread.sleep( 100 );
			co 			= this.robot.getPixelColor(535, 964); // Posicao inicio barra vermelha TOPO
			distColor 	= this.colourDistance(co, new Color(140, 40, 18) ); // diferenca de cor entre topo com cor Vermelho 
			
			out.write("\n verificaLife false ");
			return false; //esta vermelho retorna false pq life nao ta down
		}
		
		Thread.sleep( 200 );
		
		co 			= this.robot.getPixelColor(540, 1010); // Posicao inicio barra vermelha TOPO
		distColor 	= this.colourDistance(co, new Color(140, 40, 18) ); // diferenca de cor entre topo com cor Vermelho 
		
		if( distColor < 120 ){
			Thread.sleep( 100 );
			co 			= this.robot.getPixelColor(535, 964); // Posicao inicio barra vermelha TOPO
			distColor 	= this.colourDistance(co, new Color(140, 40, 18) ); // diferenca de cor entre topo com cor Vermelho 
			
			out.write("\n verificaLife false ");
			return false; //esta vermelho retorna false pq life nao ta down
		}
		
		out.write("\n verificaLife true ");
		
		return true;
	}
	
	/**
	 * True se achar vermelho no topo, indicando monstro.
	 * 
	 * @return
	 */
	public boolean verificaMonstro() throws Exception {
		Color 	co 			= this.robot.getPixelColor(872, 71); // Posicao inicio barra vermelha TOPO
		double 	distColor 	= this.colourDistance(co, new Color(128, 30, 0) ); // diferenca de cor entre topo com cor Vermelho 
		
		if( distColor < 40 ){
			return true;
		}
		
		return false;
	}
	
	/**
	 * True se achar vermelho no topo, indicando monstro.
	 * 
	 * @return
	 */
	public boolean verificaGold( int x, int y) throws Exception {
		Color 	co 			= this.robot.getPixelColor(x, y); // Posicao inicio barra vermelha TOPO
		double 	distColor 	= this.colourDistance(co, new Color(253, 254, 127) ); // diferenca de cor entre topo com cor Vermelho 
		
		if( distColor < 10 ){
			return true;
		}
		
		return false;
	}
	
	/**
	 * True se achar vermelho no topo, indicando monstro.
	 * 
	 * @return
	 */
	public boolean verificaNoMenuResumeGame( ) throws Exception {
		Color 	co 			= this.robot.getPixelColor(245, 418); // Posicao inicio barra vermelha TOPO
		double 	distColor 	= this.colourDistance(co, new Color(93, 19, 0) ); // diferenca de cor entre topo com cor Vermelho 
		
		out.write("\n verificaNoMenuResumeGame " + distColor );
		if( distColor < 5 ){
			out.write("\n verificaNoMenuResumeGame TRUE " + distColor );
			return true;
		}
		
		return false;
	}
	
	/**
	 * Retorna array posicao mouse.
	 * @return
	 */
	
	public static int[] movePos(){
		
		int[] m = { MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y};
		
		return m;
	}
	
	/**
	 * Diferenca entre cores.
	 * 
	 * @param e1
	 * @param e2
	 * @return
	 */
	public double colourDistance(Color e1, Color e2)
	{
	    long rmean = ( (long)e1.getRed() + (long)e2.getRed() ) / 2;
	    long r = (long)e1.getRed() - (long)e2.getRed();
	    long g = (long)e1.getGreen() - (long)e2.getGreen();
	    long b = (long)e1.getBlue() - (long)e2.getBlue();
	    
	    return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
	}
}