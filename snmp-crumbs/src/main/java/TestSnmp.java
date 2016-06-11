import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.security.nonstandard.PrivAES256With3DESKeyExtension;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by root on 5/18/16.
 */
public class TestSnmp {

    public static void mai2n(String[] args) {
        OctetString st = new OctetString("L0g1$@cm0n");
        System.out.println(st.toString());
        System.out.println(st.toHexString());
        System.out.println(Arrays.toString(st.toByteArray()));
    }

    public static void main(String[] args) throws IOException {

        Address targetAddress = GenericAddress.parse("udp:10.130.128.2/161");
        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);

        USM usm = new USM(SecurityProtocols.getInstance(),
                new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);
        transport.listen();

        snmp.getUSM().addUser(new OctetString("logicmonitor"),
                new UsmUser(new OctetString("logicmonitor"),
                        AuthSHA.ID,
                        new OctetString("$@logicmon"),
                        PrivAES256With3DESKeyExtension.ID,
//                        PrivAES256.ID, //PrivDES.ID,
                        new OctetString("$@logicmon")));
        // create the target
        UserTarget target = new UserTarget();
        target.setAddress(targetAddress);
        target.setRetries(1);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version3);
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString("logicmonitor"));

        // create the PDU
        PDU pdu = new ScopedPDU();
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.3.0")));
        pdu.setType(PDU.GETNEXT);

        // send the PDU
        ResponseEvent response = snmp.send(pdu, target);
        // extract the response PDU (could be null if timed out)
        PDU responsePDU = response.getResponse();
        // extract the address used by the agent to send the response:
        Address peerAddress = response.getPeerAddress();

        System.out.println();
        System.out.println(response.getResponse().toString());

    }
}
