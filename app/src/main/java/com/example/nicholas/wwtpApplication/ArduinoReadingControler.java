package com.example.nicholas.wwtpApplication;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;

import java.net.InetAddress;


/**
 * Created by Chun-Wei Tseng on 2015/6/8.
 */
public class ArduinoReadingControler extends Thread {

    private static final String DEBUGTAG = "ArduinoReadingControler";

    public ArduinoReadingControler() {

    }

    @Override
    public void run() {
        TCPMasterConnection ardConn = null; //the connection
        String outStr = "";

        try {
            //Get user settings
            InetAddress ardAddress = InetAddress.getByName(Constant.ANDROID_IP); //The device's address
            int port = Constant.DEFAULTPORT; //The device's port

            //Connect to device
            writeLog("Connecting to Arduino ...");
            ardConn = new TCPMasterConnection(ardAddress);
            ardConn.setPort(port);
            ardConn.connect();
            writeLog("Reading from Arduino ...");

            while (!isInterrupted()) {
                Thread.sleep(100);
                int flow = 0;
                int ph = 0;
                int orp = 0;
                int doVal = 0;
                int waterLvl = 0;
                int valve = 0;
                int pump = 0;

                Register[] regs;
                int startingAddr = 0;
                int numRegs = 23;

                //init  Modbus request
                ReadMultipleRegistersRequest readReq = new ReadMultipleRegistersRequest(startingAddr, numRegs);

//                Log.d(DEBUGTAG, "\nRequest: \nModbus request bytes (hex): \n" + readReq.getHexMessage());

                //Setup the Modbus request
                ModbusTCPTransaction trans = new ModbusTCPTransaction(ardConn);
                trans.setRequest(readReq);

                //Send/Receive the Modbus request/response
                try {
                    trans.execute();
                } catch (ModbusException e) {
                    e.printStackTrace();
                }

                //Get the Modbus response data
                ReadMultipleRegistersResponse readRes = (ReadMultipleRegistersResponse) trans.getResponse();
                //Convert registers to bytes
                regs = readRes.getRegisters();
//                Log.d(DEBUGTAG, readRes.getHexMessage());

                for (int address = 0; address < numRegs; address++) {
                    if (address == Constant.PH) {
                        ph = regs[address].getValue();
                        Ardruino.PH = ph;
                        outStr += "PH : " + ph + ",";
                    } else if (address == Constant.ORP) {
                        orp = regs[address].getValue();
                        Ardruino.ORP = orp;
                        outStr += "ORP : " + orp + ",";
                    } else if (address == Constant.DO) {
                        doVal = regs[address].getValue();
                        Ardruino.DO = doVal;
                        outStr += "DO : " + doVal + ",";
                    } else if (address == Constant.FLOW) {
                        flow = regs[address].getValue();
                        Ardruino.FLOW = flow;
                        outStr += "FLOW : " + flow + ",";
                    } else if (address == Constant.WATERLEVEL) {
                        waterLvl = regs[address].getValue();
                        Ardruino.WATERLEVEL = waterLvl;
                        outStr += "Water Level : " + waterLvl + ",";
                    }
//                    else if (address == Constant.VALVE) {
//                        valve = regs[address].getValue();
//                        Ardruino.VALVE = valve;
//                        outStr += "Valve : " + valve + ",";
//                    } else if (address == Constant.PUMP) {
//                        pump = regs[address].getValue();
//                        Ardruino.PUMP = pump;
//                        outStr += "PUMP : " + pump;
//                    }

                }
                writeLog(outStr + "\nArduino Reading Done.\n");

            }
        } catch (ModbusSlaveException me) {
            outStr = "Modbus response exception " + me.getType() + ".";
        } catch (Exception e) {
            outStr = "Error Detected:\n" + e.toString();
        } finally {
            try {
                if (ardConn != null) {
                    //Close connection
                    ardConn.close();
                }
            } catch (Exception e) {

            }
        }


    }

    public void writeLog(String msg) {
//        Log.e(DEBUGTAG, msg);
    }


}


