package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     Experiment with different algorithms to find accurate results.

     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all of the test cases provided in the test folder.

     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */
    public static class Pi implements Runnable{
        static MathContext MC= new MathContext(1000);
        public int start;
        public BigDecimal value=new BigDecimal(0);
        public Pi(int start) {
            this.start=start;
        }

        public void run(){
            for(int i=start;i<start+250;i++) {
                BigDecimal sorat=factorial(i);
                sorat=sorat.pow(2,MC);
                BigDecimal temp=new BigDecimal(2);
                temp=temp.pow(i+1,MC);
                sorat=sorat.multiply(temp);
                BigDecimal makhraj=factorial(2*i+1);
                value=value.add(sorat.divide(makhraj,MC));
            }
            addsum(value);
        }
        public BigDecimal factorial(int n){
            BigDecimal temp = new BigDecimal(1);
            for (int i = 1; i <= n; i++) {
                temp = temp.multiply(new BigDecimal(i), MC);
            }

            return temp;
        }
    }
    public static BigDecimal sum = new BigDecimal(0);

    public static synchronized void addsum(BigDecimal value){
        sum=sum.add(value);
    }
    public static String calculate(int floatingPoint)
    {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for(int i=0; i<16;i++){
            Pi task=new Pi(i*250);
            threadPool.execute(task);
        }
        threadPool.shutdown();

        try {
            threadPool.awaitTermination(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(sum.setScale(floatingPoint, RoundingMode.DOWN));
    }

    public static void main(String[] args) {
        // Use the main function to test the code yourself
        System.out.println( calculate(1000));
    }
}