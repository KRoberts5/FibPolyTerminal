/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fibonaccipolynomialsterminal;

/**
 *
 * @author Brendan
 */
public class FibonacciFactor extends Factor{
    public FibonacciFactor(int k, int n){
        super(k,n);
    }
    
    public FibonacciFactor(int k, int n, double x){
        super(k,n,x);
    }
    public FibonacciFactor(int k, int n, double x, double y){
        super(k,n,x,y);
    }
     protected void setValue(){
        double theta = (k*Math.PI)/n;
        double cosine = Math.cos(theta);
        
        value = Math.pow(x, 2) + 4*y*(Math.pow(cosine, 2));
    }
    public String toString(){
        
        String output = "(" +x+ "^2 + 4(" + y +")(COS(" + k + "pi/" + n + "))^2)";
        
        return output;
    }
}
