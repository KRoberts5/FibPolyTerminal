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

import java.lang.Math;

public class Factor {
    
    private double value;
    private double x;
    private int k;
    private int n;
    private boolean equalsOne;
    
    /*public Factor(double x){
            this.x = x;
            this.value = x;
            
            k = 0;
            n = 0;
    }*/
    
    public Factor(int k, int n){
        
        this.k = k;
        this.n = n;
        this.x = 1;
        
        this.setValue();
        if(value == 1)
            equalsOne = true;
        else
            equalsOne = false;
    }
    
    public Factor(int k, int n, double x){
        this.k = k;
        this.n = n;
        this.x = x;
        
        this.setValue();
        
        if(value == 1)
            equalsOne = true;
        else
            equalsOne = false;
        
    }
    
    private void setValue(){
        double theta = (k*Math.PI)/n;
        double cosine = Math.cos(theta);
        
        value = Math.pow(x, 2) + 4*(Math.pow(cosine, 2));
    }
    
    
    
    public String toString(){
        
        String output ="";
        
        if(k == 0)
            output = "(" + x+")";
        else
            output = "(" +x+ "^2 + 4*(COS(" + k + "*pi/" + n + "))^2)";
        
        return output;
    }
    public double getValue(){
        return value;
    }
    
    public boolean equalsOne(){
        return this.equalsOne;
    }
}
