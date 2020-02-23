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
        private static double y;
        
    
    public static void main(String[] args) {   
        
        boolean done = false;
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Fibonacci Polynomials\n");
        
        while(!done){
            System.out.println("\nWhat would you like to do?");
            System.out.println("(1) Find Fibonacci Clusters | (2) Find Fibonacci Coprime Product | (3) Find Fibonacci Coprime Clusters  "
                    + "\n(4) Find Lucas Clusters | (5) Find Lucas Coprime Clusters | (Q)uit");
            System.out.print("Input: ");
            
            try{
                String input = in.next();
                input = input.toUpperCase();
                
                switch(input){
                    case "1":
                        executeFindFibonacciClusters();
                        break;
                    case "2":
                        executeFindFibonacciCoprimes();
                        break;
                    case "3":
                        executeFindFibonacciCoprimeClusters();
                        break;
                    case "4":
                        executeFindLucasClusters();
                        break;
                    case "5":
                        executeFindLucasCoprimeClusters();
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
    
    private static void executeFindFibonacciCoprimeClusters(){
        getUserInput();
        ArrayList<Factor> coprimes = generateCoprimeFibonacciFactors();
        Double product = findProduct(coprimes);
        System.out.println("Product of Coprimes: " + product);
        printFactors(coprimes);
        TreeMap<Long,Integer> clusters = findClusters(coprimes);
        try{
            createClustersFile(clusters,"F", "coprime");
        }
        catch(Exception e){
            System.err.println(e);
        }
    }
    
    private static void executeFindFibonacciCoprimes(){
        getUserInput();
        ArrayList<Factor> coprimes = generateCoprimeFibonacciFactors();
        printFactors(coprimes);
        double coprimeProduct = findProduct(coprimes);
        System.out.println("Product of Coprimes: " + coprimeProduct);
    }
    
    private static void executeFindFibonacciClusters(){
        getUserInput();
        ArrayList<Factor> factors = generateFibonacciFactors();
        Double product = findProduct(factors);
        System.out.println("Product: " + product);
        printFactors(factors);
        TreeMap<Long,Integer> clusters = findClusters(factors);
        try{
            createClustersFile(clusters, "F"); 
        }
        catch(Exception e){System.err.println(e.toString());}
    }
    
    private static void executeFindLucasFactors(){
        getUserInput();
        ArrayList<Factor> factors = generateLucasFactors();
        Double product = findProduct(factors);
        System.out.println("Product: " + product);
        printFactors(factors);
    }
    private static void executeFindLucasClusters(){
        getUserInput();
        ArrayList<Factor> factors =generateLucasFactors();
        double product = findProduct(factors);
        System.out.println("Product: " + product);
        printFactors(factors);
        TreeMap<Long,Integer> clusters = findClusters(factors);
        try{
            createClustersFile(clusters,"L");
        }
        catch(Exception e){System.err.println(e.toString());}
    }
    private static void executeFindLucasCoprimeClusters(){
        getUserInput();
        ArrayList<Factor> factors =generateCoprimeLucasFactors();
        double product = findProduct(factors);
        System.out.println("Product: " + product);
        printFactors(factors);
        TreeMap<Long,Integer> clusters = findClusters(factors);
        try{
            createClustersFile(clusters,"L","Coprime");
        }
        catch(Exception e){System.err.println(e.toString());}
    }
    
    /*
    *
    *Factor Generation Methods
    *
    */
    
    private static ArrayList<Factor> generateFibonacciFactors(){
        ArrayList<Factor> significantFactors = new ArrayList<>();
        Factor f;
        
        for(int i = 1; i <= n/2; ++i){
            f = new FibonacciFactor(i,n,x,y);
            significantFactors.add(f);
        }
        
        return significantFactors;
    }
    
    private static ArrayList<Factor> generateCoprimeFibonacciFactors(){
        ArrayList<Factor> coprimes = new ArrayList<>();
        Factor f;
        
        for(int i = 1; i <=n/2; ++i){
            if(isCoprime(i,n)){
                f = new FibonacciFactor(i,n,x,y);
                coprimes.add(f);
            }
        }
        
        return coprimes;
    }
    
    private static ArrayList<Factor> generateLucasFactors(){
        ArrayList<Factor> factors = new ArrayList();
        for(int i = 1; i <= n/2; ++i)
            factors.add(new LucasFactor(i,n,x,y));
        return factors;
    }
    
    private static ArrayList<Factor> generateCoprimeLucasFactors(){
        ArrayList<Factor> factors = new ArrayList();
        for(int i = 1; i <= n/2; ++i){
            if(isCoprime(i,n))
                factors.add(new LucasFactor(i,n,x,y));
        }
        return factors;
    }
    
    
    /*
    *
    *Computational Methods
    *
    */
    
    private static TreeMap<Long,Integer> findClusters(ArrayList<Factor> significantFactors){
        int numFactors = significantFactors.size();
        long powerSetMax = (long)Math.pow(2, numFactors);
        double productSum = 0;
        double productMean = 0;
        TreeMap<Long,Integer> clusterings = new TreeMap<>();
        int pc = 0;
        
        for(int i = 1; i < powerSetMax; ++i){
            
            double currentValue = 1;
            String binary = generateBinaryString(i,numFactors);
            
            for(int j = 0; j <binary.length(); ++j){
                
                if(binary.charAt(j) == '1')
                    currentValue = currentValue * significantFactors.get(j).getValue();
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
        
        productMean = (double)productSum/pc;
        
        System.out.println("");
        System.out.println("Number of Combinations: " + pc);
        System.out.println("Number of Cluster Values: " + clusterings.size());
        System.out.println("Sum of Products: " + productSum);
        System.out.println("Mean of Products: " + productMean + "\n");
        
        return clusterings;
    }
    
    
    private static double findProduct(ArrayList<Factor> factors){
        double product = 1;
        
        for(Factor f: factors){
            if(!f.equalsOne())
                product *= f.getValue();
        }
        return product;
    }
    
    
    private static String generateBinaryString(int num, int digits){
        //num is the value of the number
        //digits is how many digits in the number (So that leading zeroes can be created).
        
        String binaryNLZ = Integer.toBinaryString(num); //Binary String with no leading zeroes
        /*int numZeroes = digits - binaryNLZ.length();
        
        String zeroString = "";
        
        if(numZeroes > 0) {
            for(int i = 0; i < numZeroes; ++i){
                zeroString += "0";
            }
        }*/
        
        String binaryString = String.format("%" + digits + "s", binaryNLZ).replace(" ", "0");
        
        return binaryString;
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
    private static void getUserInput(){
        Scanner in = new Scanner(System.in);
            try{
                System.out.print("Nth Polynomial: ");
                n = in.nextInt();
                
                System.out.print("X value: ");
                x = in.nextDouble();
                
                System.out.print("Y value: ");
                y = in.nextDouble();
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
    
    private static void printFactors(ArrayList<Factor> factors){
        StringBuilder output = new StringBuilder();
        
        output.append("Factors: ");
        
        for(Factor f: factors){
            output.append(f.toString());
        }
        
        System.out.println(output.toString());
    }
    
    private static void createClustersFile(TreeMap<Long,Integer> clusterings,String factorType)throws FileNotFoundException, IOException{
        
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
   
    private static void createClustersFile(TreeMap<Long,Integer> clusterings, String factorType, String fileDesc)throws FileNotFoundException, IOException{
        
        StringWriter writer = new StringWriter();
        CSVWriter csv = new CSVWriter(writer,',','"','\n');
        String[] row = {"Value", "Count"};
        csv.writeNext(row);
        
        for(HashMap.Entry<Long,Integer> cluster : clusterings.entrySet()){
            row[0] = String.valueOf(cluster.getKey());
            row[1] = String.valueOf(cluster.getValue());
            csv.writeNext(row);
        }
        
        String fileName = factorType + "_"+ n +"_" +fileDesc+ "_Clusters.csv";
        
        String filePath = System.getProperty("user.home") + "\\Desktop\\";
        File file = new File(filePath + fileName);
        file.createNewFile();
        
        PrintWriter out = new PrintWriter(file);
        out.write(writer.toString());
        
        out.write('\n');
        out.close();
    }
}
