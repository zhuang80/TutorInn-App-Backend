package com.wequan.bu.controller;

import com.wequan.bu.config.handler.MessageHandler;
import com.wequan.bu.config.properties.AppProperties;
import com.wequan.bu.controller.vo.LoginSignUp;
import com.wequan.bu.controller.vo.result.Result;
import com.wequan.bu.controller.vo.result.ResultGenerator;
import com.wequan.bu.repository.dao.UserMapper;
import com.wequan.bu.repository.model.User;
import com.wequan.bu.security.authentication.token.EmailPasswordAuthenticationToken;
import com.wequan.bu.security.component.TokenProvider;
import com.wequan.bu.security.oauth2.user.AuthProvider;
import com.wequan.bu.service.UserService;
import com.wequan.bu.util.GeneralTool;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.Objects;

/**
 * @author ChrisChen
 */
@RestController
@Api(tags = "User Login")
public class UserLoginController {

    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AppProperties appProperties;

    @PostMapping("/user/register")
    @ApiOperation(value = "user register", notes = "返回注册信息")
    @ApiModelProperty(value = "user", notes = "用户信息的json串")
    public Result register(@RequestBody LoginSignUp loginSignUp) {
        String userName = loginSignUp.getUserName();
        String email = loginSignUp.getEmail();
        String password = loginSignUp.getPassword();
        //check parameters
        String msg;
        if (!GeneralTool.checkUsername(userName)) {
            msg = messageHandler.getMessage("40001", "User Name");
            return ResultGenerator.fail(msg);
        }
        if (!GeneralTool.checkEmail(email)) {
            msg = messageHandler.getMessage("40001", "Email");
            return ResultGenerator.fail(msg);
        }
        if (!GeneralTool.checkPassword(password)) {
            msg = messageHandler.getMessage("40001", "Password");
            return ResultGenerator.fail(msg);
        }
        //check email registered
        if (userService.checkEmailRegistered(email)) {
            msg = messageHandler.getMessage("40002", email);
            return ResultGenerator.fail(msg);
        }
        //check username registered
        if (userService.checkUerNameRegistered(userName)) {
            msg = messageHandler.getMessage("40002", email);
            return ResultGenerator.fail(msg);
        }
        //add user info
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setProvider(AuthProvider.LOCAL.toString());
        user.setCredential(passwordEncoder.encode(password));
        user.setCreateTime(new Date());
        boolean success = userService.registerUser(user);
        if (success) {
            //send confirm email
            userService.sendConfirmEmail(email, userName);
            return ResultGenerator.success();
        } else {
            return ResultGenerator.fail(500, messageHandler.getMessage("500"));
        }
    }

    @PostMapping("/user/login")
    @ApiOperation(value = "user login", notes = "返回登录信息")
    public ResponseEntity<Result> login(@RequestBody LoginSignUp loginSignUp) {
        String userName = loginSignUp.getUserName();
        String email = loginSignUp.getEmail();
        Authentication authentication = null;
        if (Objects.nonNull(userName)) {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, loginSignUp.getPassword())
            );
        }
        if (Objects.nonNull(email)) {
            authentication = authenticationManager.authenticate(
                    new EmailPasswordAuthenticationToken(email, loginSignUp.getPassword())
            );
        }
        if (Objects.isNull(authentication)) {
            return ResponseEntity.ok().body(ResultGenerator.fail("认证失败"));
        } else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.createToken(authentication);
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setBearerAuth(token);
            return ResponseEntity.ok().headers(respHeaders).body(ResultGenerator.success());
        }
    }

    @GetMapping("/user/e-confirm")
    @ApiOperation(value = "verify user", notes = "返回验证注册用户信息")
    public Result emailConfirm(@RequestParam("token") String token) {
        Result result;
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Claims claims = Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody();
            String subject = claims.getSubject();
            String email = subject.split("\\|\\|")[0];
            userService.confirmEmail(email);
            result = ResultGenerator.success();
        } else {
            result = ResultGenerator.fail(messageHandler.getMessage("40099"));
        }
        return result;
    }

    @PostMapping("/user/reset-password")
    @ApiOperation(value = "reset password", notes = "返回重置用户密码信息")
    public String resetPassword() {
        return "";
    }

    @GetMapping("/users")
    @ApiOperation(value = "a list of users", notes = "返回用户列表")
    public String getAll() {
        StringBuilder result = new StringBuilder();
        userService.findAll().stream().map(User::getEmail).forEach(result::append);
        return result.toString();
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> registerUser(@RequestBody LoginSignUp loginSignUp) {
        // Creating user's account
        User user = new User();
        int result = userMapper.insertSelective(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body(ResultGenerator.success("User registered successfully"));
    }
}
