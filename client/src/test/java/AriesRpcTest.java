import com.aries.client.domain.RpcRequest1;
import com.aries.client.domain.RpcResponse1;
import com.aries.client.utils.AriesRpc;

import java.util.UUID;

public class AriesRpcTest {
    public static void main(String[] args) throws Exception {
        AriesRpc ariesRpc = new AriesRpc();
        Thread.sleep(1000);
        for (int a = 0; a < 100; a++) {
            String s = UUID.randomUUID().toString();
            RpcResponse1 response1 = ariesRpc.requestSync(new RpcRequest1(s, s));
            // System.out.println(response1.getResponseId() + "  " + response1.getResponseData());
        }
    }
}
