package com.demo3sp.controller;

import com.demo3sp.entiy.Product;
import com.demo3sp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class LoginController {
    @Autowired
    private ProductService productService;

    @PostMapping("/login_success_handler")
    public String loginSuccessHandler(Principal principal) {
        System.out.println("Login Success");
        System.out.println(principal.getName());
        System.out.println(principal.toString());
        return "index";
    }
    @PostMapping("/login_failure_handler")
    public String loginFailureHandler() {
        System.out.println("Login Failure");
        return "login";
    }
    @RequestMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listProducts", productService.getProducts());
        return "index";
    }
    @RequestMapping("/new")
    public String showNewProductForm(Model model, @ModelAttribute("product") Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User Authorities: " + authentication.getAuthorities());
        model.addAttribute("product", product);
        model.addAttribute("product", product);
//        return "index";
        return "new_product";

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("product") Product product) {
        productService.saveProduct(product);
        return "redirect:/";

    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_product");

        Product product = productService.getProduct(id);
        mav.addObject("product", product);

        return mav;

    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return "redirect:/";

    }
}
