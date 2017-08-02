import java.io.*;

public class AnalisadorLexico {

	public static final int S0 = 0;
	public static final int S1 = 1;
	public static final int S2 = 2;
	public static final int S3 = 3;
	public static final int SL = 4;

	private String fileContent;
	private int  index;
	private int  estado;

	private Token currentToken;

	public Token getToken(){
		return this.currentToken;
	}

	public AnalisadorLexico(String fileName){
		try{
			FileReader f = new FileReader(new File(fileName));
			BufferedReader br = new BufferedReader(f);
			String linha;
			StringBuilder fContent = new StringBuilder();
			while ((linha = br.readLine()) != null){
				linha+='\n';
				fContent.append(linha);
			}
			br.close();
			fileContent = fContent.toString()+' '+'\0';  // forcei o caractere fim de arquivo
			System.out.println(fileContent);
			index = 0;
		}
		catch(Exception ex){
			System.err.println("Erro ao abrir arquivo");
		}
	}

	private char nextChar(){
		if (index < fileContent.length())
			return fileContent.charAt(index++);
		else 
			return '\0';
	}
	private boolean isDigit(char ch){
		return (ch >= '0' && ch <= '9');
	}
	private boolean isAlpha(char ch){
		return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'));
	}
	private boolean isSpace(char ch){
		return (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');
	}
	private boolean isOper(char ch){
		return (ch == '+' || ch == '-' || ch == '*' || ch == '/' );
	}
	public void retroceder(){
		index--;
	}
	public boolean isEOF(){
		return (index == fileContent.length());
	}

	public Token nextToken(){
		String symbol="";
		estado = S0;
		Token token;
		char c;

		while (true){

			switch (estado){
			case S0:
				c = nextChar();

				if (isSpace(c)){
					estado = S0;
				}
				else if (c == '\0'){
					token = new Token();
					token.setCode(Token.EOF);
					token.setSymbol("END OF FILE");
					return token;
				}
				else if (isDigit(c)){
					symbol+=c;
					estado = S1;   
				}
				else if (c == '.'){
					symbol+=c;
					estado = S2;
				}
				else if (isAlpha(c)){
					symbol+=c;
					estado = S3;
				}
				else if (isOper(c)){
					symbol+=c;
					token = new Token();
					token.setCode(Token.OPERADOR);
					token.setSymbol(symbol);
					return token;
				}
				else{
					estado = SL;
				}
				break;
			case S1:
				c = nextChar();
				
				if (isDigit(c)){
					symbol+=c;
					estado = S1;
				}
				else if (c == '.'){
					symbol+=c;
					estado = S2;
				}
				else if(isSpace(c) || isOper(c)){
					token = new Token();
					token.setCode(Token.NUMERO_INTEIRO);
					token.setSymbol(symbol);
					retroceder();
					return token;
				}
				else{
					estado = SL;
				}
				break;
			case S2:
				c = nextChar();
				
				if (isDigit(c)){
					symbol+=c;
					estado = S2;
				}
				else if(isSpace(c) || isOper(c)){
					token = new Token();
					token.setCode(Token.NUMERO_PONTO_FIXO);
					token.setSymbol(symbol);
					retroceder();
					return token;
				}
				else{
					estado = SL;
				}
				break;
			case S3:
				c = nextChar();
				
				if (isAlpha(c)){
					symbol+=c;
					estado = S3;
				}
				else if (isDigit(c)){
					symbol+=c;
					estado = S3;   
				}
				else if(isSpace(c)){
					token = new Token();
					token.setCode(Token.IDENTIFICADOR);
					token.setSymbol(symbol);
					retroceder();
					return token;
				}else{
					estado = SL;
				}
				break;
			case SL:
				index = fileContent.length();
				return null;

			}
		}
	}
}
