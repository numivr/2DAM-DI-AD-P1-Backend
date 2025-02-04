package org.example.diadp1backend.DTOs;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {

  //No le pondremos ni el rol ni el id
  private String usuario;
  private String password;
  private String email;
}
