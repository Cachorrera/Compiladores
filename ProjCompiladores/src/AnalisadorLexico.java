import java.io.*;

public class AnalisadorLexico{
   
   public static final int S0 = 0;
   public static final int S1 = 1;
   public static final int S2 = 2;
   public static final int S3 = 3;
   public static final int S5 = 5;
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
              fContent.append(linha);
          }
          fileContent = fContent.toString()+'\0';  // forcei o caractere fim de arquivo
          System.out.println(fileContent);
          index = 0;
          //estado = S0;
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
                 // System.out.println(">> DEBUG  E="+estado+ "C="+c);
                  if (c == ' ' || c == '\t' || c == '\n' || c== '\r'){
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
                     // System.out.println(">>>> DEBUG - fui pra s1");
                  }
                  else if (c == '.'){
                      symbol+=c;
                      estado = S2;
                     // System.out.println(">>>> DEBUG - fui pra s2");
                  }
                  else if (isAlpha(c)){
                      symbol+=c;
                      estado = S5;
                  }
                  else{
                      estado = SL;
                  }
                  break;
              case S1:
                  c = nextChar();
                 // System.out.println(">> DEBUG  E="+estado+ "C="+c);
                  if (isDigit(c)){
                      symbol+=c;
                      estado = S1;
                  }
                  else if (c == '.'){
                      symbol+=c;
                      estado = S2;
                  }
                  else{
                      token = new Token();
                      token.setCode(Token.NUMERO_INTEIRO);
                      token.setSymbol(symbol);
                      retroceder();
                      return token;
                  }
                  break;
              case S2:
                  c = nextChar();
                  
                  if (isDigit(c)){
                      symbol+=c;
                      estado = S3;
                  }
                  else{
                      estado = SL;
                  }
                  break;
              case S3:
                  c = nextChar();
                 
                  if (isDigit(c)){
                      symbol+=c;
                      estado = S3;
                  }
                  else{
                      token = new Token();
                      token.setCode(Token.NUMERO_PONTO_FIXO);
                      token.setSymbol(symbol);
                      retroceder();
                      return token;
                  }
                  break;
              case S5:
                  c = nextChar();
                  
                  if (isDigit(c)){
                      symbol+=c;
                      estado = S5;
                  }else if(isAlpha(c)){
                      symbol+=c;
                      estado = S5;
                  }else if(c == ' ' || c == '\t' || c == '\n' || c== '\r'){
                      token = new Token();
                      token.setCode(Token.IDENTIFICADOR);
                      token.setSymbol(symbol);
                      retroceder();
                      return token;
                  }
                  break;
              case SL:
                  estado = S0;
                  return null;
                  
          }
       }
   }
}