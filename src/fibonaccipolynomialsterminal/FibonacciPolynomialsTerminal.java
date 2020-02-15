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
        
    
    public static void main(String[] args) {   
        
        boolean done = false;
        
        Scanner in = new Scanner(System.in);
        
        
        System.out.println("Welcome to Fibonacci Polynomials\n");
        
        while(!done){
            
            
            
            System.out.println("\nWhat would you like to do?");
            System.out.println("(1) Find Clusterings | (2) Find Coprime Product | (3) Find Coprime Clusters  | (Q)uit");
            System.out.print("Input: ");
            
            
            try{
                String input = in.next();
                input = input.toUpperCase();
                
                switch(input){
                    case "1":
                        executeClusterings();
                        break;
                    case "2":
                        executeCoprime();
                        break;
                    case "3":
                        executeCoprimeClusterings();
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
    
    /*
    *
    *Execution Methods
    *
    */
    
    private static void executeCoprimeClusterings(){
        getNandXInput();
        ArrayList<FibonacciFactor> coprimes = generateCoprimeFactors();
        Double product = findProduct(coprimes);
        System.out.println("Product of Coprimes: " + product);
        printFactors(coprimes);
        TreeMap<Long,Integer> clusters = findClusters(coprimes);
        try{
            createClustersFile(clusters, "coprime");
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    private static void executeCoprime(){
        getNandXInput();
        ArrayList<FibonacciFactor> coprimes = generateCoprimeFactors();
        printFactors(coprimes);
        double coprimeProduct = findProduct(coprimes);
        System.out.println("Product of Coprimes: " + coprimeProduct);
    }
    
    private static void executeClusterings(){
        getNandXInput();
            
        ArrayList<FibonacciFactor> factors = generateFactors();
        Double product = findProduct(factors);
        System.out.println("Product: " + product);
        printFactors(factors);
        TreeMap<Long,Integer> clusters = findClusters(factors);
        //printClusterings();
        try{
            createClustersFile(clusters); 
        }
        catch(Exception e){System.err.println(e.toString());}
    }
    
    /*
    *
    *Factor Generation Methods
    *
    */
    
    private static ArrayList<FibonacciFactor> generateFactors(){
        //factors = new ArrayList<>();
        ArrayList<FibonacciFactor> significantFactors = new ArrayList<>();
        FibonacciFactor f;
        
        for(int i = 1; i <= n/2; ++i){
            f = new FibonacciFactor(i,n,x);
            significantFactors.add(f);
        }
        
        return significantFactors;
    }
    
    private static ArrayList<FibonacciFactor> generateCoprimeFactors(){
        ArrayList<FibonacciFactor> coprimes = new ArrayList<>();
        FibonacciFactor f;
        
        for(int i = 1; i <=n/2; ++i){
            if(isCoprime(i,n)){
                f = new FibonacciFactor(i,n,x);
                coprimes.add(f);
            }
        }
        
        return coprimes;
    }
    
    /*
    *
    *Computational Methods
    *
    */
    
    private static TreeMap<Long,Integer> findClusters(ArrayList<FibonacciFactor> significantFactors){
        int numFactors = significantFactors.size();
        ArrayList<FibonacciFactor> factorSubset = new ArrayList();
        long powerSetMax = (long)Math.pow(2, numFactors);
        
        double productSum = 0;
        double productMean = 0;
        
        TreeMap<Long,Integer> clusterings = new TreeMap<>();
        
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
        System.out.println("Number of Cluster Values: " + clusterings.size());
        System.out.println("Sum of Products: " + productSum);
        System.out.println("Mean of Products: " + productMean + "\n");
        
        return clusterings;
    }
    
    
    private static double findProduct(ArrayList<FibonacciFactor> factors){
        double product = 1;
        
        for(FibonacciFactor f: factors){
            if(!f.equalsOne())
                product *= f.getValue();
        }
        return product;
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
    
    
    /*
    *
    *Input/Output Methods
    *
    */
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
    
    private static void printClusterings(TreeMap<Long,Integer> clusterings){
        
        System.out.println("Clusterings: ");
        
        for(HashMap.Entry<Long,Integer> cluster : clusterings.entrySet()){
            System.out.println("Nearest Int: " + cluster.getKey() +" | Count: " + cluster.getValue());
        }
    }
    
    private static void printFactors(ArrayList<FibonacciFactor> factors){
        StringBuilder output = new StringBuilder();
        
        output.append("Factors: ");
        
        for(FibonacciFactor f: factors){
            output.append(f.toString());
        }
        
        System.out.println(output.toString());
    }
    
    private static void createClustersFile(TreeMap<Long,Integer> clusterings)throws FileNotFoundException, IOException{
        
        StringWriter writer = new StringWriter();
        CSVWriter csv = new CSVWriter(writer,',','"','\n');
        String[] row = {"Value", "Count"};
        csv.writeNext(row);
        
        for(HashMap.Entry<Long,Integer> cluster : clusterings.entrySet()){
            row[0] = String.valueOf(cluster.getKey());
            row[1] = String.valueOf(cluster.getValue());
            csv.writeNext(row);
        }
        
        String fileName = "F"+ n + "_Clusters.csv";
        
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        File file = new File(filePath + fileName);
        file.createNewFile();
        
        PrintWriter out = new PrintWriter(file);
        out.write(writer.toString());
        
        
        
        out.write('\n');
        out.close();
        
    }
   
    private static void createClustersFile(TreeMap<Long,Integer> clusterings, String fileDesc)throws FileNotFoundException, IOException{
        
        StringWriter writer = new StringWriter();
        CSVWriter csv = new CSVWriter(writer,',','"','\n');
        String[] row = {"Value", "Count"};
        csv.writeNext(row);
        
        for(HashMap.Entry<Long,Integer> cluster : clusterings.entrySet()){
            row[0] = String.valueOf(cluster.getKey());
            row[1] = String.valueOf(cluster.getValue());
            csv.writeNext(row);
        }
        
        String fileName = "F"+ n +"_" +fileDesc+ "_Clusters.csv";
        
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        File file = new File(filePath + fileName);
        file.createNewFile();
        
        PrintWriter out = new PrintWriter(file);
        out.write(writer.toString());
        
        
        
        out.write('\n');
        out.close();
        
    }
    
}
