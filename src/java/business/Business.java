/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import components.data.*;
import java.util.List;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

/**
 *
 * @author Maulik
 */
public class Business {
      
      //Initializing the db component to use db object in order to access data layer
      IComponentsData db=new DB();
      List<Object> objs;
      
      //Checking the time duration for booking the appointment.Returns true if its a valid time.
      //Else throws InvalidTime Exception
      public boolean checktime(Appointment a) throws InvalidTime{
          Calendar inittime = Calendar.getInstance();
          inittime.set(Calendar.HOUR,8);
          inittime.set(Calendar.MINUTE,0);
          inittime.set(Calendar.SECOND,0);
          Calendar endtime = Calendar.getInstance();
          endtime.set(Calendar.HOUR,16);
          endtime.set(Calendar.MINUTE,45);
          endtime.set(Calendar.SECOND,0);
          long diff = Math.abs(a.getAppttime().getTime()-inittime.getTimeInMillis());
          long diff1 = Math.abs(endtime.getTimeInMillis()-a.getAppttime().getTime());
          System.out.println(diff);
          if(diff>0 && diff1>0){
                return true; 
          }else{
                throw new InvalidTime();
          }
      }
      
      //Checking the time duration for booking the next appointment. Retuerns true if the next appointment
      // is after 15 minutes.Else throws InvalidTimeDuration Exception
      public boolean checkduration(Appointment a) throws InvalidTimeDuration{
          String querystr;
          querystr = "pscid='";
          querystr = querystr.concat(a.getPscid().getId());
          querystr = querystr.concat("' AND apptdate='");
          querystr = querystr.concat(a.getApptdate().toString());
          querystr = querystr.concat("' AND id <> '"+a.getId().toString()+"'");
          objs = db.getData("Appointment",querystr);
          if(objs.size()>0){
              for(Object obj:objs){
                Appointment apt = (Appointment)obj;
                long diff1 = a.getAppttime().getTime()-apt.getAppttime().getTime();
                long diff = Math.abs((int)TimeUnit.MILLISECONDS.toMinutes(diff1));
                if(diff>=15){
                  return true;
                }else{
                  throw new InvalidTimeDuration();
                }
            }
              
              return false;
          }else{
              return true;
          }
      }
      
      //Checking the time for booking the next appointment for same Phleobotomist at different center.
      //Retuerns true if the next appointment for Phleobotomist is after 45 minutes to the other PSC.
      //Else throws InvalidTimeInterval and  InvalidTimeDuration Exception
      public boolean checktimeinterval(Appointment a) throws InvalidTimeDuration, InvalidTimeInterval{
          Boolean flag = false;
          String querystr;
          querystr = "phlebid='";
          querystr = querystr.concat(a.getPhlebid().getId());
          querystr = querystr.concat("' AND apptdate='");
          querystr = querystr.concat(a.getApptdate().toString());
          querystr = querystr.concat("' AND id <> '"+a.getId().toString()+"'");
          objs = db.getData("Appointment",querystr);
          if(objs.size()>0){
            for(Object obj:objs){
                Appointment apt = (Appointment)obj;
                if(apt.getPscid().getId()!=a.getPscid().getId()){
                    long diff1 = a.getAppttime().getTime()-apt.getAppttime().getTime();
                    long diff = Math.abs((int)TimeUnit.MILLISECONDS.toMinutes(diff1));
                    if(diff>=45){
                      flag=true;
                    }else{
                      throw new InvalidTimeInterval();
                    }
                }else{
                    flag=this.checkduration(a);
                }
            }
          }else{
              flag=true;
          }
          return flag;
      }
      
      //Get the list of all the appointments. Returns List of Appointment Object
      public List<Appointment> getallappointments()
      {
            List<Object> objs = db.getData("Appointment", "");
            List<Appointment> apl = new ArrayList<Appointment>();
            for(Object obj:objs){
                Appointment apt = (Appointment)obj; 
                apl.add(apt);
            }
            return apl;
      }
      
     //Get the list of all the appointments. Returns List of Appointment Object. If Object not 
      // found then throws InvalidAppointment Exception.
      public List<Appointment> getappointment(String aptid) throws InvalidAppointment
      {
            String querystr;
            querystr = "id='";
            querystr = querystr.concat(aptid);
            querystr = querystr.concat("'");
            List<Object> objs = db.getData("Appointment", querystr);
            List<Appointment> apt=new ArrayList<Appointment>();
            if(objs.size()==1){
                for(Object obj:objs){
                    apt.add((Appointment)obj);
                }
                return apt;
            }else{
                throw new InvalidAppointment();
            }
      }
      
      //Gets the Patient Object on the id supplied to it. Else returns null. 
      public Patient getpatient(String ptid){
            String querystr;
            querystr = "id='";
            querystr = querystr.concat(ptid);
            querystr = querystr.concat("'");
            List<Object> objs = db.getData("Patient", querystr);
            Patient pt=new Patient();
            if(objs.size()==1){
                for(Object obj:objs){
                    pt = (Patient)obj;
                }
                return pt;
            }else{
                return null;
            }
      }
      
       //Check whether the appointment is been added to the DB or not.
      public Boolean checkAppointment(Appointment a) throws InvalidTime, InvalidTimeDuration, InvalidTimeInterval{
          Boolean flag = false;
          if(this.checktime(a)){
              if(this.checkduration(a)){
                  if(this.checktimeinterval(a)){
                      flag = db.addData(a);
                  }
              }
          }
          return flag;
      }
      
      //Check whether the appointment is been updated to the DB or not.
      public Boolean updateAppointment(Appointment a) throws InvalidTime, InvalidTimeDuration, InvalidTimeInterval{
          Boolean flag = false;
          System.out.println("Inside update");
          if(this.checktime(a)){
              if(this.checkduration(a)){
                  if(this.checktimeinterval(a)){
                      System.out.println("Inside update 1");
                      flag = db.updateData(a);
                  }
              }
          }
          return flag;
      }
}
