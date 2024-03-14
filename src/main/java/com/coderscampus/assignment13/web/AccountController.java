package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/users/{userId}/accounts/new")
    public String getCreateAccount(ModelMap model, @PathVariable Long userId) {
        User user = userService.findById(userId);
        Account account = new Account();
        account.getUsers().add(user);
        user.getAccounts().add(account);
        account.setAccountName(user.getName() + "'s Account " + user.getAccounts().size());
        accountService.saveAccount(account);
        model.put("user", user);
        model.put("account", account);
        return "account";
    }

    @PostMapping("/users/{userId}/accounts/new")
    public String postCreateAccount(Account account, @PathVariable Long userId) {
        User user = userService.findById(userId);
        account.setAccountName(account.getAccountName());
        account.getUsers().add(user);
        user.getAccounts().add(account);
        accountService.saveAccount(account);
        userService.saveUser(user);
        return "redirect:/users/" + userId;
    }

    @GetMapping("/users/{userId}/accounts/{accountId}")
    public String getUpdateAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        model.put("account", account);
        return "account";
    }

}
