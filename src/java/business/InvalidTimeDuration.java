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
User Defined Exception for Invalid TimeDuration
*/
public class InvalidTimeDuration extends Exception{
    public InvalidTimeDuration(){
        super("Next Appointment can be booked only after 15 minutes.");
    }
}
