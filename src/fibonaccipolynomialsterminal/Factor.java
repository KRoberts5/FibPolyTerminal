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

public abstract class Factor {
    
    protected double value;
    protected double x;
    protected double y;
    protected int k;
    protected int n;
    protected boolean equalsOne;
    

    
    public Factor(int k, int n){
        
        this.k = k;
        this.n = n;
        this.x = 1;
        this.y = 1;
        
        this.setValue();
    }
    
    public Factor(int k, int n, double x){
        this.k = k;
        this.n = n;
        this.x = x;
        this.y = 1;
        this.setValue();
    }
    
    public Factor(int k, int n, double x, double y){
        this.k = k;
        this.n = n;
        this.x = x;
        this.y = y;
        this.setValue();
    }
    
    protected abstract void setValue();
    public abstract String toString();
    public double getValue(){
        return value;
    }
    
    public boolean equalsOne(){
        return (value == 1);
    }
}
