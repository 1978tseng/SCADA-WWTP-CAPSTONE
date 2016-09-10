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
 * Created by Chun-Wei Tseng on 2015/6/11.
 */
public class PLCReadingControler extends Thread {

        private static final String DEBUGTAG = "PLCReadingControler";

        public PLCReadingControler() {

        }

        @Override
        public void run() {
            TCPMasterConnection plcConn = null; //the connection
            String outStr = "";

            try {
                //Get user settings
                InetAddress ardAddress = InetAddress.getByName(Constant.PLC_IP); //The device's address
                int port = Constant.DEFAULTPORT; //The device's port

                //Connect to device
                writeLog("Connecting to PLC ...");
                plcConn = new TCPMasterConnection(ardAddress);
                plcConn.setPort(port);
                plcConn.connect();
                writeLog("Reading from PLC ...");

                while (!isInterrupted()) {
                    Thread.sleep(100);
                    int valve = 0;
                    int pump = 0;

                    Register[] regs;
                    int startingAddr = 0;
                    int numRegs = 4;

                    //init  Modbus request
                    ReadMultipleRegistersRequest readReq = new ReadMultipleRegistersRequest(startingAddr, numRegs);

//                    Log.d(DEBUGTAG, "\nRequest: \nModbus request bytes (hex): \n" + readReq.getHexMessage());

                    //Setup the Modbus request
                    ModbusTCPTransaction trans = new ModbusTCPTransaction(plcConn);
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
//                    Log.d(DEBUGTAG, readRes.getHexMessage());

                    for (int address = 0; address < numRegs; address++) {
                     if (address == Constant.VALVE) {
                        valve = regs[address].getValue();
                        PLC.VALVE = valve;
                        outStr += "Valve : " + valve + ",";
                    } else if (address == Constant.PUMP) {
                        pump = regs[address].getValue();
                        PLC.PUMP = pump;
                        outStr += "PUMP : " + pump;
                    }

                    }
                    writeLog(outStr + "\nPLC Reading Done.\n");
                }
            } catch (ModbusSlaveException me) {
                outStr = "Modbus response exception " + me.getType() + ".";
            } catch (Exception e) {
                outStr = "Error Detected:\n" + e.toString();
            } finally {
                try {
                    if (plcConn != null) {
                        //Close connection
                        plcConn.close();
                    }
                } catch (Exception e) {

                }
            }


        }

        public void writeLog(String msg) {
//            Log.e(DEBUGTAG,msg);
        }



    }



