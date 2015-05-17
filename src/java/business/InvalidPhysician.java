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
User Defined Exception for InvalidAppointment
*/
public class InvalidPhysician extends Exception {
    public InvalidPhysician(){
        super("The Physician that you have selected does not exist.");
    }
}
