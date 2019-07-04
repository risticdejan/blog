package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.domain.Contact;
import com.dejanristic.blog.domain.form.ContactForm;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.service.ContactService;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String store(
            @Valid @ModelAttribute(AttributeNames.NEW_CONTACT) ContactForm formData,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult."
                    + AttributeNames.NEW_CONTACT,
                    result
            );
            redirectAttributes.addFlashAttribute(AttributeNames.NEW_CONTACT, formData);

            return UrlMappings.REDIRECT_CONTACT;
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

        return UrlMappings.REDIRECT_HOME;
    }
}
