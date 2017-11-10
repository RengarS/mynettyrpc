import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufTest {
    public static void main(String[] args) {
        byte[] bytes = "$$_".getBytes();
        for (byte b : bytes) {
            System.out.print(b);
        }

        System.out.println();

        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes("aries".getBytes());
        byte[] bytes1 = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes1);
        for (byte b : bytes1) {
            System.out.print(b);
        }
    }
}
