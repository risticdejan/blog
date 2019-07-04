package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.Contact;
import com.dejanristic.blog.domain.form.ContactForm;
import com.dejanristic.blog.domain.model.JsonRespone;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.ContactService;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContactController {

    private final ContactService contactService;
    private final CategoryService categoryService;
    private final Flash flash;

    @Autowired
    public ContactController(
            ContactService contactService,
            CategoryService categoryService,
            Flash flash
    ) {
        this.contactService = contactService;
        this.categoryService = categoryService;
        this.flash = flash;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping(UrlMappings.CONTACT)
    public String index(
            Model model
    ) {
        if (!model.containsAttribute(AttributeNames.NEW_CONTACT)) {
            model.addAttribute(AttributeNames.NEW_CONTACT, new ContactForm());
        }
        return ViewNames.CONTACT;
    }

    @PostMapping(UrlMappings.CONTACT_STORE)
    @ResponseBody
    public ResponseEntity<?> store(
            @Valid @ModelAttribute(AttributeNames.NEW_CONTACT) ContactForm formData,
            BindingResult result,
            HttpServletRequest request,
            Model model
    ) {
        String path = request.getContextPath();

        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap();
            for (FieldError fe : result.getFieldErrors()) {
                if (!errors.containsKey(fe.getField())) {
                    errors.put(fe.getField(), fe.getDefaultMessage());
                }
            }

            return new ResponseEntity(
                    new JsonRespone("failed", errors),
                    HttpStatus.OK
            );
        }

        Contact contact = new Contact(
                formData.getName(),
                formData.getBody(),
                formData.getEmail(),
                formData.getPhone()
        );

        contact = this.contactService.create(contact);

        if (contact != null) {
            flash.success("The message has been sent, "
                    + "as soon as possible we will respond");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        Map<String, Object> data = new HashMap();

        data.put("url", path + UrlMappings.HOME);

        return new ResponseEntity(
                new JsonRespone("success", data),
                HttpStatus.OK
        );
    }
}
