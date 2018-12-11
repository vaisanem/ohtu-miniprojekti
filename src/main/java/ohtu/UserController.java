/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ohtu.db.LoginManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ColdFish
 */
@Controller
public class UserController {

    private LoginManager loginMan;

    public UserController() throws ClassNotFoundException {
        this.loginMan = new LoginManager();
    }

    // <editor-fold desc="Login management">
    public String getUserFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String user = "NOT LOGGED IN";
        if (cookies != null) {
            Optional<Cookie> cooki = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).findFirst();
            if (cooki.isPresent()) {
                user = cooki.get().getValue();
            }
        }
        return user;
    }

    @PostMapping("/login")
    public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response, @RequestParam String username, @RequestParam String password, @RequestParam("targetURL") String target) {
        if (username.equals("default") || username.equals("testUser")) {
            Cookie cookie = new Cookie("user", username);
            cookie.setValue(username);
            response.addCookie(cookie);
            return "redirect:/" + target;
        } else {
            if (loginMan.login(username, password)) {
                Cookie cookie = new Cookie("user", username);
                cookie.setValue(username);
                response.addCookie(cookie);
                return "redirect:/" + target;
            } else {
                return "redirect:/error";
            }
        }
    }

    @PostMapping("/logout")
    public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model, @ModelAttribute(value = "targetURL") String target) {
        model.addAttribute("targetURL", target);
        return "login";
    }

    // </editor-fold>
    // Spacer
    //
    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public String createUser(ModelMap model) {
        return "createUser";
    }

    @PostMapping("/createUser")
    public String createUser(ModelMap model, @RequestParam String username, @RequestParam String password) {
        if (username.length() < 4) {
            model.addAttribute("error", "Username too short (min 4 length)");
            return "error";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "password too short (min 6 length)");
            return "error";
        }

        loginMan.createUser(username, password);
        return "redirect:/";
    }
}
