/*
    SamuraiPi - a Java program for calculating Pi with a series derived
    from Wasan in Edo-Japan.
    Copyright (C) 2025 Hans-Christian Meynert

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// Here, you could use the source code of the Java BigDecimal classes
// instead of using the system classes
// The advantage would be complete control over the calculation
// a control which is indispensable when calculating Pi
//import bd.java.math.BigDecimal;
//import bd.java.math.RoundingMode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.io.*;
import java.util.*;

public class SamuraiPi {

    // several global variables
    public static BigDecimal number;
    public static BigDecimal result;
    public static BigDecimal mulFrac;
    public static BigDecimal divFrac;
    public static int digits;
    public static int summands;
    public static int order = 1;
    public static int i;
    public static BigDecimal square, numberSin, sinSP;
    public static String message = "";
    // denomCPU sets the fraction of the CPU used,
    // for example denomCPU = 8 means,
    // that the CPU load is 1/4 = 25 % during calculation
    public static int denomCPU = 4;

    // Calculate Pi with my series derived from Takebes square root
    // series from Japanese mathematics (wasan).
    public static void calculatePi() {
        long estimatedTime = 1;
        while(i <= summands) {
            
            // start measuring time for calculating one summand
            long startTime = System.currentTimeMillis();
            
            // Multiply last summand with
            // [2i * (2i - 1) / (16 * i * i)] *
            // (2 * i - 1) / (2 * i + 1) 
            // to get next (i'th) summand of my series
            mulFrac =
            new BigDecimal(
                "" + ((double) (2 * i - 1) * (2 * i - 1))
            );
            divFrac =
            new BigDecimal("" + ((double) i * 8 * (2 * i + 1)));
            number = number.multiply(mulFrac);
            number =
            number.divide(divFrac, digits, RoundingMode.HALF_UP);
            result = result.add(number);
            
            // Show 100 messages in total
            int s = (summands / 100);
            if(s == 0) {
                s = 1;
            }
            if(i % s == 0) {
                // Show message
                message = "Calculate: " + i + "/" + summands;
                System.out.print(message);
                // Backspace characters to overwrite last message
                // to display next message on top
                for(int k = 0; k < message.length(); k++) {
                    System.out.print("\b");
                }
            }
            // stop measuring time for calculating one summand
            estimatedTime = System.currentTimeMillis() - startTime;
            int pauseTime = (int) estimatedTime * (denomCPU - 1);
            if(pauseTime < 1) {
                pauseTime = 1;
            }
            try {
                // pause calculation to cool down CPU
                Thread.sleep(pauseTime);
            } catch(InterruptedException v) {
                System.out.println(v);
            }
            
            i++;
        }

    }

    // Calculate sine of SamuraiPi in order to verify the digits
    // Sine of SamuraiPi directly is the error of SamuraiPi
    // because of Delta
    // sin(Samurai-Pi) = cos(Samurai-Pi) * Delta Samurai-Pi
    public static void verifyPi() {
        long estimatedTime = 1;
        while(i <= summands) {

            // start measuring time for calculating one summand
            long startTime = System.currentTimeMillis();
            // sine Taylor series
            divFrac =
            new BigDecimal("" + ((double) 2 * i * (2 * i + 1)));
            numberSin = numberSin.multiply(square);

            numberSin =
            numberSin.divide(
                divFrac, digits, RoundingMode.HALF_UP
            );
            // Taylor series of sine is alternating
            if(i % 2 == 0) {
                sinSP = sinSP.add(numberSin);
            }
            if(i % 2 == 1) {
                sinSP = sinSP.subtract(numberSin);
            }
            
            // Show 100 messages in total
            int s = (summands / 100);
            if(s == 0) {
                s = 1;
            }
            if(i % s == 0) {
                // Show message
                message = "Verify: " + i + "/" + summands + "       ";
                System.out.print(message);
                // Backspace characters to overwrite last message
                // to display next message on top
                for(int k = 0; k < message.length(); k++) {
                    System.out.print("\b");
                }
            }

            // stop measuring time for calculating one summand
            estimatedTime = System.currentTimeMillis() - startTime;
            int pauseTime = (int) estimatedTime * (denomCPU - 1);
            if(pauseTime < 1) {
                pauseTime = 1;
            }
            try {
                // pause calculation to cool down CPU
                Thread.sleep(pauseTime);
            } catch(InterruptedException v) {
                System.out.println(v);
            }

            i++;

        }

    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        // Ask for number of digits wanted
        System.out.println(
            "How many digits of Pi do you want to calculate? " +
            " (<= 1,000,000)"
        );
        digits = sc.nextInt();
        if(digits > 1000000) {
            digits = 1000000;
        }
        boolean verification = true;
        if(digits > 10000) {
            // It is to be said, with more than 10,000 digits, the
            // verification process may lead to possibly harmful CPU
            // peaks (because of more than 100,000,000 multiplications
            // of digits at once and repeatedly in this case)
            // You could only avoid this by editing the BigDecimal class
            // or by renouncing the verification in these cases.
            // In contrast, the calculation process is harmless, because
            // it does involve only the multiplication and division
            // by short integers.
            verification = false;
            // Show message
            System.out.println(
                "Verification disabled because the number of digits" +
                " exceeds 10,000."
                );
        }
        digits = digits + 5;
        // Show message
        System.out.println(
            "Using " + digits + " decimal places after decimal point."
            );
        summands = digits * 2;
    
        number = new BigDecimal("3.0");
        result = new BigDecimal("0.0");
        result = result.add(number);
    
        // Calculate first three summands with my series, where
        // [2i * (2i - 1) / (16 * i * i)] *
        // (2 * i - 1) / (2 * i + 1) 
        // is the quotient of subsequent summands
        mulFrac = new BigDecimal("" + ((double) 1 * 2 * 1));
        divFrac = new BigDecimal("" + ((double) 1 * 1 * 16 * 3));
        number = number.multiply(mulFrac);
        number = number.divide(divFrac, digits, RoundingMode.HALF_UP);
        result = result.add(number);
        
        mulFrac = new BigDecimal("" + ((double) 3 * 4 * 3));
        divFrac = new BigDecimal("" + ((double) 2 * 2 * 16 * 5));
        number = number.multiply(mulFrac);
        number = number.divide(divFrac, digits, RoundingMode.HALF_UP);
        result = result.add(number);
        
        mulFrac = new BigDecimal("" + ((double) 5 * 6 * 5));
        divFrac = new BigDecimal("" + ((double) 3 * 3 * 16 * 7));
        number = number.multiply(mulFrac);
        number = number.divide(divFrac, digits, RoundingMode.HALF_UP);
        result = result.add(number);
        
        // now set index of next summand to 4
        i = 4;
        
        // Calculate SamuraiPi to the desired number of digits
        // after decimal point
        calculatePi();
        
        // decide whether to do verification or not
        if(verification) {
            // start measuring time for calculating first summand
            // and multiplicator Samurai-Pi * Samurai-Pi
            long startTime = System.currentTimeMillis();

            sinSP = new BigDecimal("0.0");
            numberSin = result;
            square = result.multiply(result);

            // stop measuring time for calculating first summand
            // and multiplicator
            long estimatedTime = System.currentTimeMillis() - startTime;
            int pauseTime = (int) estimatedTime * (denomCPU - 1);
            if(pauseTime < 1) {
                pauseTime = 1;
            }
            try {
                // pause calculation to cool down CPU
                Thread.sleep(pauseTime);
            } catch(InterruptedException v) {
                System.out.println(v);
            }

            sinSP = sinSP.add(numberSin);

            // set number of next summand to 1
            i = 1;
            
            // Calculate sine of Samurai-Pi to estimate the error
            // and verify the digits calculated
            verifyPi();
            
            // Write out result to console
            System.out.println("Samurai-Pi = " + result +
                "\nsin(Samurai-Pi) = " + sinSP
            );

            // Write out result to file "samurai-pi.txt"
            try (BufferedWriter bw =
                new BufferedWriter(
                    new FileWriter(new File("samurai-pi.txt"))
                )
            ) {
                bw.write(
                    "Using " + digits +
                    " decimal places after decimal point.\n" + 
                    "Samurai-Pi = " + result +
                    "\nsin(Samurai-Pi) = " + sinSP
                );
            }   
            catch( Exception e) {
            }
            
        } else {
            // Write out result to console
            System.out.println("Samurai-Pi = " + result +
                "\nVerification not feasible because number of " +
                "digits exceeds 10,000."
            );

            // Write out result to file "samurai-pi.txt"
            try (BufferedWriter bw =
                new BufferedWriter(
                    new FileWriter(new File("samurai-pi.txt"))
                )
            ) {
                bw.write(
                    "Using " + digits +
                    " decimal places after decimal point.\n" + 
                    "Samurai-Pi = " + result +
                    "\nVerification not feasible because number of " +
                    "digits exceeds 10,000."
                );
            }   
            catch( Exception e) {
            }
        }
    }
}
