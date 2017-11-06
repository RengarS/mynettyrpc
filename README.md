# mynettyrpc

基于netty构建的rpc框架。

实现原理：
        1.发出经过封装的request请求，请求包括请求id以及请求体，service端经过处理之后将结果封装成一个response响应。response中
        封装了request的id。也就是说request id对应response id。通过id来匹配request和response。使用id匹配来解决tcp无状态的问题。
        因为netty是无状态的，不像http一样一个request对应一个response。
        2.客户端将收到的结果封装进一个ConcurrentHashMap之中，key是request id，value则是service端返回的响应。轮询map，如果map.get（requstId）
        不等于空，则返回。
        3.框架有两种请求方式：同步请求和异步请求。同步请求会阻塞，直到有response返回。异步请求则直接返回一个Future<RpcResponse>对象，
        用户通过future.get（）来获取响应值。
 
 
总的流程就是：
          1.客户端发送经过封装的request（包含request id和请求体）
          2.服务端接受到之后会打开request，取出request体之后进行处理
          3.服务端经过处理之后，将返回值封装成一个response对象（request id->response id）
          4.服务端返回给客户端response
          5.客户端接受到response后将response id和response 体放进一个ConcurrentHashMap中
          6.调用者轮询map，如果map.get（request id）不为空，则返回
          
          
我不清楚其他的rpc框架是怎样实现request和response匹配的，但是这是我自己想出来的方法。可能和其他的rpc框架相比起来实现的很简陋，但是总归是一种解决方案.
