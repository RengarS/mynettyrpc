
import com.aries.client.consts.ThreadPool;
import com.aries.client.domain.RpcRequest;
import com.aries.client.domain.RpcResponse;
import com.aries.client.utils.AriesRpc;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AriesRpcTest {
    public static void main(String[] args) throws Exception {
        AtomicInteger integer = new AtomicInteger(0);
        AriesRpc ariesRpc = new AriesRpc();
        Thread.sleep(1000);
        for (int i = 0; i < 100; i++) {

            ThreadPool.submit(() -> {
                System.out.println(integer.getAndIncrement());
                String content = UUID.randomUUID().toString();
                try {
                    RpcResponse response1 = ariesRpc.requestSync(new RpcRequest(content, content, ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}