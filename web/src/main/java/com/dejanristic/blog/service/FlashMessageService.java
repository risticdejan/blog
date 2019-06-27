package com.dejanristic.blog.service;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FlashMessageService {

    public void flash(
            String type,
            String message,
            RedirectAttributes redirectAttributes
    );

    public void errorWasHappend(RedirectAttributes redirectAttributes);

    public void articleNotFound(RedirectAttributes redirectAttributes);

    public void articleNotReleased(RedirectAttributes redirectAttributes);

    public void articleWasReleased(RedirectAttributes redirectAttributes);

    public void articleHasReleased(RedirectAttributes redirectAttributes);

    public void articleAlreadyExists(RedirectAttributes redirectAttributes);

    public void articleWasCreated(RedirectAttributes redirectAttributes);

    public void articleWasDeleted(RedirectAttributes redirectAttributes);

    public void articleWasUpdate(RedirectAttributes redirectAttributes);

    public void userWasCreated(RedirectAttributes redirectAttributes);
}
