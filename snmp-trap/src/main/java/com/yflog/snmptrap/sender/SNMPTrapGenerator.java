package com.yflog.snmptrap.sender;

import org.snmp4j.*;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;

import java.io.IOException;

/**
 * Created by vincent on 1/19/16.
 */
public class SNMPTrapGenerator {
    private static final String community = "public";
    private static final String trapOid = ".1.3.6.1.2.1.1.6";
    private static final String ipAddress = "127.0.0.1";
    private static final int port = 162;

    public static void main(String[] args) {
        sendSnmpV1V2Trap(SnmpConstants.version1);
        sendSnmpV1V2Trap(SnmpConstants.version2c);
        sendSnmpV3Trap();
    }

    private static void sendSnmpV1V2Trap(int version) {
        sendV1orV2Trap(version, community, ipAddress, port);
    }

    private static void sendV1orV2Trap(int version, String community, String ipAddress, int port) {
        try {
            PDU snmpPDU = createPDU(version);
            // create Transport Mapping
            TransportMapping<?> transport = new DefaultTcpTransportMapping();
            transport.listen();

            // create Target
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setVersion(version);
            target.setAddress(new UdpAddress(ipAddress + "/" + port));
            target.setRetries(2);
            target.setTimeout(5000);

            // send the PDU
            Snmp snmp = new Snmp(transport);
            snmp.send(snmpPDU, target);

            System.out.println("Send trap to (IP:PORT) =>" + ipAddress + ":" + port);

            snmp.close();
        }
        catch (IOException e) {
            System.err.println("Error in Sending Trap to (IP:Port)=> "
                    + ipAddress + ":" + port);
            System.err.println("Exception Message = " + e.getMessage());
        }
    }

    private static void sendSnmpV3Trap() {
        try {
            Address targetAddress = GenericAddress.parse("udp:" + ipAddress + "/" + port);
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            USM usm = new USM(SecurityProtocols.getInstance().addDefaultProtocols(),
                    new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            transport.listen();

            snmp.getUSM().addUser(
                    new OctetString("MD5DES"),
                    new UsmUser(new OctetString("MD5DES"), AuthMD5.ID,
                            new OctetString("UserName"), PrivAES128.ID,
                            new OctetString("UserName")));

            // create target
            UserTarget target = new UserTarget();
            target.setAddress(targetAddress);
            target.setRetries(1);
            target.setTimeout(11500);
            target.setVersion(SnmpConstants.version3);
            target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            target.setSecurityName(new OctetString("MD5DES"));

            // Create PDU for v3
            ScopedPDU pdu = new ScopedPDU();
            pdu.setType(ScopedPDU.NOTIFICATION);
            pdu.add(new VariableBinding(SnmpConstants.sysUpTime));
            pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants.linkDown));
            pdu.add(new VariableBinding(new OID(trapOid), new OctetString("Major")));

            // send the PDU
            snmp.send(pdu, target);
            System.out.println("Sending Trap to (IP:Port) => " + ipAddress + ":" + port);

            snmp.addCommandResponder(new CommandResponder() {
                public void processPdu(CommandResponderEvent commandResponderEvent) {
                    System.out.println(commandResponderEvent);
                }
            });

            snmp.close();
        }
        catch (IOException e) {
            System.err.println("Error in Sending Trap to (IP:Port)=> "
                    + ipAddress + ":" + port);
            System.err.println("Exception Message = " + e.getMessage());

        }
    }

    private static PDU createPDU(int version) {
        PDU snmpPud = DefaultPDUFactory.createPDU(version);
        if (version == SnmpConstants.version1) {
            snmpPud.setType(PDU.V1TRAP);
        }
        else {
            snmpPud.setType(PDU.TRAP);
        }

        snmpPud.add(new VariableBinding(SnmpConstants.sysUpTime));
        snmpPud.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(trapOid)));
        snmpPud.add(new VariableBinding(SnmpConstants.snmpTrapAddress,
                new IpAddress(ipAddress)));
        snmpPud.add(new VariableBinding(new OID(trapOid), new OctetString("Major")));
        return snmpPud;
    }
}
