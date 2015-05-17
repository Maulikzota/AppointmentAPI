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
User Defined Exception for Invalid Time
*/
public class InvalidTime extends Exception{
    public InvalidTime(){
        super("Cannot book any appointment after 5pm and before 8am");
    }
}
