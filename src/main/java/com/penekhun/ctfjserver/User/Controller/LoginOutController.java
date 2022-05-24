package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.penekhun.ctfjserver.Config.Jwt.JwtFilter.AUTHORIZATION_HEADER;
import static com.penekhun.ctfjserver.Config.Jwt.JwtFilter.REFRESH_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class LoginOutController {

    private final AccountService accountService;

    @PostMapping("login")
    public TokenDto loginMap
            (@RequestParam String username,
             @RequestParam String password){

        return accountService.login(username, password);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissueMap(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                     @RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken){
        return accountService.reissue(accessToken, refreshToken);
    }

    @GetMapping("logout")
    public ResponseEntity logoutMap(){
        return ResponseEntity.ok("logout");
    }

    @PostMapping("tempSignup")
    public AccountDto.Res.Signup postMapping(@Validated AccountDto.Req.Signup signup){
        //post Account == Signup
        log.info("post account");

        return accountService.signup(signup);
    }

}
