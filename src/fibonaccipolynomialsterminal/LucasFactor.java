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
public class LucasFactor extends Factor{
    public LucasFactor(int k, int n){
        super(k,n);
    }
    
    public LucasFactor(int k, int n, double x){
        super(k,n,x);
    }
    public LucasFactor(int k, int n, double x, double y){
        super(k,n,x,y);
    }
     protected void setValue(){
        double theta = ((2*k-1)*Math.PI)/(2*n);
        double cosine = Math.cos(theta);
        
        value = Math.pow(x, 2) + 4*y*(Math.pow(cosine, 2));
    }
    public String toString(){
        
        String output = "(" +x+ "^2 + 4(" + y +")(COS(" + ((2*k)-1) + "pi/" + (2*n) + "))^2)";
        
        return output;
    }
}
