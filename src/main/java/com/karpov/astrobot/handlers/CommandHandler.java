package com.karpov.astrobot.handlers;

import com.karpov.astrobot.keyboards.InlineKeyboards.MainMenuKeyboard;
import com.karpov.astrobot.keyboards.InlineKeyboards.SettingsKeyboard;
import com.karpov.astrobot.models.ChatLocation;
import com.karpov.astrobot.repo.ChatRepository;
import com.karpov.astrobot.services.CustomMessageService;
import com.karpov.astrobot.services.LocationService;
import com.karpov.astrobot.services.RegisterChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

@Component
@Slf4j
public class CommandHandler {

	private final RegisterChatService registerChatService;
	private final MainMenuKeyboard mainMenuKeyboard;
	private final SettingsKeyboard settingsKeyboard;
	private final LocationService locationService;
	private final CustomMessageService customMessageService;
	private final ChatRepository chatRepository;

	public CommandHandler(RegisterChatService registerChatService,
	                      MainMenuKeyboard mainMenuKeyboard,
	                      SettingsKeyboard settingsKeyboard,
	                      LocationService locationService,
	                      CustomMessageService deleteMessageService, ChatRepository chatRepository) {
		this.registerChatService = registerChatService;
		this.mainMenuKeyboard = mainMenuKeyboard;
		this.settingsKeyboard = settingsKeyboard;
		this.locationService = locationService;
		this.customMessageService = deleteMessageService;
		this.chatRepository = chatRepository;
	}

	public SendMessage handleCommand(Update update, String command) {
		SendMessage sendMessage = new SendMessage();
		Long chatId = update.getMessage().getChatId();
		Integer receivedMessageId = update.getMessage().getMessageId();
		sendMessage.setChatId(chatId);
		sendMessage.setParseMode("HTML");

		switch (command) {
			case ("/start") -> {
				log.trace("/start command received: chatId={}, update={}", chatId, update);
				registerChatService.registerChat(update);
				sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nMain Menu");
				sendMessage.setReplyMarkup(mainMenuKeyboard.getMainMenuInlineKeyboard());
				return sendMessage;
			}
			case ("/menu") -> {
				log.trace("/menu command received: chatId={}, update={}", chatId, update);
				sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nMain Menu");
				sendMessage.setReplyMarkup(mainMenuKeyboard.getMainMenuInlineKeyboard());
				customMessageService.deleteMessage(chatId, receivedMessageId);
				return sendMessage;
			}
			case ("/help") -> {
				log.trace("/help command received: chatId={}, update={}", chatId, update);
				sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nUse /menu to open Main Menu");
				customMessageService.deleteMessage(chatId, receivedMessageId);
				return sendMessage;
			}
			case ("/settings") -> {
				log.trace("/settings command received: chatId={}, update={}", chatId, update);
				sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nSettings");
				sendMessage.setReplyMarkup(settingsKeyboard.getSettingsInlineKeyboard());
				customMessageService.deleteMessage(chatId, receivedMessageId);
				return sendMessage;
			}
			case ("/location") -> {
				log.trace("/location command received: chatId={}, update={}", chatId, update);
				String messageText = update.getMessage().getText();
				if (locationService.isParsebleCoordinates(messageText)) {
					ChatLocation location = locationService.parseCoordinates(messageText);
					Double latitude = location.getLatitude();
					Double longitude = location.getLongitude();
					chatRepository.updateLongitudeAndLatitudeById(chatId, latitude, longitude);
					sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nLocation is successfully set. Latitude: " + latitude + ", Longitude: " + longitude + ".");
					sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
					log.info("Location for chat is set: chatId={}, latitude={}, longitude={}", chatId, latitude, longitude);
				} else {
					sendMessage.setText("<pre>Astrophotography Helper</pre>\n\nLocation is not recognized. Please try again.");
					log.info("Location for chat is not recognized: chatId={}, messageText={}", chatId, messageText);
				}
				return sendMessage;
			}
			default -> {
				log.info("/command is not recognized: chatId={}, command={}, messageText={}", chatId, command, update.getMessage().getText());
				sendMessage.setText(command + " - command not recognized");
				return sendMessage;
			}
		}
	}
}
