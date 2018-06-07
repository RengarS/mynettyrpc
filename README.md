# mynettyrpc

基于netty构建的rpc框架。

实现原理：</br>
        1.发出经过封装的request请求，请求包括请求id以及请求体，service端经过处理之后将结果封装成一个response响应。response中</br>
        封装了request的id。也就是说request id对应response id。通过id来匹配request和response。使用id匹配来解决tcp无状态的问题。</br>
        因为netty是无状态的，不像http一样一个request对应一个response。</br>
        2.客户端将收到的结果封装进一个ConcurrentHashMap之中，key是request id，value则是service端返回的响应。轮询map，如果map.get（requstId）
        不等于空，则返回。</br>
        3.框架有两种请求方式：同步请求和异步请求。同步请求会阻塞，直到有response返回。异步请求则直接返回一个Future<RpcResponse>对象，
        用户通过future.get（）来获取响应值。</br>
 
 
总的流程就是：</br>
          1.客户端发送经过封装的request（包含request id和请求体）</br>
          2.服务端接受到之后会打开request，取出request体之后进行处理</br>
          3.服务端经过处理之后，将返回值封装成一个response对象（request id->response id）</br>
          4.服务端返回给客户端response</br>
          5.客户端接受到response后将response id和response 体放进一个ConcurrentHashMap中</br>
          6.调用者轮询map，如果map.get（request id）不为空，则返回</br>
 </br>         
 </br>         
我不清楚其他的rpc框架是怎样实现request和response匹配的，但是这是我自己想出来的方法。可能和其他的rpc框架相比起来实现的很简陋，但是总归是一种解决方案.</br>

</br>
使用tcp协议做RPC的优势：</br>
        1.避免了http新建和销毁连接带来的性能开销</br>
        2.http协议占用了很多额外内存和带宽（比如请求头）</br>
        3.可以使用自定义序列化协议，比如protocol buffers，大幅降低内存、带宽和时间</br>
        </br>
        </br>
下一步的优化：</br>
        可以借鉴Jedis操作redis的思路创建一个链接池，实现半双工，方便匹配res 和req</br>
        </br>
        </br>
2017-11-10：</br>
        重构了整个项目，拆分成client和server，可以单独构建。</br>
        修改了AriesRpc.java，使其可以作为一个单独的bean注入。</br>
        大量修改了client端的结果匹配逻辑。使用synchronized和notify()、wait()来阻塞和唤醒线程。避免了自旋带来的性能损失(虽然是使用synchronized，但         是由于锁的是局部变量，锁状态仅是偏向锁，不影响吞吐量等)。</br>
        
        </br></br>
        
        
2017-11-11:</br>
        项目阶段性完工，通过初步测试。随后将会逐步封装，使其更容易使用</br>
        随后会逐步重构项目，测试中留下的各种sout等都会清除</br>


2017-11-20:</br> 
        好几天没commit了，这段时间有点松懈了
 
 
 
1.类似Spring、Spring MVC的web框架。具有Aop和IoC功能         (Done，待重构)
2.基于Netty的分布式Web框架，包括Client和Server端。           (Done，待重构)
3.基于Netty的服务发现治理中心，包括Client和Server端。         (Doing...)
4.类似Spring Session的分布式框架的Session一致化解决方案。     (TODO)
5.基于Hash算法的分布式Redis解决方案。                        (TODO)


模块说明：
    client是RPC 的客户端，用于服务消费者和生产者的通信
    registerclient 提供RPC服务消费者注册到注册中心的功能
    registerserver 注册中心，同时提供负载均衡功能（随机算法）
    server rpc消费者服务容器
    
阅读源码指引：
    阅读client模块最好先阅读 com.aries.client.utils.AriesRpc.java
    阅读server模块最好先阅读 com.aries.server.AriesRpcFramework.java