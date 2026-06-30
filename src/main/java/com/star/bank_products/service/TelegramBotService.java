package com.star.bank_products.service;

import com.star.bank_products.bot.BankProductsBot;
import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.dto.RecommendationResponse;
import com.star.bank_products.model.UserEntity;
import com.star.bank_products.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final BankProductsBot bot;

    public TelegramBotService(RecommendationService recommendationService, UserRepository userRepository, @Lazy BankProductsBot bot) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.bot = bot;
    }

    //Этот метод выполняется асинхронно.
    @Async
    public void handleMessage(Message message) {
        String text = message.getText();
        long chatId = message.getChatId();

        // 1. Приветствие и справка при первом обращении или команде /start
        if (text.equals("/start") || text.equals("/help")) {
            sendReply(chatId, "Привет! Я бот банка Star.\n\n" +
                    "Я могу подобрать для тебя банковские продукты.\n" +
                    "Используй команду: /recommend <username>\n" +
                    "Пример: /recommend ivan_petrov");
            return;
        }

        // 2. Обработка команды /recommend
        if (text.startsWith("/recommend")) {
            String[] parts = text.split("\\s+");
            if (parts.length < 2) {
                sendReply(chatId, "Ошибка: Укажите имя пользователя.\nПример: /recommend alex_smith");
                return;
            }

            String requestedUsername = parts[1].trim();
            processRecommendationRequest(chatId, requestedUsername);
        } else {
            sendReply(chatId, "Я не понимаю эту команду. Напиши /help для справки.");
        }
    }

    private void processRecommendationRequest(long chatId, String username) {
        // Ищем пользователя в БД по username
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            sendReply(chatId, "Пользователь не найден.");
            return;
        }

        UserEntity user = userOpt.get();
        UUID userId = user.getId();

        try {
            // Вызываем твой существующий сервис рекомендаций
            RecommendationResponse response = (RecommendationResponse) recommendationService.getRecommendations(userId);
            List<RecommendationDto> recommendations = response.getRecommendations();

            if (recommendations.isEmpty()) {
                sendReply(chatId, "У нас пока нет персональных рекомендаций для этого пользователя.");
                return;
            }

            // Формируем ответ
            StringBuilder sb = new StringBuilder();
            sb.append("Здравствуйте ")
                    .append(user.getUsername())
                    .append("\n\n");

            sb.append("Новые продукты для вас:\n\n");
            for (int i = 0; i < recommendations.size(); i++) {
                RecommendationDto dto = recommendations.get(i);
                sb.append((i + 1)).append(". ").append(dto.getName()).append("\n");
                sb.append("   ").append(dto.getText()).append("\n\n");
            }

            sb.append("Не забудьте проконсультироваться с менеджером!");

            sendReply(chatId, sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            sendReply(chatId, "Произошла ошибка при получении рекомендаций. Попробуйте позже.");
        }
    }

    private void sendReply(long chatId, String text) {
        System.out.println("[TELEGRAM] Chat: " + chatId + " | Text: " + text);
        bot.sendMessage(chatId, text);
    }
}