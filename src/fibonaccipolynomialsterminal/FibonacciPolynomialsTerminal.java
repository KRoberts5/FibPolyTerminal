/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fibonaccipolynomialsterminal;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


/**
 *
 * @author Brendan
 */



public class FibonacciPolynomialsTerminal {

    /**
     * @param args the command line arguments
     */
        

    
        private static int n;
        private static double x;
        private static int desiredProduct;
        private static double error;
        
        //private static ArrayList<Factor> factors;
        private static ArrayList<Factor> significantFactors;
        private static StringBuilder fullFactorization;
        private static TreeMap<Long,Integer> clusterings;

    
    public static void main(String[] args) {   
        
        boolean done = false;
        
        Scanner in = new Scanner(System.in);
        
        
        System.out.println("Welcome to Fibonacci Polynomials\n");
        
        while(!done){
            
            System.out.println("\nWhat would you like to do?");
            System.out.println("(F)ind Clusterings | Find (C)oprime Product | (Q)uit");
            System.out.print("Input: ");
            
            
            try{
                String input = in.next();
                input = input.toUpperCase();
                
                switch(input){
                    case "F":
                        executeClusterings();
                        break;
                    case "C":
                        executeCoprime();
                        break;
                    case "Q":
                        done = true;
                        break;
                    default:
                        System.out.println("Invalid Input\n");
                }
                
                
            }
            catch(Exception e){
                System.err.println(e.toString());
            }
            
            
            
        }
    }
    
    private static void getNandXInput(){
        Scanner in = new Scanner(System.in);
            try{
                System.out.print("Nth Polynomial: ");
                n = in.nextInt();
                
                System.out.print("X value: ");
                x = in.nextDouble();
            }
            catch(Exception e){
                System.err.println(e.toString());
            }
            
            
    }
    
    private static void executeCoprime(){
        getNandXInput();
        ArrayList<Factor> coprimes = generateCoprimeFactors();
        printFactors(coprimes);
        
        double coprimeProduct = findProduct(coprimes);
        System.out.println("Product of Coprimes: " + coprimeProduct);
    }
    
    private static void printFactors(ArrayList<Factor> factors){
        StringBuilder output = new StringBuilder();
        
        output.append("Factors: ");
        
        for(Factor f: factors){
            output.append(f.toString());
        }
        
        System.out.println(output.toString());
    }
    
    private static double findProduct(ArrayList<Factor> factors){
        double product = 1;
        
        for(Factor f: factors){
            product *= f.getValue();
        }
        return product;
    }
    
    private static ArrayList<Factor> generateCoprimeFactors(){
        ArrayList<Factor> coprimes = new ArrayList<>();
        Factor f;
        
        for(int i = 1; i <=n/2; ++i){
            if(isCoprime(i,n)){
                f = new Factor(i,n,x);
                coprimes.add(f);
            }
        }
        
        return coprimes;
    }
    
    private static void executeClusterings(){
        getNandXInput();
            
        generateFactors();
        findClusters();
        printClusterings();
            
    }
    
    private static void generateFactors(){
        //factors = new ArrayList<>();
        significantFactors = new ArrayList<>();
        fullFactorization = new StringBuilder();
        Factor f;
        double product = 1;
        
        
        
        for(int i = 1; i <= n/2; ++i){
            f = new Factor(i,n,x);
            
            fullFactorization.append(f.toString() + " ");
            
            if(!f.equalsOne()){
                significantFactors.add(f);
                product *= f.getValue();
            }
            
        }
        
        System.out.println("Product Value: " + product);
        System.out.println("Factors: \n"+fullFactorization.toString());
        
    }
    
    private static void findClusters(){
        int numFactors = significantFactors.size();
        ArrayList<Factor> factorSubset = new ArrayList();
        long powerSetMax = (long)Math.pow(2, numFactors);
        
        double productSum = 0;
        double productMean = 0;
        
        clusterings = new TreeMap<>();
        
        int pc = 0;
        
        
        for(int i = 1; i < powerSetMax; ++i){
            
            
            factorSubset = new ArrayList();
            Double currentValue = new Double(1);
            StringBuilder subsetFactorization = new StringBuilder();
            String binary = generateBinaryString(i,numFactors);
            
            
            
            for(int j = 0; j <binary.length(); ++j){
                
                if(binary.charAt(j) == '1'){
                    
                    factorSubset.add(significantFactors.get(j));
                    subsetFactorization.append(significantFactors.get(j).toString() + "  ");
                    currentValue = currentValue * significantFactors.get(j).getValue();
                    
                }
            }
            
            ++pc;
            
            Long key = Math.round(currentValue);
            
            if(clusterings.containsKey(key)){
                Integer count = clusterings.get(key);
                ++count;
                clusterings.put(key, count);
            }
            else
                clusterings.put(key, 1);
            
            productSum += currentValue;
        }
        
        productMean = productSum/pc;
        
        
        System.out.println("");
        System.out.println("Number of Combinations: " + pc);
        System.out.println("Sum of Products: " + productSum);
        System.out.println("Mean of Products: " + productMean + "\n");
    }
    
    private static void printClusterings(){
        
        System.out.println("Clusterings: ");
        
        for(HashMap.Entry<Long,Integer> cluster : clusterings.entrySet()){
            System.out.println("Nearest Int: " + cluster.getKey() +" | Count: " + cluster.getValue());
        }
    }
    
    private static void createFile(String fileName, String data) throws FileNotFoundException, IOException{
        
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        File file = new File(filePath + fileName);
        file.createNewFile();
        
        PrintWriter out = new PrintWriter(file);
        out.write(data);
        out.write('\n');
        out.close();
    }
    
    private static String generateBinaryString(int num, int digits){
        //num is the value of the number
        //digits is how many digits in the number (So that leading zeroes can be created).
        
        String binaryNLZ = Integer.toBinaryString(num); //Binary String with no leading zeroes
        int numZeroes = digits - binaryNLZ.length();
        
        String zeroString = "";
        
        if(numZeroes > 0) {
            for(int i = 0; i < numZeroes; ++i){
                zeroString += "0";
            }
        }
        
        return zeroString + binaryNLZ;
    }
    
    public static boolean isCoprime(int x, int y){
        
        boolean coprime = true;
        
        if((x!=1)&&(y!=1)){
            if((x%y==0)||(y%x==0))
                coprime = false;
            else{
                for(int i = 2; (i<=(x/2))&&(i<=y/2); ++i){
                    if((x%i==0)&&(y%i==0))
                        coprime = false;
                }
            }
        }
        return coprime;
    }
   
        
    
    
}
