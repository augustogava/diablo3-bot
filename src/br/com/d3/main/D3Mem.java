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

import br.com.d3.pro.PropriedadesActor;

import com.sun.jna.Pointer;


public class D3Mem {
	
	private static final Integer TIMEOUT_START = 500;
	
	private static final Integer TIMEOUT = 5000;
	
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
	public Pointer myProcess;
	
	public int ofs_ObjectManager 				= 0x15A0BEC;
	public int ofs__ObjmanagerActorOffsetA 		= 0x8b0;
	public int ofs__ObjmanagerActorCount 		= 0x108;
	public int ofs__ObjmanagerActorOffsetB 		= 0x148;
	public int ofs__ObjmanagerActorLinkToCTM 	= 0x380;
	public int _ObjmanagerStrucSize 			= 0x428;
	
	public int _itrObjectManagerA;
	public int _itrObjectManagerB;	
	public int _itrObjectManagerCount;
	public int _itrObjectManagerC;
	public int _itrObjectManagerD;
	public int _itrObjectManagerE;
	
	public int ofs_Interact 					= 0x15A0BD4;
	public int ofs__InteractOffsetA 			= 0xA8;
	public int ofs__InteractOffsetB 			= 0x58;
	public int ofs__InteractOffsetUNK1 			= 0x7F20;
	public int ofs__InteractOffsetUNK2 			= 0x7F44;
	public int ofs__InteractOffsetUNK3 			= 0x7F7C;
	public int ofs__InteractOffsetUNK4 			= 0x7F80;
	public int ofs__InteractOffsetMousestate 	= 0x7F84; 
	public int ofs__InteractOffsetGUID 			= 0x7F88;
	
	
	public int _itrInteractA;
	public int _itrInteractB;
	public int _itrInteractC;
	public int _itrInteractD;
	public int _itrInteractE;

	public int ClickToMoveMain;
	public int ClickToMoveRotation;
	public int ClickToMoveCurX;
	public int ClickToMoveCurY;
	public int ClickToMoveCurZ;
	public int ClickToMoveToX;
	public int ClickToMoveToY;
	public int ClickToMoveToZ;
	public int ClickToMoveToggle;
	public int ClickToMoveFix;
	
	static {
		try{
			fstream = new FileWriter("debug.txt");
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
	
	public D3Mem( Robot robot, boolean debug ){
		this.robot = robot;
		this.debug = debug;
	}
		
	public static void main(String[] args) {

		try{
			
			D3Mem d3 = new D3Mem( new Robot(), false );
			d3.mainTimer = System.currentTimeMillis();
			
			out.write("\n Inicio... espserando, reset em: " + RESETGAME );
			Thread.sleep( TIMEOUT_START );
			
			
			
			int a = 0x15A0BEC;
			d3.myProcess = ReadProcessMemory.FindMyProcess("Diablo III");
			d3.offsetlist();
			
			Float[] pos = d3.getCurrentPos();
			System.out.println(" x: " + pos[0] + " y: " + pos[1] + " z: " + pos[2]);
			
			objetor= d3.getObjList();
//			d3.MoveToPos( 100, 1939, 4111);
			
//			 x: 100.0 y: 2101.4707 z: 4108.183
//			 Achou no endereco: f8d50a0 guiId: 2008809472 name: Wizard_Male-5 x: 2101.6611 y: 4108.0615 z: 4.4208045 data: 8 data2: 29944 data3: 2081 DIST: 0
			
			int ang = pos[y]
			
			Thread.sleep( 2000 );
			System.exit(0);
			
			
			//main loop...
			do {
				out.write("\n " + new Date().toString()  + "loop..." );
				d3.debug();
				
//				int[] mp1 = movePos();
//				Color 	co 			= d3.robot.getPixelColor(245, 418);
//				out.write("\n verificaNoJogo Dist: " + " x: " + mp1[0] + " y: " + mp1[1] + " - " +  co.getRed() + " G: " + co.getGreen() + " B: " + co.getBlue() );

				
				out.write("\n ACAO: " + d3.action );
				
				Thread.sleep( INTERVAL );
			} while ( (System.currentTimeMillis() - d3.mainTimer) < TIMEOUT );
			
			out.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public void offsetlist(){
		
		_itrObjectManagerA  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, ofs_ObjectManager, 4, 2);
		_itrObjectManagerB  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrObjectManagerA+ofs__ObjmanagerActorOffsetA, 4, 2);
		_itrObjectManagerCount  = _itrObjectManagerB+ofs__ObjmanagerActorCount;
		_itrObjectManagerC  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrObjectManagerB+ofs__ObjmanagerActorOffsetB, 4, 2);
		_itrObjectManagerD  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrObjectManagerC, 4, 2);
		_itrObjectManagerE  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrObjectManagerD, 4, 2);
	
