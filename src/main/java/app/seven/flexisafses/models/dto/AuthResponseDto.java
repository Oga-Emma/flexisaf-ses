package app.seven.flexisafses.models.dto;

import app.seven.flexisafses.models.pojo.AppUser;

public class AuthResponseDto {
    AppUser user;
    String token;
    String refreshToken;
}
