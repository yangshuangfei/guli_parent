package com.stitch.service.edu.controller;

import com.stitch.common.base.result.R;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@RequestMapping("/user")
public class LoginController {
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    @GetMapping("info")
    public R info(String token) {
        System.out.println(token);
        return R.ok().data("name", "admin")
                .data("roles","[admin]")
                .data("avatar","https://www.baidu.com/link?url=-23QZFZsW0Ut7ziqdAjveDqaG_uwEFQ7M1gqR-_tNR4pVfZtF7yNfOFsRy0FcMLld3JccEDyUq-xMoH7Ro_vz6tuj_05qa5LgZbKhBFgYuq&wd=&eqid=df1571f000019d62000000065fe48146");
    }

    @PostMapping("logout")
    public R logout() {
        return R.ok();
    }
}
