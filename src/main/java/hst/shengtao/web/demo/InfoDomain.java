package hst.shengtao.web.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoDomain implements Serializable {
    private String hostname;
    private String requesterIP;
    private String sessionID;
    private String appVersion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private LocalDateTime now;
}
