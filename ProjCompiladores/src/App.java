public class App{
    public static void main(String args[]){
        AnalisadorLexico an = new AnalisadorLexico("src/input.in");
        Token t;
        
        while (!an.isEOF()){
            t = an.getToken();
            if (t != null){
                System.out.println("Reconhecido "+t.getCode() + "  "+t.getSymbol());
            }
            else{
                System.out.println("Token invalido");
            }
            an.nextToken();
        }
    }
}