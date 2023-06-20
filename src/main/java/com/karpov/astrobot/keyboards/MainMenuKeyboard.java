package com.karpov.astrobot.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MainMenuKeyboard {

	public static InlineKeyboardMarkup getMainMenuInlineKeyboard() {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

		List<InlineKeyboardButton> rowInLine1 = new ArrayList<>();
		InlineKeyboardButton inlineKeyboardButtonWeather = new InlineKeyboardButton("Weather");
		inlineKeyboardButtonWeather.setCallbackData("WeatherButtonPressed");

		List<InlineKeyboardButton> rowInLine2 = new ArrayList<>();
		InlineKeyboardButton inlineKeyboardButtonSettings = new InlineKeyboardButton("Settings");
		inlineKeyboardButtonSettings.setCallbackData("SettingsButtonPressed");
		InlineKeyboardButton inlineKeyboardButtonExitMenu = new InlineKeyboardButton("Exit");
		inlineKeyboardButtonExitMenu.setCallbackData("ExitButtonPressed");

		rowInLine1.add(inlineKeyboardButtonWeather);
		rowInLine2.add(inlineKeyboardButtonSettings);
		rowInLine2.add(inlineKeyboardButtonExitMenu);
		rowsInLine.add(rowInLine1);
		rowsInLine.add(rowInLine2);
		inlineKeyboardMarkup.setKeyboard(rowsInLine);
		return inlineKeyboardMarkup;
	}
}
