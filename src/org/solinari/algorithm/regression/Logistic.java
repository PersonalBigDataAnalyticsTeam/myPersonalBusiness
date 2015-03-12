/**
 * 
 */
package org.solinari.algorithm.regression;

/**
 * @author solinari
 *
 */
public class Logistic {

	private double sigmoid(double x){
		return (1.0/(1+Math.exp(x)));
	}
	
}
