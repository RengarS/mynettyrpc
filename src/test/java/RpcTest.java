

import com.aries.mynettyrpc.domain.RpcRequest1;
import com.aries.mynettyrpc.domain.RpcResponse1;
import com.aries.mynettyrpc.rpcclient.RpcClient;
import com.aries.mynettyrpc.utils.AriesRpc;

import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author Aries
 */
public class RpcTest {
    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                new RpcClient().connect("127.0.0.1", 8888);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000);
        for (int i = 1; i < 10; i++) {
            String id = UUID.randomUUID().toString();
            RpcResponse1 response = AriesRpc.requestSync(new RpcRequest1(id, id));
            System.out.println(response.getResponseId() + "  " + response.getResponseData());
        }
        String idd = UUID.randomUUID().toString();
        Future<RpcResponse1> future = AriesRpc.requestAsync(new RpcRequest1(idd, idd));
        RpcResponse1 response = future.get();
    }
}
