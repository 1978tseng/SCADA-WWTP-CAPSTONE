package com.example.nicholas.wwtpApplication;

import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by Chun-Wei Tseng on 2015/6/11.
 */
public class PLCWritingControler extends Thread {

    private static final String DEBUGTAG = "PLCWritingControler";

    public ArrayList<int[]> dataQueue = new ArrayList<int[]>();

    public PLCWritingControler() {

    }

    @Override
    public void run() {

        TCPMasterConnection pConn = null; //the connection
        try {
            //Get user settings
            InetAddress ipAddr = InetAddress.getByName(Constant.PLC_IP); //The device's address
            int port = Constant.DEFAULTPORT; //The device's port

            //Connect to device
            writeLog("Connecting to PLC ...");
            pConn = new TCPMasterConnection(ipAddr);
            pConn.setPort(port);
            pConn.connect();

            while (!isInterrupted()) {
                String outStr = "";

                if (dataQueue.size() == 0) {
                    continue;
                } else {
                    int[] value = dataQueue.remove(0);
                    //Convert bytes to registers
                    SimpleRegister reg = new SimpleRegister(value[0]);

                    //Setup the Modbus request
                    WriteSingleRegisterRequest writeReq = new WriteSingleRegisterRequest(value[1], reg);

                    outStr = "Modbus request bytes (hex): " + writeReq.getHexMessage();

                    ModbusTCPTransaction trans = new ModbusTCPTransaction(pConn);
                    trans.setRequest(writeReq);

                    //Send/Receive the Modbus request/response
                    trans.execute();

                    WriteSingleRegisterResponse writeRes = (WriteSingleRegisterResponse) trans.getResponse();

                    outStr += "\nModbus response bytes (hex): " + writeRes.getHexMessage();

                }

                writeLog(outStr + "\nPLC Writing Done.\n");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ModbusSlaveException me) {
            me.printStackTrace();
//                outStr = "Modbus response exception " + me.getType() + ".";
        } catch (Exception e) {
            e.printStackTrace();
//                outStr = "Error Detected:\n" + e.toString();
        } finally {
            if (pConn != null) {
                //Close connection
                pConn.close();
            }
        }
    }

    public void writeLog(String msg) {
//        Log.e(DEBUGTAG, msg);
    }


    public synchronized void requestWriting(int value, int address) {
        int[] tmp = new int[]{value, address};
        dataQueue.add(tmp);
    }

}

