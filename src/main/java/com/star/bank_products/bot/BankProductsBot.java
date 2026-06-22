package com.star.bank_products.bot;

import com.star.bank_products.service.TelegramBotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BankProductsBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final TelegramBotService botService;

    public BankProductsBot(TelegramBotService botService) {
        super();
        this.botService = botService;
    }

    @Override
    public String getBotUsername() {
        return "bankstarskypro_bot"; // Имя бота
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            botService.handleMessage(update.getMessage());
        }
    }

    // Вспомогательный метод для реальной отправки ответа
    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}