		_itrInteractA  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, ofs_Interact,4, 2);
		_itrInteractB  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrInteractA, 4, 2);	
		_itrInteractC  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrInteractB, 4, 2);
		_itrInteractD  = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrInteractC+ofs__InteractOffsetA, 4, 2);	
		_itrInteractE  = _itrInteractD+ofs__InteractOffsetB;
	
		int FixSpeed = 0x20;
		int ToggleMove = 0x34;
		int MoveToXoffset = 0x3c;
		int MoveToYoffset = 0x40;
		int MoveToZoffset = 0x44;
		int CurrentX = 0xA4;
		int CurrentY = 0xA8;
		int CurrentZ = 0xAc;
		int RotationOffset = 0x170;
		
		this.locateMyToon();
		
		ClickToMoveMain = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _Myoffset + ofs__ObjmanagerActorLinkToCTM, 4, 2);
		ClickToMoveRotation = ClickToMoveMain + RotationOffset;
		ClickToMoveCurX = ClickToMoveMain + CurrentX;
		ClickToMoveCurY = ClickToMoveMain + CurrentY;
		ClickToMoveCurZ = ClickToMoveMain + CurrentZ;
		ClickToMoveToX = ClickToMoveMain + MoveToXoffset;
		ClickToMoveToY = ClickToMoveMain + MoveToYoffset;
		ClickToMoveToZ = ClickToMoveMain + MoveToZoffset;
		ClickToMoveToggle = ClickToMoveMain + ToggleMove;
		ClickToMoveFix= ClickToMoveMain + FixSpeed;
	}
	
	public int _Myoffset = 0;
	
	public void locateMyToon(){
		int _CurOffset = _itrObjectManagerD;
		int qtd =  (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _itrObjectManagerCount, 4, 2);
		System.out.println("QTD " + qtd);
		
		for( int i=0; i< qtd; i++ ){ 
			int id =  (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x4, 4, 2);
			System.out.println("id " + id);
			String name =  (String) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x8, 80, 6);
			if( id == 0x77BC0000){
				_Myoffset = _CurOffset;
				System.out.println("My toon located at: " + Integer.toHexString( _Myoffset ) + ", GUID: " + Integer.toHexString( id ) + ", NAME: " + name );
				return;
			}
			_CurOffset = _CurOffset + _ObjmanagerStrucSize;
		}
		
	}
	
	public Float[] getCurrentPos(){
		Float[] retorno = new Float[3];
		
		retorno[0] = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, ClickToMoveCurX, 4, 4);
		retorno[1] = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, ClickToMoveCurY, 4, 4);
		retorno[2] = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, ClickToMoveCurZ, 4, 4);
		
		return retorno;
	}
	
	public ArrayList<PropriedadesActor> getObjList(){
		ArrayList<PropriedadesActor> listaActor = new ArrayList<PropriedadesActor>();
		
		int _CurOffset = this._itrObjectManagerD;
		int count = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, this._itrObjectManagerCount, 4, 2);
		
		for(int i=0; i<count; i++){
			int guidid = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x4, 4, 2);
			String name = (String) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x8, 60, 6);
			float posx = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0xB0, 4, 4);
			float posy = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0xB4, 4, 4);
			float posz = (Float) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0xB8, 4, 4);	
			int data1 = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x1FC, 4, 2);
			int data2 = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x1Cc, 4, 2);		
			int data3 = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x1C0, 4, 2);		
//			int inteli = (Integer) ReadProcessMemory.ReadMyProcessMemory( myProcess, _CurOffset+0x00C, 4, 2);	
			
			if( guidid != -1){
				Float[] currentPos = this.getCurrentPos();
				int zd = (int) (posz-currentPos[0]);
				int xd = (int) (posx-currentPos[1]);
				int yd = (int) (posy-currentPos[2]);
				int distance = (int) Math.sqrt( xd*xd + yd*yd );
				
				PropriedadesActor propriedadesActor = new PropriedadesActor();
				propriedadesActor.setGuiId( guidid );
				propriedadesActor.setName( name );
				propriedadesActor.setPosx( posx );
				propriedadesActor.setPosy( posy );
				propriedadesActor.setPosz( posz );
				propriedadesActor.setData1( data1 );
				propriedadesActor.setData2( data2 );
				propriedadesActor.setData3( data3 );
				propriedadesActor.setDistance( distance );
				
				System.out.println("Achou no endereco: " + Integer.toHexString( _CurOffset ) + " guiId: " + guidid + " name: " + name + " x: " + posx + " y: " + posy + " z: " + posz + " data: " + data1 + " data2: " + data2 + " data3: " + data3 + " DIST: " + distance );
				listaActor.add( propriedadesActor );
			}
			
			_CurOffset = _CurOffset + this._ObjmanagerStrucSize;
		}
		
		return listaActor;
	}
	
	public void MoveToPos( int x, int y, int z ){
		
		ReadProcessMemory.WriteMyProcessMemory( myProcess, ClickToMoveToX , String.valueOf(x), 4 );
		ReadProcessMemory.WriteMyProcessMemory( myProcess, ClickToMoveToY , String.valueOf(y), 4 );
		ReadProcessMemory.WriteMyProcessMemory( myProcess, ClickToMoveToZ , String.valueOf(z), 4 );
		ReadProcessMemory.WriteMyProcessMemory( myProcess, ClickToMoveToggle , String.valueOf(1), 2 );
		ReadProcessMemory.WriteMyProcessMemory( myProcess, ClickToMoveFix , String.valueOf( 69736 ), 2 );

	}	
	
	public static byte[] intToByteArray(int value) {
	    byte[] b = new byte[4];
	    for (int i = 0; i < 4; i++) {
	        int offset = (b.length - 1 - i) * 8;
	        b[i] = (byte) ((value >>> offset) & 0xFF);
	    }
	    return b;
	}
	
	public static int[] intToArray(int value) {
		int[] b = new int[1];
	   
		b[0] = value;
			
	    return b;
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
				
				/*if( this.verificaMonstro() ){
					this.initAttack( p.x, p.y );
				}else{
					this.robot.mouseClick();
					this.posAtualPath++;
				}*/
				
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
//					if( this.verificaMonstro() ){
//						this.initAttack( xNovo, yNovo );
//					}
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
//					if( this.verificaMonstro() ){
//						this.initAttack( xNovo, yNovo );
//					}
				}
				
				cont = 0;
			}
		}else{
			M *= -1;
			out.write(" -- Mantem alterou valor: ");
			if( this.debug == false){
				this.robot.mouseMove( xNovo, yNovo);
				this.robot.mouseClick();
//				if( this.verificaMonstro() ){
//					this.initAttack( xNovo, yNovo );
//				}
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