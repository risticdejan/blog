package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Message;
import com.dejanristic.blog.service.FlashMessageService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.FlashNames;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FlashMessageServiceImpl implements FlashMessageService {

    @Override
    public void flash(
            String type,
            String message,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute(
                AttributeNames.FLASH_MESSAGE,
                new Message(type, message)
        );
    }

    @Override
    public void articleNotFound(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, Article not found",
                redirectAttributes
        );
    }

    @Override
    public void articleNotReleased(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, Article not released",
                redirectAttributes
        );
    }

    @Override
    public void articleWasReleased(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, Article was released,"
                + " you cannot edit/update this article",
                redirectAttributes
        );
    }

    @Override
    public void articleHasReleased(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.SUCCESS_TYPE,
                "Article has released",
                redirectAttributes
        );
    }

    @Override
    public void errorWasHappend(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.ERROR_TYPE,
                "Unfortunately, there was a problem, "
                + "please try again later",
                redirectAttributes
        );
    }

    @Override
    public void articleAlreadyExists(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.ERROR_TYPE,
                "Article already exists",
                redirectAttributes
        );
    }

    @Override
    public void articleWasCreated(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.SUCCESS_TYPE,
                "The article was created, as soon as possible "
                + "it will be released",
                redirectAttributes
        );
    }

    @Override
    public void articleWasDeleted(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.SUCCESS_TYPE,
                "The article was deleted",
                redirectAttributes
        );
    }

    @Override
    public void articleWasUpdate(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.SUCCESS_TYPE,
                "The article was updated",
                redirectAttributes
        );
    }

    @Override
    public void userWasCreated(RedirectAttributes redirectAttributes) {
        flash(
                FlashNames.SUCCESS_TYPE,
                "Thank you, You are registered successfully",
                redirectAttributes
        );
    }

}
