package ru.sshibko.AccountsManager.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication response with token")
public class AuthResponse {

    @Schema(description = "Token", example = """
            eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
            .бeyJzdWIiOiJ1c2VyNzg5IiwibmFtZSI6Ik
            saWNlIFNtaXRoIiwicm9sZSI6ImVkaXRvciI
            sImlhdCI6MTczMTAwMDAwMCwiZXhwIjoxNzM
            xMDAzNjAwLCJlbWFpbCI6ImFsaWNlLnNtaXR
            oQGV4YW1wbGU3kLHgqIm.6eE6f5rYvZ3f1Z5
            f4q8v7m4k5n6p8r9s2t3u4vk********
            """)
    private String token;
}
