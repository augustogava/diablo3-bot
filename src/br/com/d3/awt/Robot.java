package br.com.d3.main;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Robot extends java.awt.Robot {

	private static 	Map<String, int[]> 		KEYS_MAP;
	private 		Map 		processes;
	private 		Long 		timeout;
	private 		Color 		pixelColor;
	public 	static final String	DEFAULT_DELIMITER 		= ",";

	static {
		KEYS_MAP = new LinkedHashMap<String, int[]>();
		KEYS_MAP.put("0", new int[] { 48 });
		KEYS_MAP.put("1", new int[] { 49 });
		KEYS_MAP.put("2", new int[] { 50 });
		KEYS_MAP.put("3", new int[] { 51 });
		KEYS_MAP.put("4", new int[] { 52 });
		KEYS_MAP.put("5", new int[] { 53 });
		KEYS_MAP.put("6", new int[] { 54 });
		KEYS_MAP.put("7", new int[] { 55 });
		KEYS_MAP.put("8", new int[] { 56 });
		KEYS_MAP.put("9", new int[] { 57 });
		KEYS_MAP.put("a", new int[] { 65 });
		KEYS_MAP.put("\341", new int[] { 65 });
		KEYS_MAP.put("\343", new int[] { 65 });
		KEYS_MAP.put("\342", new int[] { 65 });
		KEYS_MAP.put("\340", new int[] { 65 });
		KEYS_MAP.put("b", new int[] { 66 });
		KEYS_MAP.put("c", new int[] { 67 });
		KEYS_MAP.put("\347", new int[] { 67 });
		KEYS_MAP.put("d", new int[] { 68 });
		KEYS_MAP.put("e", new int[] { 69 });
		KEYS_MAP.put("\351", new int[] { 69 });
		KEYS_MAP.put("\352", new int[] { 69 });
		KEYS_MAP.put("f", new int[] { 70 });
		KEYS_MAP.put("g", new int[] { 71 });
		KEYS_MAP.put("h", new int[] { 72 });
		KEYS_MAP.put("i", new int[] { 73 });
		KEYS_MAP.put("\355", new int[] { 73 });
		KEYS_MAP.put("j", new int[] { 74 });
		KEYS_MAP.put("k", new int[] { 75 });
		KEYS_MAP.put("l", new int[] { 76 });
		KEYS_MAP.put("m", new int[] { 77 });
		KEYS_MAP.put("n", new int[] { 78 });
		KEYS_MAP.put("o", new int[] { 79 });
		KEYS_MAP.put("\363", new int[] { 79 });
		KEYS_MAP.put("\364", new int[] { 79 });
		KEYS_MAP.put("\365", new int[] { 79 });
		KEYS_MAP.put("p", new int[] { 80 });
		KEYS_MAP.put("q", new int[] { 81 });
		KEYS_MAP.put("r", new int[] { 82 });
		KEYS_MAP.put("s", new int[] { 83 });
		KEYS_MAP.put("t", new int[] { 84 });
		KEYS_MAP.put("u", new int[] { 85 });
		KEYS_MAP.put("\372", new int[] { 85 });
		KEYS_MAP.put("\374", new int[] { 85 });
		KEYS_MAP.put("v", new int[] { 86 });
		KEYS_MAP.put("x", new int[] { 88 });
		KEYS_MAP.put("y", new int[] { 89 });
		KEYS_MAP.put("z", new int[] { 90 });
		KEYS_MAP.put("w", new int[] { 87 });
		KEYS_MAP.put("A", new int[] { 16, 65 });
		KEYS_MAP.put("\301", new int[] { 16, 65 });
		KEYS_MAP.put("\302", new int[] { 16, 65 });
		KEYS_MAP.put("\303", new int[] { 16, 65 });
		KEYS_MAP.put("\300", new int[] { 16, 65 });
		KEYS_MAP.put("B", new int[] { 16, 66 });
		KEYS_MAP.put("C", new int[] { 16, 67 });
		KEYS_MAP.put("\307", new int[] { 16, 67 });
		KEYS_MAP.put("D", new int[] { 16, 68 });
		KEYS_MAP.put("E", new int[] { 16, 69 });
		KEYS_MAP.put("\311", new int[] { 16, 69 });
		KEYS_MAP.put("\312", new int[] { 16, 69 });
		KEYS_MAP.put("F", new int[] { 16, 70 });
		KEYS_MAP.put("G", new int[] { 16, 71 });
		KEYS_MAP.put("H", new int[] { 16, 72 });
		KEYS_MAP.put("I", new int[] { 16, 73 });
		KEYS_MAP.put("\315", new int[] { 16, 73 });
		KEYS_MAP.put("J", new int[] { 16, 74 });
		KEYS_MAP.put("K", new int[] { 16, 75 });
		KEYS_MAP.put("L", new int[] { 16, 76 });
		KEYS_MAP.put("M", new int[] { 16, 77 });
		KEYS_MAP.put("N", new int[] { 16, 78 });
		KEYS_MAP.put("O", new int[] { 16, 79 });
		KEYS_MAP.put("\323", new int[] { 16, 79 });
		KEYS_MAP.put("\324", new int[] { 16, 79 });
		KEYS_MAP.put("\325", new int[] { 16, 79 });
		KEYS_MAP.put("P", new int[] { 16, 80 });
		KEYS_MAP.put("Q", new int[] { 16, 81 });
		KEYS_MAP.put("R", new int[] { 16, 82 });
		KEYS_MAP.put("S", new int[] { 16, 83 });
		KEYS_MAP.put("T", new int[] { 16, 84 });
		KEYS_MAP.put("U", new int[] { 16, 85 });
		KEYS_MAP.put("\332", new int[] { 16, 85 });
		KEYS_MAP.put("\334", new int[] { 16, 85 });
		KEYS_MAP.put("V", new int[] { 16, 86 });
		KEYS_MAP.put("X", new int[] { 16, 88 });
		KEYS_MAP.put("Y", new int[] { 16, 89 });
		KEYS_MAP.put("Z", new int[] { 16, 90 });
		KEYS_MAP.put("W", new int[] { 16, 87 });
		KEYS_MAP.put("!", null);
		KEYS_MAP.put("?", null);
		KEYS_MAP.put("+", new int[] { 107 });
		KEYS_MAP.put("-", new int[] { 109 });
		KEYS_MAP.put("*", new int[] { 106 });
		KEYS_MAP.put("/", new int[] { 111 });
		KEYS_MAP.put("=", new int[] { 61 });
		KEYS_MAP.put(".", new int[] { 46 });
		KEYS_MAP.put("_", new int[] { 523 }); 
		KEYS_MAP.put(":", new int[] { 513 });
		KEYS_MAP.put(",", new int[] { 44 });
		KEYS_MAP.put(";", new int[] { 59 });
		KEYS_MAP.put("\\", new int[] { 92 });
		KEYS_MAP.put("/", new int[] { 47 });
		KEYS_MAP.put(" ", new int[] { 32 });
		KEYS_MAP.put("$", new int[] { 515 });
		KEYS_MAP.put("#", new int[] { 520 });
		KEYS_MAP.put("[", new int[] { 91 });
		KEYS_MAP.put("]", new int[] { 93 });
		KEYS_MAP.put("{", new int[] { 161 });
		KEYS_MAP.put("}", new int[] { 162 });
		KEYS_MAP.put("(", new int[] { 519 });
		KEYS_MAP.put(")", new int[] { 522 });
		KEYS_MAP.put(">", new int[] { 160 });
		KEYS_MAP.put("<", new int[] { 153 });
		KEYS_MAP.put("'", new int[] { 222 });
		KEYS_MAP.put("\"", new int[] { 152 });
		KEYS_MAP.put("\n", new int[] { 10 });
		KEYS_MAP.put("\t", new int[] { 9 });
		KEYS_MAP.put("alt", new int[] { KeyEvent.VK_ALT });
		KEYS_MAP.put("ctrl", new int[] { 7 });
		KEYS_MAP.put("copy", new int[] { 65485 });
		KEYS_MAP.put("paste", new int[] { 65487 });
	}
	
	public Robot() throws Exception {
		
		processes = null;
		timeout = Long.valueOf(0L);
		pixelColor = null;
		
		setAutoWaitForIdle(true);
	}
	
	public static Double getScreenWidth() {
		return Double.valueOf(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
	}

	public static Double getScreenHeight() {
		return Double.valueOf(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void capturePixel(Integer x, Integer y) {
		pixelColor = getPixelColor(x.intValue(), y.intValue());
		
		/*int xS = 300, yS = 300;
		
		BufferedImage I = new BufferedImage(xS, yS, BufferedImage.TYPE_INT_RGB);
		int[] a = new int[yS * xS * 3]; // 96 bit pixels
		WritableRaster wr = I.getRaster();
		
		for (int j = 0; j < y; ++j){
			for (int i = 0; i < x; ++i) {
				int z = 3 * (j * x + i);
				a[z] = i;
				a[z + 1] = j;
				a[z + 2] = 128;
			}
		}
		
		wr.setPixels(30, 30, x, y, a);
		
		try {
			ImageIO.write(I, "png", new File("teste.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	public void waitForPixelChange(Integer x, Integer y) {
		if (pixelColor == null)
			return;
		
		Color comparePixelColor = null;
		
		do {
			comparePixelColor = getPixelColor(x.intValue(), y.intValue());
			
			try {
				Thread.sleep(100L);
				continue;
			} catch (InterruptedException e) { }
			
			break;
		} while (pixelColor.getRed() == comparePixelColor.getRed() && pixelColor.getGreen() == comparePixelColor.getGreen() && pixelColor.getBlue() == comparePixelColor.getBlue());
		
		pixelColor = null;
	}

	public void keyPress(Integer key) {
		super.keyPress(key.intValue());
	}

	public void keyRelease(Integer key) {
		super.keyRelease(key.intValue());
	}

	public void mousePress(Integer code) {
		super.mousePress(code.intValue());
	}

	public void mouseRelease(Integer code) {
		super.mouseRelease(code.intValue());
	}

	public void delay(Integer delay) {
		super.delay(delay.intValue());
	}

	public void mouseMove(Integer x, Integer y) {
		super.mouseMove(x.intValue(), y.intValue());
	}

	public void mouseClick() {
		mousePress(16);
		mouseRelease(16);
	}

	public void mouseClick(Integer x, Integer y) {
		capturePixel(x, y);
		mouseRightClick();
		waitForPixelChange(x, y);
	}

	public void mouseRightClick(Integer x, Integer y) {
		capturePixel(x, y);
		mouseRightClick();
		waitForPixelChange(x, y);
	}

	public void mouseRightClick() {
		mousePress(4);
		mouseRelease(4);
	}

	public void type(String value, Integer x, Integer y) {
		capturePixel(x, y);
		type(value);
		waitForPixelChange(x, y);
	}

	public void type(String value) {
		if ( this.removeEmpty(value).length() > 0) {
			int keyCodes[] = (int[]) null;
			String keyChar = "";
			
			for (int cont1 = 0; cont1 < value.length(); cont1++) {
				keyChar = value.substring(cont1, cont1 + 1);
				keyCodes = (int[]) KEYS_MAP.get(keyChar);
				
				if (keyCodes != null) {
					for (int cont2 = 0; cont2 < keyCodes.length; cont2++)
						keyPress(keyCodes[cont2]);

					for (int cont2 = keyCodes.length - 1; cont2 >= 0; cont2--)
						keyRelease(keyCodes[cont2]);

				}
			}

		}
	}

	public void destroy(String executable) throws Throwable {
		if (processes != null) {
			
			Process process = (Process) processes.get(executable);
			
			if (process != null)
				process.destroy();
		}
	}

	

	public void shutdown() {
		if (processes != null && processes.size() > 0) {
			Process process = null;

			for (Iterator iterator = processes.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				process = (Process) processes.get(key);
				
				if (process != null)
					process.destroy();
			}

			processes.clear();
		}
		
	}

	public static String removeEmpty(Object value){
		if(value == null)
			return "";

		if(value.toString() == null)
			return "";

		return value.toString().trim();
	}
	
	/**
	 * Transforma uma string delimitada em um array utilizando um delimitador espec�fico.
	 * 
	 * @param value String delimitada.
	 * @param delimiter String contendo o delimitador.
	 * @return Array contendo as strings.
	 */
	public static String[] disassemble(String value, String delimiter){
		StringTokenizer tokenizer = new StringTokenizer(value, delimiter);
		String          tokens[]  = new String[tokenizer.countTokens()];

		for(Integer cont = 0 ; cont < tokens.length ; cont++)
			tokens[cont] = tokenizer.nextToken();

		return tokens;
	}
	
	public static String replaceAll(String value, String search, String replace){
		if(search.length() == 0)
			return value;
		
		Integer pos = value.indexOf(search);

		if(pos < 0)
			return value;

		StringBuilder replaceBuffer = new StringBuilder();

		replaceBuffer.append(value.substring(0, pos));
		replaceBuffer.append(replace);
		replaceBuffer.append(value.substring(pos + search.length()));

		return replaceAll(replaceBuffer.toString(), search, replace);
	}
	
	public static String[] disassemble(String value){
		return disassemble(value, DEFAULT_DELIMITER);
	}
}

class InternalErrorException extends Exception{
	/**
	 * Construtor default.
	 */
	public InternalErrorException(){
		super();
	}

	/**
	 * Construtor - Define a mensagem do erro.
	 * 
	 * @param message String contendo a mensagem.
	 */
	public InternalErrorException(String message){
		super(message);
	}

	/**
	 * Construtor - Encapsula o erro que original.
	 * 
	 * @param exception Instncia contendo o erro original.
	 */
	public InternalErrorException(Throwable exception){
		super(exception);
	}
}

class InvalidResourceException extends InternalErrorException{
	private String resourceId = "";

	/**
	 * Construtor - Inicializa propriedades da exceo.
	 * 
	 * @param resourceId String contendo o identificador do recurso.
	 */
	public InvalidResourceException(String resourceId){
		super(resourceId);

		setResourceId(resourceId);
	}
	
	/**
	 * Construtor - Inicializa propriedades da exceo.
	 * 
	 * @param resourceId String contendo o identificador do recurso.
	 * @param e Instncia contendo a exceo gerada.
	 */
	public InvalidResourceException(String resourceId, Throwable e){
		super(e);
		
		setResourceId(resourceId);
	}

	/**
	 * Retorna o identificador do recurso.
	 * 
	 * @return String contendo o identificador do recurso.
	 */
	public String getResourceId(){
		return resourceId;
	}

	/**
	 * Define o identificador do recurso.
	 * 
	 * @param resourceId String contendo o identificador do recurso.
	 */
	public void setResourceId(String resourceId){
		this.resourceId = resourceId;
	}
}