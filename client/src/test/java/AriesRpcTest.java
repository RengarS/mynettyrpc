
import com.aries.client.consts.ThreadPool;
import com.aries.client.domain.RpcRequest;
import com.aries.client.domain.RpcResponse;
import com.aries.client.utils.AriesRpc;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class AriesRpcTest {
    public static void main(String[] args) throws Exception {
        /**
         * 机器：AlienWare 13 i7 6500U ，同时跑server和client，发送10000个请求耗时12秒，QPS 800左右
         */
        AriesRpc ariesRpc = new AriesRpc("127.0.0.1", 8888);
        Thread.sleep(1000);
        CountDownLatch latch = new CountDownLatch(10000);
        long before = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {

            ThreadPool.submit(() -> {
                String content = UUID.randomUUID().toString();
                try {
                    String response = ariesRpc.requestSync(new RpcRequest(content, content, "getPerson"));
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


        }

        latch.await();
        long after = System.currentTimeMillis();
        System.out.println(after - before + "   end");
    }
}