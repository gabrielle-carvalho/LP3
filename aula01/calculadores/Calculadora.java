public class Calculadora{
    private double num1, num2;
    private char operacao;
    public Calculadora (double num1, double num2, char operacao){
        this.num1=num1;
        this.num2=num2;
        this.operacao=operacao;
    }

    public void setOperacao(char operacao){
        this.operacao=operacao;
    }
    public void setNum1(double num1){
        this.num1=num1;
    }
    public void setNum2(double num2){
        this.num2=num2;
    }
    public double getNum1(){
        return this.num1;
    }
    public double getNum2(){
        return this.num2;
    }
    public char getOperacao(){
        return this.operacao;
    }


    private double soma(){
        return this.num1+this.num2;
    }
    public double sub(){
        return this.num1-this.num2;
    }
    public double mult(){
        return this.num1*this.num2;
    }
    public double div(){
        return this.num1/this.num2;
    }

    public double calcular(){
        switch(this.operacao){
            case '+':
                return this.soma();
            case '-':
                return this.sub();
            case '*':
                return this.mult();
            case '/':
                return this.div();
            default:
                System.out.println("operação inválida");
                return 0;
        }
    }
}
