/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.lang.Exception;

/**
 *
 * @author MaulikZota
 */

/*
User Defined Exception for Invalid TimeInterval
*/
public class InvalidTimeInterval extends Exception {
    public InvalidTimeInterval(){
        super("The Phlebotomist that you have selected is not avaialble.");
    }
}
