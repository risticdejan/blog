package com.dejanristic.blog.controller.admin;

import com.dejanristic.blog.annotation.PerPageAdmin;
import com.dejanristic.blog.domain.Contact;
import com.dejanristic.blog.service.ContactService;
import com.dejanristic.blog.service.Flash;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.SecurityUtility;
import com.dejanristic.blog.util.admin.UrlAdminMappings;
import com.dejanristic.blog.util.admin.ViewAdminNames;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller("admin.contact")
public class ContactController {

    @Autowired
    @PerPageAdmin
    private int perPage;

    private final ContactService contactService;
    private final Flash flash;

    public ContactController(ContactService contactService, Flash flash) {
        this.contactService = contactService;
        this.flash = flash;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_CONTACT_LIST)
    public String index(
            @RequestParam(required = false) String page,
            Model model
    ) {
        int cleanPage = SecurityUtility.cleanPageParam(page);

        Page<Contact> contacts
                = contactService.findAll(
                        PageRequest.of(cleanPage, perPage, Sort.by("createdAt").descending())
                );

        model.addAttribute("contacts", contacts);

        return ViewAdminNames.ADMIN_CONTACT_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_CONTACT_SHOW + "/{id}")
    public String show(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Contact contact = contactService.findById(cleanId);

        contactService.view(contact);

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN;

        model.addAttribute("contact", contact);
        model.addAttribute(AttributeNames.BACK_URL, backUrl);

        return ViewAdminNames.ADMIN_CONTACT_SHOW;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(UrlAdminMappings.ADMIN_CONTACT_DELETE + "/{id}")
    public String delete(
            @PathVariable("id") String id,
            HttpServletRequest request,
            Model model
    ) {
        Long cleanId = SecurityUtility.cleanIdParam(id);

        Contact contact = contactService.findById(cleanId);

        if (contact != null) {
            contactService.delete(contact);

            flash.success("The contact was deleted");
        } else {
            flash.error("Unfortunately, there was a problem, "
                    + "please try again later");
        }

        String backUrl = (request.getHeader("Referer") != null)
                ? request.getHeader("Referer")
                : UrlAdminMappings.ADMIN;

        return "redirect:" + backUrl;
    }
}
