package com.ibm.hybrid.cloud.sample.portfolio.controllers.local;

import javax.jms.JMSException;

import com.ibm.jms.JMSTextMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class LoyaltyMessagingTestListener {

    @JmsListener(destination = "${loyalty.queue.name}" )
    public void dumpMessage(Object msg){
        switch(msg.getClass().getName()){
            case "com.ibm.jms.JMSTextMessage": {
                JMSTextMessage t = (JMSTextMessage)msg;
                try{
                    System.out.println("JMSMSG: "+t.getText());
                }catch(JMSException e){
                    e.printStackTrace();
                }
                break;
            }
            default: {
                System.out.println("JMSClass: "+msg.getClass().getName());        
                System.out.println("JMSMSG: "+String.valueOf(msg));
            }
        }
    }
}