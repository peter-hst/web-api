package hst.shengtao.web.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Payload<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间戳
     */
    private Long timestamp;

    /**
     * 成功响应
     */
    public static <T> Payload<T> success() {
        return success(null);
    }

    public static <T> Payload<T> success(T data) {
        return Payload.<T>builder()
                .code(0)
                .msg("success")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 失败响应
     */
    public static <T> Payload<T> fail(String msg) {
        return Payload.<T>builder()
                .code(500)
                .msg(msg)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Payload<T> fail(Integer code, String msg) {
        return Payload.<T>builder()
                .code(code)
                .msg(msg)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}